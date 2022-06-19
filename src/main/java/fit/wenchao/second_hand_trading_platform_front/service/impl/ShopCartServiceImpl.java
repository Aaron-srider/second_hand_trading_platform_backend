package fit.wenchao.second_hand_trading_platform_front.service.impl;

import fit.wenchao.second_hand_trading_platform_front.dao.repo.ShopCartDao;
import fit.wenchao.second_hand_trading_platform_front.service.ShopCartService;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * <p>
 * ShopCart 服务类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
public class ShopCartServiceImpl implements ShopCartService{

    @Autowired
    ShopCartDao shopCartDao;

}


