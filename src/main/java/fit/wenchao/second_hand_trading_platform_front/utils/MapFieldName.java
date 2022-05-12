package fit.wenchao.second_hand_trading_platform_front.utils;

import fit.wenchao.second_hand_trading_platform_front.controller.OrderVO;
import fit.wenchao.utils.reflect.ReflectUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static fit.wenchao.utils.collection.SimpleFactories.ofList;

public class MapFieldName {
    public static List<Map<String, Object>> mapList(List<?> targetList) {
        return targetList.stream().map(MapFieldName::mapFieldName).collect(Collectors.toList());
    }

    public static Map<String, Object> mapFieldName(Object target) {
        Field[] declaredField = ReflectUtils.getAllFields(target.getClass());
        Map<String, Object> map = new HashMap<>();
        Arrays.stream(declaredField).forEach((field -> {
            field.setAccessible(true);
            MapName annotation = field.getAnnotation(MapName.class);

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