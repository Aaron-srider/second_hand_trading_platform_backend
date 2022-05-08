package fit.wenchao.second_hand_trading_platform_front.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public class WrapperUtils {
    public static <T> QueryWrapper<T> eq(String column, Object value) {
        return new QueryWrapper<T>().eq(column, value);
    }
}