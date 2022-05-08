package fit.wenchao.second_hand_trading_platform_front.service.impl;

import fit.wenchao.second_hand_trading_platform_front.dao.repo.UserDao;
import fit.wenchao.second_hand_trading_platform_front.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>
 * User 服务类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
public class UserServiceImpl implements UserService{

    @Autowired
    UserDao userDao;

}


