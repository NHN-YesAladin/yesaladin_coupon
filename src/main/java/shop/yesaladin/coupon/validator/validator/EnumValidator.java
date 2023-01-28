package shop.yesaladin.coupon.validator.validator;

import java.util.Arrays;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import shop.yesaladin.coupon.validator.annotation.EnumValue;

/**
 * enum 타입의 Bean Validation 을 위한 Validator 입니다. String 으로 들어온 값이 enum 클래스에 존재하는지 확인합니다. null 값이 들어온
 * 경우 유효성 검증을 실행하지 않고 유효한 값으로 판단합니다.
 *
 * @author 김홍대
 * @since 1.0
 */
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
