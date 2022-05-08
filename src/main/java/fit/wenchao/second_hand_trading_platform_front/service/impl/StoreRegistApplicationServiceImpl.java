package fit.wenchao.second_hand_trading_platform_front.service.impl;

import fit.wenchao.second_hand_trading_platform_front.dao.repo.StoreRegistApplicationDao;
import fit.wenchao.second_hand_trading_platform_front.service.StoreRegistApplicationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>
 * StoreRegistApplication 服务类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
public class StoreRegistApplicationServiceImpl implements StoreRegistApplicationService{

    @Autowired
    StoreRegistApplicationDao storeRegistApplicationDao;

}


