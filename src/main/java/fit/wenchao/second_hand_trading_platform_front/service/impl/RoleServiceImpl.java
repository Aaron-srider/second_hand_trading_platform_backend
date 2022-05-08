package fit.wenchao.second_hand_trading_platform_front.service.impl;

import fit.wenchao.second_hand_trading_platform_front.dao.repo.RoleDao;
import fit.wenchao.second_hand_trading_platform_front.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>
 * Role 服务类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
public class RoleServiceImpl implements RoleService{

    @Autowired
    RoleDao roleDao;

}


