package fit.wenchao.second_hand_trading_platform_front.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class PageVo<T> {
    Integer total;
    List<T> pageData;
    Integer pageNo;
    Integer pageSize;
}
