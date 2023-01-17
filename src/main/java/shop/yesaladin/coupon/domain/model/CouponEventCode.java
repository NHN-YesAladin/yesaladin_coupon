package shop.yesaladin.coupon.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 쿠폰 이벤트 코드 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public enum CouponEventCode {

    SIGN_UP(1), BIRTHDAY(2), COUPON_OF_THE_MONTH(3);

    private final int code;
}