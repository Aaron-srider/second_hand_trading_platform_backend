
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

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 商品id
     */
    private Integer goodsId;

    /**
     * 评价的用户id
     */
    private Integer userId;

    /**
     * 受评价的店铺id
     */
    private Integer storeId;

    /**
     * 评价星级（0~4）
     */
    private Integer star;

    /**
     * 评价文字
     */
    private String comment;

    /**
     * 评价时间
     */
    private String commentTime;



}
