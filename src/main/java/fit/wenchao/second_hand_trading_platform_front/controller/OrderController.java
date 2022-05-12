package fit.wenchao.second_hand_trading_platform_front.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fit.wenchao.second_hand_trading_platform_front.dao.po.*;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.*;
import fit.wenchao.second_hand_trading_platform_front.utils.JsonResult;
import fit.wenchao.second_hand_trading_platform_front.utils.PageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static fit.wenchao.second_hand_trading_platform_front.utils.MapFieldName.mapFieldName;
import static fit.wenchao.second_hand_trading_platform_front.utils.WrapperUtils.eq;
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

                                orderVO.goodsName=value.goodsName;
                                orderVO.goodsSize=value.size;
                                orderVO.goodsOldDegree=value.oldDegree;
                                orderVO.goodsPic = nullable(value.pictureList())
                                        .filter(pictures -> pictures.size()!=0)
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

}
