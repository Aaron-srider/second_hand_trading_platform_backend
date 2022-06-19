package fit.wenchao.second_hand_trading_platform_front.service.impl;

import fit.wenchao.second_hand_trading_platform_front.dao.repo.StoreEvaDao;
import fit.wenchao.second_hand_trading_platform_front.service.StoreEvaService;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * <p>
 * StoreEva 服务类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
public class StoreEvaServiceImpl implements StoreEvaService{

    @Autowired
    StoreEvaDao storeEvaDao;

}


