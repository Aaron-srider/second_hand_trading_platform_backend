
package fit.wenchao.second_hand_trading_platform_front.dao.po;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;

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

    public static final long serialVersionUID = 1L;

    /**
     * 商品评价
     */
    @TableId(type=IdType.AUTO)
    public Integer id;

    /**
     * 商品id
     */
    public Integer goodsId;

    /**
     * 购买者id
     */
    public Integer consumerId;

    /**
     * 用户评价星级（0~4）
     */
    public BigDecimal star;

    /**
     * 文字评价
     */
    public String comment;

    /**
     * 评价时间
     */
    public String commentTime;

    /**
     * 评价对应的订单id
     */
    public Integer orderId;


    // 聚合字段
    @TableField(exist = false)
    public BigDecimal avgStar;

    
}
