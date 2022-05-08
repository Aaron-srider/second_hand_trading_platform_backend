package fit.wenchao.second_hand_trading_platform_front.dao.repo.impl;

import fit.wenchao.second_hand_trading_platform_front.dao.po.UserPO;
import fit.wenchao.second_hand_trading_platform_front.dao.mapper.UserMapper;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.UserDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户表
 dao实现类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
@Repository
public class UserDaoImpl extends ServiceImpl<UserMapper, UserPO> implements UserDao {

}
