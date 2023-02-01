package shop.yesaladin.coupon.coupon.persistence.converter;

import java.util.Arrays;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import shop.yesaladin.coupon.coupon.domain.model.CouponUsageStateCode;

/**
 * 쿠폰 사용 상태 코드 변환을 위한 컨버터입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Converter
public class CouponUsageStateCodeConverter implements
        AttributeConverter<CouponUsageStateCode, Integer> {

    /**
     * 쿠폰 사용 상태 코드를 Integer 타입으로 변환합니다.
     *
     * @param couponUsageStateCode 쿠폰 사용 상태 코드
     * @return 쿠폰 사용 상태 코드 Id
     */
    @Override
    public Integer convertToDatabaseColumn(CouponUsageStateCode couponUsageStateCode) {
        return couponUsageStateCode.getCode();
    }

    /**
     * 쿠폰 범위 코드의 Id 를 CouponUsageStateCode 타입으로 변환합니다.
     *
     * @param integer 쿠폰 사용 상태 코드의 Id
     * @return 쿠폰 사용 상태 코드
     */
    @Override
    public CouponUsageStateCode convertToEntityAttribute(Integer integer) {
        return Arrays.stream(CouponUsageStateCode.values())
                .filter(code -> integer.equals(code.getCode()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}