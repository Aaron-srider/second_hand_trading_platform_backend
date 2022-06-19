package fit.wenchao.second_hand_trading_platform_front.service;

import fit.wenchao.second_hand_trading_platform_front.controller.OrderController;

/**
 * <p>
 * Order 服务类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
public interface OrderService {
    void buy(OrderController.BuyVO buyVO);
}


