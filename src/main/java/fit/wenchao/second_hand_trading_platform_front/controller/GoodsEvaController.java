package fit.wenchao.second_hand_trading_platform_front.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fit.wenchao.second_hand_trading_platform_front.dao.po.*;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.*;
import fit.wenchao.second_hand_trading_platform_front.utils.*;
import fit.wenchao.utils.dateUtils.DateUtils;
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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static fit.wenchao.second_hand_trading_platform_front.utils.WrapperUtils.eq;
import static fit.wenchao.utils.collection.SimpleFactories.ofJson;
import static fit.wenchao.utils.optional.OptionalUtils.nullable;

/**
 * <p>
 * 商品评价 前端控制器
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
@RestController
@Slf4j
@CrossOrigin(allowCredentials = "true")
public class GoodsEvaController {

    @Autowired
    GoodsDao goodsDao;

    @Autowired
    GoodsEvaDao goodsEvaDao;

    @Autowired
    UserDao userDao;

    @Autowired
    OrderDao orderDao;


    @GetMapping("/goodsComments")
    public JsonResult goodsEvaList(Page<GoodsEvaPO> page, Integer goodsId) {

        //计算星级
        Optional<Object> avgStar = getAvgStar(goodsId);

        //获取page原始数据
        goodsEvaDao.page(page, eq("goods_id", goodsId));

        //构造pageVo
        PageVo<Map<String, Object>> resultPage = constructPageInfo(page);

        return JsonResult.ok(ofJson(
                "page", resultPage,
                "avgStar", avgStar.orElse(null)));
    }

    private Optional<Object> getAvgStar(Integer goodsId) {

        GoodsEvaPO goodsEvaPOWithAvgStar = goodsEvaDao.getOne(
                WrapperUtils
                        .<GoodsEvaPO>eq("goods_id", goodsId)
                        .select("avg(star) as avgStar")
        );
        return nullable(goodsEvaPOWithAvgStar)
                .map(GoodsEvaPO::getAvgStar)
                .map((avg) -> avg.setScale(2, RoundingMode.DOWN))
                ;


    }

    private PageVo<Map<String, Object>> constructPageInfo(Page<GoodsEvaPO> page) {

        //trans result 2 map
        List<Map<String, Object>> result = page.getRecords().stream()
                .map((this::tran2GoodsEvaVO))
                .map((MapFieldName::mapFieldName))
                .collect(Collectors.toList());

        return PageVo.<Map<String, Object>>builder()
                .pageSize((int) page.getSize())
                .pageNo((int) page.getCurrent())
                .pageData(result)
                .total(Math.toIntExact(page.getTotal()))
                .build();

    }

    public static void main(String[] args) {
        System.out.println(B.class.getDeclaredFields().length);
    }


    static class A {
        String a;
        String b;
    }

    static class B extends A {
        String c;
        String d;
    }

    private GoodsEvaVO tran2GoodsEvaVO(GoodsEvaPO goodsEvaPO) {
        //id
        //comment:
        //consumerName:
        //        count:1,
        //        star:4.2,
        //        commentTime:'1011-11-22 22:22:22',
        GoodsEvaVO goodsEvaVO = new GoodsEvaVO();

        BeanUtils.copyProperties(goodsEvaPO, goodsEvaVO);

        UserPO consumer = userDao.getById(goodsEvaPO.getConsumerId());
        goodsEvaVO.consumerName = consumer.name;


        OrderPO orderPO = orderDao.getById(goodsEvaPO.orderId);
        if (orderPO != null) {
            goodsEvaVO.boughtGoodsCount = orderPO.count;
        }

        return goodsEvaVO;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Accessors(chain = true)
    public static class Comment {
        public Integer userId;
        public Integer goodsId;
        public Integer orderId;
        public String storeComment;
        public String goodsComment;
        public Integer goodsStar;
        public Integer storeStar;
    }


    @Autowired
    StoreDao storeDao;

    @Autowired
    StoreEvaDao storeEvaDao;

    @PostMapping("/submitCommit")
    @Transactional
    public JsonResult submitCommit(@RequestBody Comment comment) {
        log.info("comment:{}", comment);
        nullable(comment).
                filter(commentValue -> commentValue.userId != null &&
                        commentValue.goodsId != null &&
                        commentValue.orderId != null &&
                        commentValue.storeComment != null &&
                        commentValue.goodsComment != null &&
                        commentValue.goodsStar != null &&
                        commentValue.storeStar != null
                )
                .orElseThrow(() -> new BackendException(ResultCodeEnum.FRONT_PARAM_ERROR, null));

        List<GoodsEvaPO> goodsEvaPOList = goodsEvaDao.list(eq("order_id", comment.orderId));
        nullable(goodsEvaPOList)
                .filter((goodsEvaPOS -> goodsEvaPOS.size() != 0))
                .ifPresent(goodsEvaPOValue -> {
                    throw new BackendException(ResultCodeEnum.ORDER_COMMENT_EXISTS, "订单已评价");
                });

        List<StoreEvaPO> storeEvaPOList = storeEvaDao.list(eq("order_id", comment.orderId));
        nullable(storeEvaPOList).filter((storeEvaPOs -> storeEvaPOs.size() != 0))
                .ifPresent(storeEvaPOValue -> {
                    throw new BackendException(ResultCodeEnum.ORDER_COMMENT_EXISTS, "订单已评价");
                });

        GoodsEvaPO goodsEvaPO = new GoodsEvaVO();
        goodsEvaPO.comment = comment.goodsComment;
        goodsEvaPO.goodsId = comment.goodsId;
        goodsEvaPO.orderId = comment.orderId;
        goodsEvaPO.consumerId = comment.userId;
        goodsEvaPO.star = new BigDecimal(comment.goodsStar);
        goodsEvaPO.commentTime = DateUtils.formatDate(new Date());
        log.info("insert goodsEva:{}", goodsEvaPO);
        goodsEvaDao
                .save(goodsEvaPO);

        GoodsPO goodsPO = goodsDao.getById(comment.goodsId);
        Integer storeId = nullable(goodsPO)
                .map(goodsPO1 -> goodsPO1.storeId)
                .orElseThrow(() -> new BackendException(
                        ResultCodeEnum.DATA_ERROR, null
                ));


        StoreEvaPO storeEvaPO = new StoreEvaPO();
        storeEvaPO.storeId = storeId;
        storeEvaPO.comment = comment.storeComment;
        storeEvaPO.goodsId = comment.goodsId;
        storeEvaPO.orderId = comment.orderId;
        storeEvaPO.userId = comment.userId;
        storeEvaPO.star = new BigDecimal(comment.storeStar);
        storeEvaPO.commentTime = DateUtils.formatDate(new Date());
        log.info("insert storeEva:{}", storeEvaPO);
        storeEvaDao
                .save(storeEvaPO);


        OrderPO orderPO = orderDao.getById(comment.orderId);
        orderPO.orderStatusId = 6;
        orderDao.updateById(orderPO);

        return JsonResult.ok(ofJson("goodsEva", goodsEvaPO,
                "storeEva", storeEvaPO));
    }

    @GetMapping("/getOrderCommentByUserIdAndOrderId")
    public JsonResult getOrderCommentByUserIdAndOrderId(Integer userId, Integer orderId) {
        log.info("query comment, userId:{}, orderId:{}", userId, orderId);

        if (userId == null || orderId == null) {
            throw new BackendException(ResultCodeEnum.FRONT_PARAM_ERROR, null);
        }

        List<GoodsEvaPO> goodsEvaPOList = goodsEvaDao.list(
                WrapperUtils.
                        <GoodsEvaPO>eq("order_id", orderId)
                        .eq("consumer_id", userId));

        log.info("query result :{}", goodsEvaPOList);

        GoodsEvaPO goodsEvaPO = nullable(goodsEvaPOList).filter((goodsEvaPOS -> {
                    return goodsEvaPOS.size() == 1;
                })).map((goodsEvaPOS -> {
                    return goodsEvaPOS.get(0);
                })).
                orElseThrow(() -> {
                    return new BackendException(ResultCodeEnum.DATA_ERROR, null);
                });

        log.info("goodsEva query result :{}", goodsEvaPO);


        List<StoreEvaPO> storeEvaPOList = storeEvaDao.list(
                WrapperUtils.
                        <StoreEvaPO>eq("order_id", orderId)
                        .eq("user_id", userId));

        log.info("query result :{}", storeEvaPOList);

        StoreEvaPO storeEvaPO = nullable(storeEvaPOList).filter((storeEvaPOS -> {
                    return storeEvaPOS.size() == 1;
                })).map((storeEvaPOS -> {
                    return storeEvaPOS.get(0);
                })).
                orElseThrow(() -> {
                    return new BackendException(ResultCodeEnum.DATA_ERROR, null);
                });

        log.info("storeEvaPO query result :{}", storeEvaPO);
        //
        //public Integer userId;
        //public Integer goodsId;
        //public Integer orderId;
        //public String storeComment;
        //public String goodsComment;
        //public Integer goodsStar;
        //public Integer storeStar;

        Comment comment = new Comment();

        comment.userId = userId;
        comment.orderId = orderId;
        comment.storeComment = storeEvaPO.comment;
        comment.goodsComment = goodsEvaPO.comment;
        comment.storeStar = storeEvaPO.star.intValue();
        comment.goodsStar = goodsEvaPO.star.intValue();



        return JsonResult.ok(comment);
    }

}
