package fit.wenchao.second_hand_trading_platform_front.service.impl;

import fit.wenchao.second_hand_trading_platform_front.dao.repo.OrderDao;
import fit.wenchao.second_hand_trading_platform_front.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>
 * Order 服务类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
public class OrderServiceImpl implements OrderService{

    @Autowired
    OrderDao orderDao;

}


