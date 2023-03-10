package shop.yesaladin.coupon.coupon.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import shop.yesaladin.coupon.coupon.domain.model.Trigger;

/**
 * 쿠폰과 쿠폰 그룹 코드를 가지는 DTO 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public class TriggerWithCouponGroupCodeDto {

    private final Trigger trigger;
    private final String couponGroupCode;

}
