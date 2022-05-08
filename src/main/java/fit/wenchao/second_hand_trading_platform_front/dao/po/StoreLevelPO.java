
package fit.wenchao.second_hand_trading_platform_front.dao.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商家等级，不同等级有不同费率，费率可以由管理员设置
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@TableName("store_level")
public class StoreLevelPO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 商家等级（0~4）
     */
    private Integer storeLevel;

    /**
     * 商家交易费率

1
0.1%
 
2
0.2%
 
3
0.5%
 
4
0.75%
 
5
1%
 

     */
    private BigDecimal rate;



}
