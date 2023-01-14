package shop.yesaladin.coupon.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 쿠폰 코드 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public enum CouponCode {

    FIXED_PRICE(1), FIXED_RATE(2), POINT(3),  // 쿠폰 종류 코드
    USER_DOWNLOAD(4), AUTO_ISSUANCE(5);       // 카드 코드

    private final int code;
}
