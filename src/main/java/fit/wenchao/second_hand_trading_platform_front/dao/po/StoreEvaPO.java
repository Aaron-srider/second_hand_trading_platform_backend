
package fit.wenchao.second_hand_trading_platform_front.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 店铺评价
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
@TableName("store_eva")
public class StoreEvaPO implements Serializable {

    public static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    public Integer id;

    /**
     * 商品id
     */
    public Integer goodsId;

    /**
     * 评价的用户id
     */
    public Integer userId;

    /**
     * 受评价的店铺id
     */
    public Integer storeId;

    /**
     * 订单id
     */
    public Integer orderId;

    /**
     * 评价星级（0~4）
     */
    public BigDecimal star;

    /**
     * 评价文字
     */
    public String comment;

    /**
     * 评价时间
     */
    public String commentTime;



}
