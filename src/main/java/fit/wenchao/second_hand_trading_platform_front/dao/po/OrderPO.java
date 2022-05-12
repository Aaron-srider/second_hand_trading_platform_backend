
package fit.wenchao.second_hand_trading_platform_front.dao.po;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import static fit.wenchao.utils.optional.OptionalUtils.nullable;

/**
 * <p>
 * 订单表
 *
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
@TableName("`order`")
public class OrderPO implements Serializable {

    public static final long serialVersionUID = 1L;

    public Integer id;

    /**
     * 消费者id
     */
    public Integer consumerId;

    /**
     * 商品id
     */
    public Integer goodsId;

    /**
     * 商品折扣
     */
    public BigDecimal discount;

    /**
     * 订单号
     */
    public String orderId;

    /**
     * 订单状态_id
     */
    public Integer orderStatusId;

    /**
     * 下单时间
     */
    public String orderTime;

    /**
     * 订单总额
     */
    public BigDecimal totalPrice;

    /**
     * 购买数量
     */
    public Integer count;


    /**
     * 购买时的商品单价
     */
    public BigDecimal price;

    public void scalePrice() {

        price = nullable(price)
                .map((value) -> value.setScale(2, RoundingMode.DOWN))
                .orElse(null);

        totalPrice = nullable(totalPrice)
                .map((value) -> value.setScale(2, RoundingMode.DOWN))
                .orElse(null);
    }

    /**
     * @return if price and discount exists, return price*discount, or null;
     */
    public BigDecimal priceAfterDiscount() {
        if(price!=null && discount!=null) {
            return price.multiply(discount);
        }
        return null;
    }
}
