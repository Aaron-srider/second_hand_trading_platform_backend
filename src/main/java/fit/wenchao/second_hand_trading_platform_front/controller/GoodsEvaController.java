package fit.wenchao.second_hand_trading_platform_front.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fit.wenchao.second_hand_trading_platform_front.dao.po.*;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.GoodsDao;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.GoodsEvaDao;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.OrderDao;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.UserDao;
import fit.wenchao.second_hand_trading_platform_front.utils.JsonResult;
import fit.wenchao.second_hand_trading_platform_front.utils.MapFieldName;
import fit.wenchao.second_hand_trading_platform_front.utils.PageVo;
import fit.wenchao.second_hand_trading_platform_front.utils.WrapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.RoundingMode;
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
                .pageNo((int) page.getSize())
                .pageSize((int) page.getCurrent())
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
}
