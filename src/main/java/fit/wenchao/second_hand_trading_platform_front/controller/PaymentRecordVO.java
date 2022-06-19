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
public class PaymentRecordVO {
    public String orderNo;

    public String orderTime;

    public BigDecimal totalPrice;

    public String receiverUserName;
}
