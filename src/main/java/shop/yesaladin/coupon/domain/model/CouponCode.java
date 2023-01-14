package shop.yesaladin.coupon.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponCode {
    FIXED_PRICE(1), FIXED_RATE(2), POINT(3),  // 쿠폰 종류 코드
    USER_DOWNLOAD(4), AUTO_ISSUANCE(5);       // 카드 코드

    private final int code;
}
