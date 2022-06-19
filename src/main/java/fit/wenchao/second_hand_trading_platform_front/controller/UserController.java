package fit.wenchao.second_hand_trading_platform_front.controller;

import fit.wenchao.second_hand_trading_platform_front.dao.po.RolePO;
import fit.wenchao.second_hand_trading_platform_front.dao.po.UserPO;
import fit.wenchao.second_hand_trading_platform_front.dao.po.UserRolePO;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.RoleDao;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.UserDao;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.UserRoleDao;
import fit.wenchao.second_hand_trading_platform_front.utils.JsonResult;
import fit.wenchao.second_hand_trading_platform_front.utils.MapFieldName;
import fit.wenchao.second_hand_trading_platform_front.utils.ResultCodeEnum;
import fit.wenchao.second_hand_trading_platform_front.utils.WrapperUtils;
import fit.wenchao.utils.optional.OptionalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static fit.wenchao.second_hand_trading_platform_front.utils.WrapperUtils.eq;

/**
 * <p>
 * 用户表
 * 前端控制器
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
@RestController
@Slf4j
@CrossOrigin(allowCredentials = "true")
public class UserController {

    @Autowired
    UserDao userDao;

    @GetMapping("/user")
    public JsonResult getUserById(Integer userId) {

        UserPO userPO = OptionalUtils.nullable(userId)
                .filter(Objects::nonNull)
                .map(userDao::getById)
                .orElseThrow(() -> new BackendException(ResultCodeEnum.FRONT_PARAM_ERROR, null));

        Map<String, Object> stringObjectMap = OptionalUtils.nullable(userPO)
                .map(MapFieldName::mapFieldName)
                .orElse(null);

        return JsonResult.ok(stringObjectMap);
    }

    @GetMapping("/fetchUserInfo")
    public JsonResult fetchUserInfo(Integer userId) {
        UserPO userPO = userDao.getById(userId);

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userPO, userVO);
        userVO.password = null;
        userVO.amount = null;
        userVO.credits = null;
        userVO.bankAccount  = null;

        //RolePO rolePO = roleDao.getById(userVO.roleId);
        //userVO.roleName = rolePO.name;
        //userVO.roleCode = rolePO.code;

        List<UserRolePO> userRolePOs = userRoleDao.list(eq("user_id", userPO.id));

        List<String> roleNames = userRolePOs.stream().map((userRolePO -> {
            Integer roleId = userRolePO.roleId;
            return OptionalUtils.nullable(roleId).map((roleIdValue) -> {
                return roleDao.getById(roleId).code;
            }).orElse(null);
        })).collect(Collectors.toList());
        userVO.roleNames = roleNames;

        List<String> roleNameTexts = userRolePOs.stream().map((userRolePO -> {
            Integer roleId = userRolePO.roleId;
            return OptionalUtils.nullable(roleId).map((roleIdValue) -> {
                return roleDao.getById(roleId).name;
            }).orElse(null);
        })).collect(Collectors.toList());
        userVO.roleNameTexts = roleNameTexts;

        if(userVO.sex == 1){
            userVO.sexText = "男";
        } else {
            userVO.sexText = "女";
        }
        return JsonResult.ok(userVO);
    }


    @Autowired
    RoleDao roleDao;

    @Autowired
    UserRoleDao userRoleDao;

    @GetMapping("/login")
    public JsonResult login(String name, String password) {
        log.info("user login request:{}-{}", name, password);
        UserPO userPO = userDao.getOne(WrapperUtils.<UserPO>eq("email", name).eq("password", password));
        OptionalUtils.nullable(userPO).orElseThrow(() -> {
            return new BackendException(ResultCodeEnum.USER_NOT_EXISTS, null);
        });

        log.info("user login:{}", userPO);

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userPO, userVO);
        userVO.password = null;
        userVO.amount = null;
        userVO.credits = null;
        userVO.bankAccount  = null;

        //RolePO rolePO = roleDao.getById(userVO.roleId);
        //userVO.roleName = rolePO.name;
        //userVO.roleCode = rolePO.code;

        List<UserRolePO> userRolePOs = userRoleDao.list(eq("user_id", userPO.id));

        List<String> roleNames = userRolePOs.stream().map((userRolePO -> {
            Integer roleId = userRolePO.roleId;
            return OptionalUtils.nullable(roleId).map((roleIdValue) -> {
                return roleDao.getById(roleId).code;
            }).orElse(null);
        })).collect(Collectors.toList());
        userVO.roleNames = roleNames;

        List<String> roleNameTexts = userRolePOs.stream().map((userRolePO -> {
            Integer roleId = userRolePO.roleId;
            return OptionalUtils.nullable(roleId).map((roleIdValue) -> {
                return roleDao.getById(roleId).name;
            }).orElse(null);
        })).collect(Collectors.toList());
        userVO.roleNameTexts = roleNameTexts;

        if(userVO.sex == 1){
            userVO.sexText = "男";
        } else {
            userVO.sexText = "女";
        }

        return JsonResult.ok(userVO);
    }

    public static class UserVO extends UserPO {
        @Deprecated
        public String roleName;

        public List<String> roleNameTexts;
        public List<String> roleNames;
        public String roleCode;
        public String sexText;


    }


}
