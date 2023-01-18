package shop.yesaladin.coupon.persistence.converter;

import java.util.Arrays;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import shop.yesaladin.coupon.domain.model.TriggerTypeCode;

/**
 * 쿠폰 이벤트 코드 변환을 위한 컨버터입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Converter
public class TriggerTypeCodeCodeConverter implements AttributeConverter<TriggerTypeCode, Integer> {

    /**
     * 쿠폰 이벤트 코드를 Integer 타입으로 변환합니다.
     *
     * @param triggerTypeCode 쿠폰 이벤트 코드
     * @return 쿠폰 이벤트 코드 Id
     */
    @Override
    public Integer convertToDatabaseColumn(TriggerTypeCode triggerTypeCode) {
        return triggerTypeCode.getCode();
    }

    /**
     * 쿠폰 이벤트 코드의 Id 를 CouponEventCode 타입으로 변환합니다.
     *
     * @param integer 쿠폰 이벤트 코드의 Id
     * @return 쿠폰 이벤트 코드
     */
    @Override
    public TriggerTypeCode convertToEntityAttribute(Integer integer) {
        return Arrays.stream(TriggerTypeCode.values())
                .filter(code -> integer.equals(code.getCode()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}