
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
 * 商品评价
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
@TableName("goods_eva")
public class GoodsEvaPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品评价
     */
    private Integer id;

    /**
     * 商品id
     */
    private Integer goodsId;

    /**
     * 购买者id
     */
    private Integer consumerId;

    /**
     * 用户评价星级（0~4）
     */
    private Integer star;

    /**
     * 文字评价
     */
    private String comment;

    /**
     * 评价时间
     */
    private String commentTime;



}
