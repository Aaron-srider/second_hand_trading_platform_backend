package fit.wenchao.second_hand_trading_platform_front.dao.repo.impl;

import fit.wenchao.second_hand_trading_platform_front.dao.po.GoodsPO;
import fit.wenchao.second_hand_trading_platform_front.dao.mapper.GoodsMapper;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.GoodsDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Repository;

/**
 * <p>
 * 商品表 dao实现类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
@Repository
public class GoodsDaoImpl extends ServiceImpl<GoodsMapper, GoodsPO> implements GoodsDao {

}
