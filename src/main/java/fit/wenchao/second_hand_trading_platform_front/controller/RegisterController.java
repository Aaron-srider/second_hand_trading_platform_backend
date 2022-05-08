package fit.wenchao.second_hand_trading_platform_front.controller;

import com.google.code.kaptcha.Constants;
import fit.wenchao.second_hand_trading_platform_front.utils.JsonResult;
import fit.wenchao.second_hand_trading_platform_front.utils.ResultCodeEnum;
import fit.wenchao.second_hand_trading_platform_front.utils.dataValidate.ValidatorUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Path;


import static fit.wenchao.second_hand_trading_platform_front.utils.dataValidate.ValidatorUtils.validate;

@RestController
@Slf4j
@CrossOrigin(allowCredentials = "true")
public class RegisterController {

    @Autowired
    RegisterService registerService;

    @PostMapping("/register")
    public JsonResult register(HttpServletRequest request, @RequestBody RegisterVo registerVo) throws Exception {

        validate(registerVo, (violat,propertyName, msg)  -> {
            throw new BackendException(ResultCodeEnum.FRONT_PARAM_ERROR,
                    propertyName + ": " + msg);
                }, RegisterVo.ReceiveFrontData.class
        );

        log.info("registerVo:{}", registerVo);
        RegisterDto registerDto = RegisterDto.builder().build();

        BeanUtils.copyProperties(registerVo, registerDto);

        registerService.register(registerDto);

        request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, null);

        return JsonResult.ok(null);

    }

}
