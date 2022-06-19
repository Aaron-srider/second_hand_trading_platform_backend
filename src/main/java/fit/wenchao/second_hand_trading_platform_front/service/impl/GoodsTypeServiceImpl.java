package fit.wenchao.second_hand_trading_platform_front.service.impl;

import fit.wenchao.second_hand_trading_platform_front.dao.repo.GoodsTypeDao;
import fit.wenchao.second_hand_trading_platform_front.service.GoodsTypeService;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * <p>
 * GoodsType 服务类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
public class GoodsTypeServiceImpl implements GoodsTypeService{

    @Autowired
    GoodsTypeDao goodsTypeDao;

}


