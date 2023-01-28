package shop.yesaladin.coupon.validator.validator;

import java.util.Arrays;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import shop.yesaladin.coupon.validator.annotation.EnumValue;

public class EnumValidator implements ConstraintValidator<EnumValue, String> {

    private EnumValue annotation;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(s)) {
            return true;
        }

        Enum<?>[] values = this.annotation.enumClass().getEnumConstants();
        if (Objects.isNull(values)) {
            return false;
        }

        return Arrays.stream(values)
                .map(value -> annotation.ignoreCase() ? value.toString().toUpperCase()
                        : value.toString())
                .anyMatch(enumName -> enumName.equals(s));
    }
}
