package fit.wenchao.second_hand_trading_platform_front.service.impl;

import fit.wenchao.second_hand_trading_platform_front.dao.repo.StoreLevelDao;
import fit.wenchao.second_hand_trading_platform_front.service.StoreLevelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>
 * StoreLevel 服务类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
public class StoreLevelServiceImpl implements StoreLevelService{

    @Autowired
    StoreLevelDao storeLevelDao;

}


