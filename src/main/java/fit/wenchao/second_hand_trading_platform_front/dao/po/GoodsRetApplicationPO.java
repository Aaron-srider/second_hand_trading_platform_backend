
package fit.wenchao.second_hand_trading_platform_front.dao.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户的退货申请，用户可以在到货后24小时内提出退货申请
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
@TableName("goods_ret_application")
public class GoodsRetApplicationPO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 消费者id
     */
    private Integer consumerId;

    /**
     * 商品id
     */
    private Integer orderId;

    /**
     * 退货原因
     */
    private String retReason;

    /**
     * 商家是否同意，（-1默认，0不同意，1同意）
     */
    private Integer merchantPermit;



}
