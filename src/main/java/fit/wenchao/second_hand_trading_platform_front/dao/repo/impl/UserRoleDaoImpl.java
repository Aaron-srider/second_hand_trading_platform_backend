package fit.wenchao.second_hand_trading_platform_front.dao.repo.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fit.wenchao.second_hand_trading_platform_front.dao.mapper.UserMapper;
import fit.wenchao.second_hand_trading_platform_front.dao.mapper.UserRoleMapper;
import fit.wenchao.second_hand_trading_platform_front.dao.po.UserPO;
import fit.wenchao.second_hand_trading_platform_front.dao.po.UserRolePO;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.UserDao;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.UserRoleDao;
import org.springframework.stereotype.Repository;

@Repository
public class UserRoleDaoImpl extends ServiceImpl<UserRoleMapper, UserRolePO> implements UserRoleDao {

}
