package fit.wenchao.second_hand_trading_platform_front.utils.dataValidate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class PermissionValueValidator implements ConstraintValidator<Permission, Byte> {
    private final List<Byte> validValues = Arrays.asList(new Byte[]{-1, 0, 1});

    @Override
    public boolean isValid(Byte value, ConstraintValidatorContext context) {
        return validValues.contains(value);
    }
}