package fit.wenchao.second_hand_trading_platform_front.service.impl;

import fit.wenchao.second_hand_trading_platform_front.dao.repo.GoodsEvaDao;
import fit.wenchao.second_hand_trading_platform_front.service.GoodsEvaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>
 * GoodsEva 服务类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
@Service
public class GoodsEvaServiceImpl implements GoodsEvaService{

    @Autowired
    GoodsEvaDao goodsEvaDao;

}


