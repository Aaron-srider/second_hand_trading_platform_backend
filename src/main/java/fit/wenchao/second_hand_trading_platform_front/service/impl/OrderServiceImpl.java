package fit.wenchao.second_hand_trading_platform_front.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fit.wenchao.second_hand_trading_platform_front.controller.BackendException;
import fit.wenchao.second_hand_trading_platform_front.controller.OrderController;
import fit.wenchao.second_hand_trading_platform_front.dao.po.GoodsPO;
import fit.wenchao.second_hand_trading_platform_front.dao.po.OrderPO;
import fit.wenchao.second_hand_trading_platform_front.dao.po.SysAccountPO;
import fit.wenchao.second_hand_trading_platform_front.dao.po.UserPO;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.GoodsDao;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.OrderDao;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.SysAccountDao;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.UserDao;
import fit.wenchao.second_hand_trading_platform_front.service.OrderService;

import fit.wenchao.second_hand_trading_platform_front.utils.JsonResult;
import fit.wenchao.second_hand_trading_platform_front.utils.ResultCodeEnum;
import fit.wenchao.utils.dateUtils.DateUtils;
import fit.wenchao.utils.optional.OptionalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Optional;

import static fit.wenchao.second_hand_trading_platform_front.utils.WrapperUtils.eq;
import static fit.wenchao.utils.optional.OptionalUtils.nullable;
import static java.awt.Event.DOWN;


/**
 * <p>
 * Order 服务类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    UserDao userDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    GoodsDao goodsDao;

    @Autowired
    SysAccountDao sysAccountDao;

    @Transactional
    public void buy(OrderController.BuyVO buyVO) {
        String buyToken = nullable(buyVO.buyToken)
                .filter((buyTokenValue) -> !buyTokenValue.equals(""))
                .orElseThrow(() -> new BackendException(ResultCodeEnum.FRONT_PARAM_ERROR, null));

        OrderPO orderPO = orderDao.getOne(eq("order_id", buyToken));

        nullable(orderPO).ifPresent((orderPOValue) -> {
            throw new BackendException(ResultCodeEnum.ORDER_EXISTS, null);
        });

        if (buyVO.userId == null || buyVO.goodsId == null || buyVO.buyCount == null) {
            throw new BackendException(ResultCodeEnum.FRONT_PARAM_ERROR, null);
        }


        GoodsPO goodsById = goodsDao.getById(buyVO.goodsId);
        if(goodsById.stockQuantity < 1) {
            throw new BackendException(ResultCodeEnum.STOCK_NOT_ENOUGH, "商品库存不足");
        }


        OrderPO orderPO1 = new OrderPO();
        orderPO1.consumerId = buyVO.userId;
        orderPO1.orderId = buyToken;
        orderPO1.goodsId = buyVO.goodsId;
        orderPO1.orderTime = DateUtils.formatDate(new Date());
        orderPO1.orderStatusId = 2;

        GoodsPO goodsPO = goodsDao.getById(buyVO.goodsId);
        nullable(goodsPO)
                .map((value) -> {
                    orderPO1.price = value.price;
                    orderPO1.discount = value.discount;
                    BigDecimal totalPrice = null;

                    //计算总金额
                    if (value.price != null && value.discount != null) {
                        totalPrice = value.price.multiply(value.discount)
                                .setScale(2, RoundingMode.DOWN)
                                .multiply(new BigDecimal(buyVO.buyCount))
                                .setScale(2, RoundingMode.DOWN);
                    } else {
                        throw new BackendException(ResultCodeEnum
                                .DATA_ERROR, "商品数据异常");
                    }

                    orderPO1.totalPrice = totalPrice;

                    UserPO userPO = userDao.getById(buyVO.userId);

                    nullable(userPO).orElseThrow(() -> new BackendException(ResultCodeEnum.
                            USER_NOT_EXISTS, null));

                    Integer userCredits = nullable(userPO.credits)
                            .orElseThrow(() -> new BackendException(ResultCodeEnum
                                    .USER_DATA_ERROR, null));


                    BigDecimal goodsMaxPoint = null;

                    //计算积分抵扣
                    if (buyVO.usePoint != null && buyVO.usePoint) {
                        goodsMaxPoint = totalPrice.multiply(new BigDecimal(2));


                        BigDecimal pointCanUse = goodsMaxPoint.compareTo(
                                new BigDecimal(userCredits)) >= 0
                                ? new BigDecimal(userCredits) :
                                goodsMaxPoint;

                        orderPO1.ifUsePoint = true;

                        orderPO1.usePoint = Double.valueOf(pointCanUse.toString())
                                .intValue();

                        BigDecimal pointPrice = pointCanUse.divide(
                                new BigDecimal(100),
                                2, RoundingMode.DOWN
                        );


                        orderPO1.totalPrice = totalPrice.subtract(pointPrice)
                                .setScale(2, RoundingMode.DOWN);

                        //扣除用户积分
                        UserPO userPO1 = userDao.getById(buyVO.userId);
                        nullable(userPO1).map((userPOValue) -> {
                            userPO1.credits = userPO1.credits - pointCanUse.intValue();
                            userDao.updateById(userPOValue);
                            return userPOValue;
                        }).orElseThrow(() -> new BackendException(ResultCodeEnum
                                .USER_DATA_ERROR, null));

                        log.info("扣除积分：{}", pointCanUse);

                        log.info("积分抵扣后需付款：{}", pointCanUse);

                    }


                    //扣除用户金钱
                    nullable(userPO).map((userPOValue) -> {
                        if (userPOValue.amount == null) {
                            throw new BackendException(ResultCodeEnum
                                    .USER_DATA_ERROR, null);
                        }
                        if (userPOValue.amount.compareTo(orderPO1.totalPrice) < 0) {
                            throw new BackendException(ResultCodeEnum.MONEY_NOT_ENOUGH, null);
                        }
                        userPOValue.amount = userPOValue.amount.subtract(orderPO1.totalPrice)
                                .setScale(2, RoundingMode.DOWN);
                        userDao.updateById(userPOValue);
                        log.info("扣除用户金钱：{}",  userPOValue.amount);
                        return userPOValue;
                    });

                    orderPO1.count = buyVO.buyCount;
                    return value;
                });


        orderPO1.addressId = buyVO.addressId;
        orderDao.save(orderPO1);
        log.info("新增订单：{}",  orderPO1);

        GoodsPO goodsById1 = goodsDao.getById(buyVO.goodsId);
        goodsById1.stockQuantity -= buyVO.buyCount;
        goodsDao.updateById(goodsById1);
        log.info("修改商品库存 - ：{}", buyVO.buyCount);

        GoodsPO goodsById2 = goodsDao.getById(buyVO.goodsId);
        goodsById2.historySales += buyVO.buyCount;
        goodsDao.updateById(goodsById2);
        log.info("修改商品历史销量 + ：{}", buyVO.buyCount);

        SysAccountPO sysAccountPO = new SysAccountPO();
        sysAccountPO.orderId = orderPO1.id;
        sysAccountPO.mount = orderPO1.totalPrice.setScale(2, RoundingMode.DOWN);
        sysAccountDao.save(sysAccountPO);
        log.info("系统账户入账：{}",  sysAccountPO.mount);
    }





}


