package fit.wenchao.second_hand_trading_platform_front.dao.repo.impl;

import fit.wenchao.second_hand_trading_platform_front.dao.po.GoodsTypePO;
import fit.wenchao.second_hand_trading_platform_front.dao.mapper.GoodsTypeMapper;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.GoodsTypeDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Repository;

/**
 * <p>
 * 存储商品的类型 dao实现类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
@Repository
public class GoodsTypeDaoImpl extends ServiceImpl<GoodsTypeMapper, GoodsTypePO> implements GoodsTypeDao {

}
