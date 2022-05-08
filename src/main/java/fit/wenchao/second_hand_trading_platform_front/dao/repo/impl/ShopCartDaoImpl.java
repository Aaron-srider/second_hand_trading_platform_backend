package fit.wenchao.second_hand_trading_platform_front.dao.repo.impl;

import fit.wenchao.second_hand_trading_platform_front.dao.po.ShopCartPO;
import fit.wenchao.second_hand_trading_platform_front.dao.mapper.ShopCartMapper;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.ShopCartDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Repository;

/**
 * <p>
 * 存储用户的购物车内容 dao实现类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
@Repository
public class ShopCartDaoImpl extends ServiceImpl<ShopCartMapper, ShopCartPO> implements ShopCartDao {

}
