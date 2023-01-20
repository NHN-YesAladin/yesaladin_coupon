package shop.yesaladin.coupon.coupon.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 쿠폰 범위 코드 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public enum CouponBoundCode {

    ALL(1), CATEGORY(2), PRODUCT(3);

    private final int code;
}
