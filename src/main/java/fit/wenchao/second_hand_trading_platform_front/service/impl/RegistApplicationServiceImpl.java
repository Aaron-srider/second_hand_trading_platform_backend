package fit.wenchao.second_hand_trading_platform_front.service.impl;

import fit.wenchao.second_hand_trading_platform_front.controller.BackendException;
import fit.wenchao.second_hand_trading_platform_front.dao.po.RegistApplicationPO;
import fit.wenchao.second_hand_trading_platform_front.dao.po.UserPO;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.RegistApplicationDao;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.UserDao;
import fit.wenchao.second_hand_trading_platform_front.service.RegistApplicationService;

import fit.wenchao.second_hand_trading_platform_front.utils.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        userPO.setRoleId(1);

        validate(userPO, (violat, propertyName, msg) -> {
            throw new BackendException(ResultCodeEnum.UNKNOWN_ERROR, propertyName + ": " +
                    msg);
        }, UserPO.RegisterNew.class);

        log.info("注册用户: {}", userPO);

        userDao.save(userPO);
    }
}


