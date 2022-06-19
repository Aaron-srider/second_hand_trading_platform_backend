package fit.wenchao.second_hand_trading_platform_front.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fit.wenchao.second_hand_trading_platform_front.dao.po.*;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.*;
import fit.wenchao.second_hand_trading_platform_front.service.OrderService;
import fit.wenchao.second_hand_trading_platform_front.utils.JsonResult;
import fit.wenchao.second_hand_trading_platform_front.utils.PageVo;
import fit.wenchao.second_hand_trading_platform_front.utils.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static fit.wenchao.second_hand_trading_platform_front.utils.MapFieldName.mapFieldName;
import static fit.wenchao.second_hand_trading_platform_front.utils.WrapperUtils.eq;
import static fit.wenchao.utils.collection.SimpleFactories.ofJson;
import static fit.wenchao.utils.optional.OptionalUtils.nullable;

/**
 * <p>
 * 订单表
 * 前端控制器
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
@RestController
@Slf4j
@CrossOrigin(allowCredentials = "true")
public class OrderController {

    @Autowired
    OrderDao orderDao;

    @Autowired
    UserDao userDao;

    @Autowired
    GoodsDao goodsDao;

    @Autowired
    OrderStatusDao orderStatusDao;

    @Autowired
    GoodsTypeDao goodsTypeDao;


    @GetMapping("/orderList")
    public JsonResult getPagedOrderList(Page<OrderPO> page, Integer consumerId) {

        List<OrderPO> orderPOList = orderDao.page(page, eq("consumer_id", consumerId))
                .getRecords();

        List<Map<String, Object>> result = orderPOList.stream()
                .map((orderPO -> {
                    orderPO.scalePrice();

                    OrderVO orderVO = OrderVO.builder().build();
                    BeanUtils.copyProperties(orderPO, orderVO);

                    GoodsPO orderGoods = goodsDao.getById(orderPO.getGoodsId());
                    nullable(orderGoods)
                            .ifPresent((value) -> {
                                orderVO.goodsTypeName =
                                        nullable(goodsTypeDao.getById(value.getTypeId()))
                                                .map(GoodsTypePO::getName)
                                                .orElse(null);

                                orderVO.goodsName = value.goodsName;
                                orderVO.goodsSize = value.size;
                                orderVO.goodsOldDegree = value.oldDegree;
                                orderVO.goodsPic = nullable(value.pictureList())
                                        .filter(pictures -> pictures.size() != 0)
                                        .map((pictures -> pictures.get(0).getBase64Str()))
                                        .orElse(null);
                            });

                    UserPO orderConsumer = userDao.getById(orderPO.getConsumerId());
                    String customerName = nullable(orderConsumer)
                            .map(UserPO::getName)
                            .orElse(null);
                    OrderStatusPO orderStatusPO = orderStatusDao.getById(orderPO.getOrderStatusId());
                    String orderStatusName = nullable(orderStatusPO)
                            .map(OrderStatusPO::getName)
                            .orElse(null);
                    orderVO.setCustomerName(customerName)
                            .setOrderStatusName(orderStatusName)
                            .setPriceAfterDiscount(orderPO.priceAfterDiscount())
                    ;

                    return mapFieldName(orderVO);
                })).collect(Collectors.toList());

        PageVo<Map<String, Object>> build = PageVo.<Map<String, Object>>builder()
                .pageNo(Math.toIntExact(page.getCurrent()))
                .pageSize(Math.toIntExact(page.getSize()))
                .pageData(result)
                .total(Math.toIntExact(page.getTotal()))
                .build();

        return JsonResult.ok(build);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Accessors(chain = true)
    public static class BuyVO {
        public String buyToken;
        public Integer userId;
        public Integer goodsId;
        public Integer buyCount;
        public Integer addressId;
        public Boolean usePoint;
    }

    @Autowired
    OrderService orderService;

    @PostMapping("/buy")
    public JsonResult buy(@RequestBody BuyVO buyVO) {
        orderService.buy(buyVO);
        return JsonResult.ok(null);
    }

    @Autowired
    StoreDao storeDao;

    @Autowired
    SysAccountDao sysAccountDao;

    @PutMapping("/confirmReceiveGoods")
    @Transactional
    public JsonResult JsonResult(@RequestBody Map<String, Integer> orderIdMap) {
        Integer orderId = orderIdMap.get("orderId");

        //修改订单状态
        OrderPO orderPO = orderDao.getById(orderId);
        nullable(orderPO).map((value) -> {
            value.orderStatusId = 1;
            orderDao.updateById(value);
            return value;
        }).orElseThrow(() -> new BackendException(ResultCodeEnum.DATA_ERROR, "订单不存在"));


        //商家到账
        SysAccountPO sysAccountPO = sysAccountDao.getOne(eq("order_id", orderId));

        nullable(sysAccountPO).map((value) -> {

            BigDecimal mount = sysAccountPO.mount;
            Integer goodsId = orderPO.goodsId;
            GoodsPO goodsPO = goodsDao.getById(goodsId);
            nullable(goodsPO).map((goodsPOValue) -> {
                Integer storeId = goodsPO.storeId;
                StorePO storePO = storeDao.getById(storeId);
                nullable(storePO).map((storeValue) -> {
                    Integer merchantId = storeValue.merchantId;
                    UserPO merchant = userDao.getById(merchantId);
                    nullable(merchant).map((merchantValue) -> {
                        nullable(merchantValue.amount).map((merchantAmount) -> {
                            merchantValue.amount = merchantValue.amount.add(mount);
                            userDao.updateById(merchantValue);
                            return merchantValue;
                        }).orElseThrow(() -> new BackendException(ResultCodeEnum.DATA_ERROR, null));
                        return merchantValue;
                    }).orElseThrow(() -> new BackendException(ResultCodeEnum.DATA_ERROR, "store data error"));
                    ;
                    return storeValue;
                }).orElseThrow(() -> new BackendException(ResultCodeEnum.DATA_ERROR, "goods data error"));
                ;
                return goodsPOValue;
            }).orElseThrow(() -> new BackendException(ResultCodeEnum.DATA_ERROR, "order data error"));
            ;

            return value;
        }).orElseThrow(() -> new BackendException(ResultCodeEnum.DATA_ERROR, null));
        ;

        UserPO consumer = userDao.getById(orderPO.consumerId);

        nullable(consumer).map((consumerValue) -> {
            consumerValue.credits += orderPO.totalPrice.intValue();
            userDao.updateById(consumerValue);
            return consumerValue;
        }).orElseThrow(() -> new BackendException(ResultCodeEnum.DATA_ERROR, "order data error"));
        return JsonResult.ok(null);
    }

    @GetMapping("/getPaymentRecords")
    public JsonResult getPaymentRecords(Page<OrderPO> page, Integer userId) {
        System.out.println(userId);
        System.out.println(page);


        orderDao.page(page, eq("consumer_id", userId));

        List<OrderPO> records = page.getRecords();

        List<PaymentRecordVO> paymentRecordVOS =
                nullable(records)
                        .map((orderPOS) -> {
                            List<PaymentRecordVO> paymentRecordVOList = new ArrayList<PaymentRecordVO>();

                            orderPOS.forEach((orderPO) -> {
                                PaymentRecordVO paymentRecordVO1 = new PaymentRecordVO();
                                paymentRecordVO1.orderNo =
                                        orderPO.orderId;
                                paymentRecordVO1.orderTime =
                                        orderPO.orderTime;

                                paymentRecordVO1.totalPrice =
                                        orderPO.totalPrice
                                                .setScale(2, RoundingMode.DOWN);


                                Integer goodsId = orderPO.goodsId;

                                String merchantName = nullable(goodsId)
                                        .map((goodsIdValue) -> goodsDao.getById(goodsId)).map((goodsPOValue -> goodsPOValue.storeId))
                                        .map((storeIdValue) -> storeDao.getById(storeIdValue))
                                        .map((storePOValue) -> storePOValue.merchantId)
                                        .map((merchantIdValue) -> userDao.getById(merchantIdValue))
                                        .map((merchantValue) -> merchantValue.name)
                                        .orElseThrow(() -> new BackendException(ResultCodeEnum.DATA_ERROR, null));

                                paymentRecordVO1.receiverUserName = merchantName;
                                paymentRecordVOList.add(paymentRecordVO1);
                            });

                            return paymentRecordVOList;
                        }).orElseGet(ArrayList::new);

        Page<PaymentRecordVO> paymentRecordVOPage = new Page<>();
        paymentRecordVOPage.setRecords(paymentRecordVOS);
        paymentRecordVOPage.setTotal(page.getTotal());
        paymentRecordVOPage.setCurrent(page.getCurrent());
        paymentRecordVOPage.setSize(page.getSize());
        return JsonResult.ok(paymentRecordVOPage);
    }

    @GetMapping("/getUserRestMoney")
    public JsonResult getUserRestMoney(Integer userId) {
        BigDecimal userAmount = nullable(userId).map((userIdValue) -> {
            UserPO userPO = userDao.getById(userId);
            return userPO.amount.setScale(2, RoundingMode.DOWN);
        }).orElseThrow(() -> new BackendException(ResultCodeEnum.FRONT_PARAM_ERROR,
                "userId missing"));

        return JsonResult.ok(ofJson("restMoney", userAmount));
    }


    @GetMapping("/getUserRestPoint")
    public JsonResult getUserRestPoint(Integer userId) {
        Integer credits = nullable(userId).map((userIdValue) -> {
            UserPO userPO = userDao.getById(userId);
            return userPO.credits;
        }).orElseThrow(() -> new BackendException(ResultCodeEnum.FRONT_PARAM_ERROR,
                "userId missing"));

        return JsonResult.ok(ofJson("restPoint", credits));
    }

}
