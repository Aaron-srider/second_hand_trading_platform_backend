package fit.wenchao.second_hand_trading_platform_front.dao.repo.impl;

import fit.wenchao.second_hand_trading_platform_front.dao.po.RolePO;
import fit.wenchao.second_hand_trading_platform_front.dao.mapper.RoleMapper;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.RoleDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户角色
 dao实现类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
@Repository
public class RoleDaoImpl extends ServiceImpl<RoleMapper, RolePO> implements RoleDao {

}
