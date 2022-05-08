package fit.wenchao.second_hand_trading_platform_front.dao.repo.impl;

import fit.wenchao.second_hand_trading_platform_front.dao.po.StoreLevelPO;
import fit.wenchao.second_hand_trading_platform_front.dao.mapper.StoreLevelMapper;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.StoreLevelDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Repository;

/**
 * <p>
 * 商家等级，不同等级有不同费率，费率可以由管理员设置 dao实现类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
@Repository
public class StoreLevelDaoImpl extends ServiceImpl<StoreLevelMapper, StoreLevelPO> implements StoreLevelDao {

}
