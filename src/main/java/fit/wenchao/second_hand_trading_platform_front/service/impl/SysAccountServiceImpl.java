package fit.wenchao.second_hand_trading_platform_front.service.impl;

import fit.wenchao.second_hand_trading_platform_front.dao.repo.SysAccountDao;
import fit.wenchao.second_hand_trading_platform_front.service.SysAccountService;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * <p>
 * SysAccount 服务类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
public class SysAccountServiceImpl implements SysAccountService{

    @Autowired
    SysAccountDao sysAccountDao;

}


