package shop.yesaladin.coupon.coupon.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 쿠폰 지급 상태 코드입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public enum CouponGivenStateCode {

    NOT_GIVEN(1, "미지급"), PENDING_GIVE(2, "지급 대기"), GIVEN(3, "지급 완료");

    private final int code;
    private final String displayName;
}
