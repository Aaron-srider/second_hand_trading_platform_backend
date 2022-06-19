package fit.wenchao.second_hand_trading_platform_front.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fit.wenchao.second_hand_trading_platform_front.dao.po.RegistApplicationPO;
import fit.wenchao.second_hand_trading_platform_front.dao.po.UserPO;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.RegistApplicationDao;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.UserDao;
import fit.wenchao.second_hand_trading_platform_front.utils.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import static fit.wenchao.second_hand_trading_platform_front.utils.dataValidate.ValidatorUtils.validate;
import static fit.wenchao.second_hand_trading_platform_front.utils.date.DateUtils.getCurrentDateString;

@Service
@Slf4j
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    UserDao userDao;

    @Autowired
    RegistApplicationDao registApplicationDao;

    @Override
    public void register(RegisterDto registerDto) throws Exception {

        log.info("registerDto: {}", registerDto);

        String phone = registerDto.getEmail();

        QueryWrapper<RegistApplicationPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", phone);
        RegistApplicationPO one = registApplicationDao.getOne(queryWrapper);

        QueryWrapper<UserPO> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("email", phone);
        UserPO one1 = userDao.getOne(queryWrapper1);

        if (one != null || one1!=null) {
            throw new BackendException(ResultCodeEnum.REPEAT_REGISTRATION, "邮箱已注册");
        }

        RegistApplicationPO registApplicationPO = RegistApplicationPO.builder().build();
        BeanUtils.copyProperties(registerDto, registApplicationPO);
        registApplicationPO.setId(null);
        registApplicationPO.setBankNo(registerDto.getBankAccount());

        String sexStr = registerDto.getSex();
        if(sexStr==null || (!sexStr.equals("1")&&!sexStr.equals("0"))) {
            throw new BackendException(ResultCodeEnum.FRONT_PARAM_ERROR,"性别填写错误");
        }

        registApplicationPO.setSex(Integer.valueOf(sexStr));

        log.info("registApplicationPO: {}", registApplicationPO);
        registApplicationPO.setPermission((byte) -1);

        registApplicationPO.setApprovalTime(getCurrentDateString());

        validate(registApplicationPO, (violat, propertyName, msg) -> {
            BackendException backendException = new BackendException(
                    ResultCodeEnum.UNKNOWN_ERROR, propertyName + ": " + msg);
            throw backendException;
        }, RegistApplicationPO.InsertNewApplication.class);

        registApplicationDao.save(registApplicationPO);

    }
}
