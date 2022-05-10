package fit.wenchao.second_hand_trading_platform_front.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class OrderVO {

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
    @OrderController.MapFieldName("total_price")
    public BigDecimal totalPrice;

    /**
     * 购买数量
     */
    public Integer count;


    /**
     * 购买时的商品单价
     */
    public BigDecimal price;

    //vo新增//

    /**
     * 购买者名称
     */
    public String customerName;

    /**
     * 商品名称
     */
    public String goodsName;

    /**
     * 订单状态名称
     */
    @OrderController.MapFieldName("order_status")
    public String orderStatusName;

    public String goodsSize;

    public Integer goodsOldDegree;

    @OrderController.MapFieldName("goodsType")
    public String goodsTypeName;

    @OrderController.MapFieldName("price_after_discount")
    public BigDecimal priceAfterDiscount;

    public String goodsPic;
}
