package fit.wenchao.second_hand_trading_platform_front.controller;

import fit.wenchao.second_hand_trading_platform_front.utils.JsonResult;
import fit.wenchao.second_hand_trading_platform_front.utils.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import java.util.regex.Pattern;

import static fit.wenchao.utils.collection.SimpleFactories.ofJson;
import static fit.wenchao.utils.optional.OptionalUtils.nullable;

/**
 * <p>
 * 商品表 前端控制器`
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
@RestController
@CrossOrigin(allowCredentials = "true")
@Slf4j
public class UtilsController {


    @GetMapping("/multiple")
    public JsonResult getGoodsPage(
            String a, String b, String scale
    ) {
        BigDecimal bigDecimal3 = nullable(a)
                .filter((valueA) -> {
                    boolean testWithPoint =
                            Pattern.matches("^-?\\d+.\\d+$", valueA);
                    boolean testWithOutPoint =
                            Pattern.matches("^-?\\d+$", valueA);
                    return testWithOutPoint || testWithPoint;
                })
                .map(BigDecimal::new).orElseThrow(() -> new BackendException(ResultCodeEnum.FRONT_PARAM_ERROR, null));

        BigDecimal bigDecimal4 = nullable(b)
                .filter((valueB) -> {
                    boolean testWithPoint =
                            Pattern.matches("^-?\\d+.\\d+$", valueB);
                    boolean testWithOutPoint =
                            Pattern.matches("^-?\\d+$", valueB);
                    return testWithOutPoint || testWithPoint;
                })
                .map(BigDecimal::new).orElseThrow(() -> new BackendException(ResultCodeEnum.FRONT_PARAM_ERROR, null));

        BigDecimal multiply = bigDecimal3.multiply(bigDecimal4);

        BigDecimal bigDecimal2 = nullable(scale)
                .filter((scaleValue) -> Pattern.matches("^\\d+$", scaleValue))
                .map(Integer::parseInt)
                .map((scaleValue) -> multiply.setScale(scaleValue, RoundingMode.DOWN))
                .orElse(multiply);


        return JsonResult.ok(ofJson("multiply", bigDecimal2));
    }


    @GetMapping("/uuid")
    public JsonResult getUUID() {
        return JsonResult.ok(UUID.randomUUID().toString());
    }

}
