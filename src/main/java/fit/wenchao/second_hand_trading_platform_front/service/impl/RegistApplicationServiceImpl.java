package fit.wenchao.second_hand_trading_platform_front.service.impl;

import fit.wenchao.second_hand_trading_platform_front.controller.BackendException;
import fit.wenchao.second_hand_trading_platform_front.dao.po.RegistApplicationPO;
import fit.wenchao.second_hand_trading_platform_front.dao.po.UserPO;
import fit.wenchao.second_hand_trading_platform_front.dao.po.UserRolePO;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.RegistApplicationDao;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.UserDao;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.UserRoleDao;
import fit.wenchao.second_hand_trading_platform_front.service.RegistApplicationService;

import fit.wenchao.second_hand_trading_platform_front.utils.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static fit.wenchao.second_hand_trading_platform_front.utils.dataValidate.ValidatorUtils.validate;


/**
 * <p>
 * RegistApplication 服务类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
@Service
@Slf4j
public class RegistApplicationServiceImpl implements RegistApplicationService {

    @Autowired
    UserDao userDao;
    @Autowired
    RegistApplicationDao registApplicationDao;
    @Autowired
    UserRoleDao userRoleDao;

    @Override
    @Transactional
    public void pass(Integer registerApplicationId) throws Exception {

        log.info("registerApplicationId to be passed:{}", registerApplicationId);

        RegistApplicationPO build = RegistApplicationPO.builder().id(registerApplicationId)
                .permission((byte) 1).build();

        log.info("update registerApplicationPO:{}", build);

        registApplicationDao.updateById(build);

        RegistApplicationPO byId = registApplicationDao.getById(registerApplicationId);

        UserPO userPO = UserPO.builder().build();
        byId.setId(null);
        BeanUtils.copyProperties(byId, userPO);
        userPO.setBankAccount(byId.getBankNo());
        userPO.credits=100000;
        userPO.amount = new BigDecimal(100000);

        //deprecated, role control trans to user_role relation table.
        //userPO.setRoleId(1);


        //validate(userPO, (violat, propertyName, msg) -> {
        //    throw new BackendException(ResultCodeEnum.UNKNOWN_ERROR, propertyName + ": " +
        //            msg);
        //}, UserPO.RegisterNew.class);


        userDao.save(userPO);
        log.info("注册用户: {}", userPO);

        UserRolePO userRolePO = new UserRolePO();
        userRolePO.userId = userPO.id;
        userRolePO.roleId = 1;
        userRoleDao.save(userRolePO);
        log.info("add role:{} for user:{}",userRolePO, userPO);

    }
}


