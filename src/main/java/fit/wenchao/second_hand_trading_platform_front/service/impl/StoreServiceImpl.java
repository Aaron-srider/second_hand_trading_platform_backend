package fit.wenchao.second_hand_trading_platform_front.service.impl;

import fit.wenchao.second_hand_trading_platform_front.dao.repo.StoreDao;
import fit.wenchao.second_hand_trading_platform_front.service.StoreService;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * <p>
 * Store 服务类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
public class StoreServiceImpl implements StoreService{

    @Autowired
    StoreDao storeDao;

}


