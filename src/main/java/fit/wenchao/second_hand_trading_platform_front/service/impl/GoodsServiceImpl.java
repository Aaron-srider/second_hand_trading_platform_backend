package fit.wenchao.second_hand_trading_platform_front.service.impl;

import fit.wenchao.second_hand_trading_platform_front.dao.repo.GoodsDao;
import fit.wenchao.second_hand_trading_platform_front.service.GoodsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>
 * Goods 服务类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
public class GoodsServiceImpl implements GoodsService{

    @Autowired
    GoodsDao goodsDao;

}


