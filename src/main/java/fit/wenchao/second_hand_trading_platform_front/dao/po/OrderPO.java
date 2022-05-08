
package fit.wenchao.second_hand_trading_platform_front.dao.po;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单表

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
public class OrderPO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 消费者id
     */
    private Integer consumerId;

    /**
     * 商品id
     */
    private Integer goodsId;

    /**
     * 商品折扣
     */
    private BigDecimal discount;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 订单状态_id
     */
    private Integer orderStatusId;

    /**
     * 下单时间
     */
    private String orderTime;

    /**
     * 订单总额
     */
    private BigDecimal totalPrice;

    /**
     * 购买数量
     */
    private Integer count;



}
