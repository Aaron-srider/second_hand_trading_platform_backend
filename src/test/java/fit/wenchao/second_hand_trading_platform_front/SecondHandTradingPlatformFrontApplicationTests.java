package fit.wenchao.second_hand_trading_platform_front;

import fit.wenchao.second_hand_trading_platform_front.dao.po.UserRolePO;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.RoleDao;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.UserDao;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.UserRoleDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class SecondHandTradingPlatformFrontApplicationTests {

    @Autowired
    UserDao userDao;

    @Autowired
    UserRoleDao userRoleDao;

    @Autowired
    RoleDao roleDao;
    @Test
    void contextLoads() {
        List<UserRolePO> collect = userDao.list().stream().map((userPO -> {
            UserRolePO userRolePO = new UserRolePO();
            userRolePO.roleId = userPO.roleId;
            userRolePO.userId = userPO.id;
            return userRolePO;
        })).collect(Collectors.toList());

        userRoleDao.saveBatch(collect);
    }

}
