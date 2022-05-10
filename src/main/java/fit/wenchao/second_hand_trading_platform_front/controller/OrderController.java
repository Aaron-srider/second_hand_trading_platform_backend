package fit.wenchao.second_hand_trading_platform_front.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fit.wenchao.second_hand_trading_platform_front.dao.po.GoodsPO;
import fit.wenchao.second_hand_trading_platform_front.dao.po.OrderPO;
import fit.wenchao.second_hand_trading_platform_front.dao.po.OrderStatusPO;
import fit.wenchao.second_hand_trading_platform_front.dao.po.UserPO;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.*;
import fit.wenchao.second_hand_trading_platform_front.utils.JsonResult;
import fit.wenchao.second_hand_trading_platform_front.utils.PageVo;
import fit.wenchao.second_hand_trading_platform_front.utils.Picture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static fit.wenchao.second_hand_trading_platform_front.controller.OrderController.MapReturnFieldName.map;
import static fit.wenchao.second_hand_trading_platform_front.utils.WrapperUtils.eq;
import static fit.wenchao.utils.collection.SimpleFactories.ofList;

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


    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    static @interface MapFieldName {
        String value();
    }


    static class MapReturnFieldName {
        public static List<Map<String, Object>> mapList(List<?> targetList) {
            return targetList.stream().map(MapReturnFieldName::map).collect(Collectors.toList());
        }

        public static Map<String, Object> map(Object target) {
            Field[] declaredField = target.getClass().getDeclaredFields();
            Map<String, Object> map = new HashMap<>();
            Arrays.stream(declaredField).forEach((field -> {
                field.setAccessible(true);
                MapFieldName annotation = field.getAnnotation(MapFieldName.class);

                if (annotation != null) {
                    String frontName = annotation.value();
                    try {
                        map.put(frontName, field.get(target));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e.getMessage());
                    }
                } else {
                    try {
                        map.put(field.getName(), field.get(target));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e.getMessage());
                    }
                }
            }));
            return map;
        }

        public static void main(String[] args) {
            OrderVO orderVOBuilder = OrderVO.builder().totalPrice(new BigDecimal("100"))
                    .build();
            OrderVO build = OrderVO.builder().totalPrice(new BigDecimal("111"))
                    .build();
            List<OrderVO> orderVOS = ofList(build, orderVOBuilder);
            List<Map<String, Object>> map = mapList(orderVOS);
            System.out.println(map);
        }
    }

    @GetMapping("/orderList")
    public JsonResult getPagedOrderList(Integer pageNo, Integer pageSize, Integer consumerId) {

        log.info("pageSize:{} pageNo:{} ", pageSize, pageNo);
        Page<OrderPO> objectPage = new Page<>();
        objectPage.setCurrent(pageNo);
        objectPage.setSize(pageSize);
        Page<OrderPO> page;

        page = orderDao.page(objectPage, eq("consumer_id", consumerId));

        Long total = page.getTotal();
        List<OrderPO> list = page.getRecords();

        List<Map<String, Object>> result = list.stream().map((orderPO -> {
            UserPO byId = userDao.getById(orderPO.getConsumerId());
            String customerName = byId.getName();

            String goodsName = "null";
            String goodsSize = "null";

            Integer goodsOldDegree = null;

            String goodsTypeName = "null";
            String goodsPic = null;
            GoodsPO byId1 = goodsDao.getById(orderPO.getGoodsId());
            if (byId1 != null) {
                goodsName = byId1.getGoodsName();
                goodsSize = byId1.getSize();

                goodsOldDegree = byId1.getOldDegree();

                goodsTypeName = goodsTypeDao.getById(byId1.getTypeId()).getName();
                List<Picture> pictures = JSONArray.parseArray(byId1.getPicture(), Picture.class);
                goodsPic = pictures.get(0).getBase64Str();
            }


            OrderStatusPO byId2 = orderStatusDao.getById(orderPO.getOrderStatusId());
            String orderStatusName = byId2.getName();


            orderPO.transPrice();

            OrderVO build = OrderVO.builder().build();
            BeanUtils.copyProperties(orderPO, build);

            BigDecimal price = orderPO.getPrice();
            BigDecimal discount = orderPO.getDiscount();
            BigDecimal multiply = price.multiply(discount);
            multiply = multiply.setScale(2, RoundingMode.DOWN);
            build.setCustomerName(customerName)
                    .setGoodsName(goodsName)
                    .setOrderStatusName(orderStatusName)
                    .setGoodsSize(goodsSize)
                    .setGoodsOldDegree(goodsOldDegree)
                    .setGoodsTypeName(goodsTypeName)
                    .setPriceAfterDiscount(multiply)
                    .setGoodsPic(goodsPic);

            Map<String, Object> map = map(build);
            return map;
        })).collect(Collectors.toList());

        PageVo<Map<String, Object>> build = PageVo.<Map<String, Object>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .pageData(result)
                .total(Math.toIntExact(total))
                .build();
        return JsonResult.ok(build);
    }

}
