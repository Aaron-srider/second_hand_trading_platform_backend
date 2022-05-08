package fit.wenchao.second_hand_trading_platform_front.utils.dataValidate;

import fit.wenchao.utils.function.ExceptionConsumer;
import fit.wenchao.utils.function.ExceptionTriConsumer;

import javax.validation.*;

import java.util.Set;

public class ValidatorUtils {
    public static boolean validate(Object obj, ExceptionConsumer<ConstraintViolation<Object>> failedCallback) throws Exception {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory();) {
            Validator validator = factory.getValidator();

            Set<ConstraintViolation<Object>> violationSet = validator.validate(obj);

            for (ConstraintViolation<Object> objectConstraintViolation : violationSet) {
                failedCallback.accept(objectConstraintViolation);
                return false;

            }
            return true;

        }
    }

    public static boolean validate(Object obj, ExceptionConsumer<ConstraintViolation<Object>> failedCallback, Class<?>... groups) throws Exception {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory();) {
            Validator validator = factory.getValidator();

            Set<ConstraintViolation<Object>> violationSet = validator.validate(obj, groups);

            for (ConstraintViolation<Object> objectConstraintViolation : violationSet) {
                failedCallback.accept(objectConstraintViolation);
                return false;
            }
            return true;
        }
    }

    public static boolean validate(
            Object obj,
            ExceptionTriConsumer<ConstraintViolation<Object>, String, String> failedCallback,
            Class<?>... groups) throws Exception {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory();) {
                Validator validator = factory.getValidator();

                Set<ConstraintViolation<Object>> violationSet = validator.validate(obj, groups);

                for (ConstraintViolation<Object> objectConstraintViolation : violationSet) {
                String propertyName = objectConstraintViolation.getPropertyPath().toString();
                String message = objectConstraintViolation.getConstraintDescriptor().getMessageTemplate();
                failedCallback.accept(objectConstraintViolation, propertyName, message);
                return false;

            }
            return true;
        }
    }


    public static boolean validate(
            Object obj,
            ExceptionTriConsumer<ConstraintViolation<Object>, String, String> failedCallback) throws Exception {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory();) {
            Validator validator = factory.getValidator();

            Set<ConstraintViolation<Object>> violationSet = validator.validate(obj);

            for (ConstraintViolation<Object> objectConstraintViolation : violationSet) {
                String propertyName = objectConstraintViolation.getPropertyPath().toString();
                String message = objectConstraintViolation.getConstraintDescriptor().getMessageTemplate();
                failedCallback.accept(objectConstraintViolation, propertyName, message);
                return false;

            }
            return true;
        }
    }
}
