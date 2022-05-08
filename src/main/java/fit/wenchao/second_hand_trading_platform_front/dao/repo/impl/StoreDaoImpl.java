package fit.wenchao.second_hand_trading_platform_front.dao.repo.impl;

import fit.wenchao.second_hand_trading_platform_front.dao.po.StorePO;
import fit.wenchao.second_hand_trading_platform_front.dao.mapper.StoreMapper;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.StoreDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Repository;

/**
 * <p>
 *  dao实现类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
@Repository
public class StoreDaoImpl extends ServiceImpl<StoreMapper, StorePO> implements StoreDao {

}
