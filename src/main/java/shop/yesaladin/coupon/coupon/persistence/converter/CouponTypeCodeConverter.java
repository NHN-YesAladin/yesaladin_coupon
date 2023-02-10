package shop.yesaladin.coupon.coupon.persistence.converter;

import java.util.Arrays;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import shop.yesaladin.coupon.code.CouponTypeCode;

/**
 * 쿠폰 코드 변환을 위한 컨버터입니다.드
 *
 * @author 서민지
 * @since 1.0
 */
@Converter
public class CouponTypeCodeConverter implements AttributeConverter<CouponTypeCode, Integer> {

    /**
     * 쿠폰 코드를 Integer 타입으로 변환합니다.
     *
     * @param couponTypeCode 쿠폰 코드
     * @return 쿠폰 코드 Id
     */
    @Override
    public Integer convertToDatabaseColumn(CouponTypeCode couponTypeCode) {
        return couponTypeCode.getCode();
    }

    /**
     * 쿠폰 코드의 Id 를 CouponCode 타입으로 변환합니다.
     *
     * @param integer 쿠폰 코드의 Id
     * @return 쿠폰 코드
     */
    @Override
    public CouponTypeCode convertToEntityAttribute(Integer integer) {
        return Arrays.stream(CouponTypeCode.values())
                .filter(code -> integer.equals(code.getCode()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}