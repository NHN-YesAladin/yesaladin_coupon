package shop.yesaladin.coupon.persistence.converter;

import java.util.Arrays;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import shop.yesaladin.coupon.domain.model.CouponBoundCode;

/**
 * 쿠폰 범위 코드 변환을 위한 컨버터입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Converter
public class CouponBoundCodeConverter implements AttributeConverter<CouponBoundCode, Integer> {

    /**
     * 쿠폰 범위 코드를 Integer 타입으로 변환합니다.
     *
     * @param couponBoundCode 쿠폰 범위 코드
     * @return 쿠폰 범위 코드 Id
     */
    @Override
    public Integer convertToDatabaseColumn(CouponBoundCode couponBoundCode) {
        return couponBoundCode.getCode();
    }

    /**
     * 쿠폰 범위 코드의 Id 를 CouponBoundCode 타입으로 변환합니다.
     *
     * @param integer 쿠폰 범위 코드의 Id
     * @return 쿠폰 범위 코드
     */
    @Override
    public CouponBoundCode convertToEntityAttribute(Integer integer) {
        return Arrays.stream(CouponBoundCode.values())
                .filter(code -> integer.equals(code.getCode()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}