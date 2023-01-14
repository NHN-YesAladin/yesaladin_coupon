package shop.yesaladin.coupon.persistence.converter;

import java.util.Arrays;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import shop.yesaladin.coupon.domain.model.CouponCode;

/**
 * 쿠폰 코드 변환을 위한 컨버터입니다.드
 *
 * @author 서민지
 * @since 1.0
 */
@Converter
public class CouponCodeConverter implements AttributeConverter<CouponCode, Integer> {

    /**
     * 쿠폰 코드를 Integer 타입으로 변환합니다.
     *
     * @param couponCode 쿠폰 코드
     * @return 쿠폰 코드 Id
     */
    @Override
    public Integer convertToDatabaseColumn(CouponCode couponCode) {
        return couponCode.getCode();
    }

    /**
     * 쿠폰 코드의 Id 를 CouponCode 타입으로 변환합니다.
     *
     * @param integer 쿠폰 코드의 Id
     * @return 쿠폰 코드
     */
    @Override
    public CouponCode convertToEntityAttribute(Integer integer) {
        return Arrays.stream(CouponCode.values())
                .filter(code -> integer.equals(code.getCode()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}