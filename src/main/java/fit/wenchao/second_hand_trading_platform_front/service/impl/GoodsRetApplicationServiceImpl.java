package fit.wenchao.second_hand_trading_platform_front.service.impl;

import fit.wenchao.second_hand_trading_platform_front.dao.repo.GoodsRetApplicationDao;
import fit.wenchao.second_hand_trading_platform_front.service.GoodsRetApplicationService;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * <p>
 * GoodsRetApplication 服务类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
public class GoodsRetApplicationServiceImpl implements GoodsRetApplicationService{

    @Autowired
    GoodsRetApplicationDao goodsRetApplicationDao;

}


