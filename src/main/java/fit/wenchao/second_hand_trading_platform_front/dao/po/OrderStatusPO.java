package fit.wenchao.second_hand_trading_platform_front.dao.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@TableName("`order_status`")
public class OrderStatusPO {
    private Integer id;
    private String name;
    private String code;
}
