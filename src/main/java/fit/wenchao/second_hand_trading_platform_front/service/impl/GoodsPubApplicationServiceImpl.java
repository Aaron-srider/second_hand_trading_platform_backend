package fit.wenchao.second_hand_trading_platform_front.service.impl;

import fit.wenchao.second_hand_trading_platform_front.dao.repo.GoodsPubApplicationDao;
import fit.wenchao.second_hand_trading_platform_front.service.GoodsPubApplicationService;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * <p>
 * GoodsPubApplication 服务类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
public class GoodsPubApplicationServiceImpl implements GoodsPubApplicationService{

    @Autowired
    GoodsPubApplicationDao goodsPubApplicationDao;

}


