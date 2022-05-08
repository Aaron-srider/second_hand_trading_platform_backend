package fit.wenchao.second_hand_trading_platform_front.dao.repo.impl;

import fit.wenchao.second_hand_trading_platform_front.dao.po.StoreRegistApplicationPO;
import fit.wenchao.second_hand_trading_platform_front.dao.mapper.StoreRegistApplicationMapper;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.StoreRegistApplicationDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户申请注册店铺时需要申请 dao实现类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
@Repository
public class StoreRegistApplicationDaoImpl extends ServiceImpl<StoreRegistApplicationMapper, StoreRegistApplicationPO> implements StoreRegistApplicationDao {

}
