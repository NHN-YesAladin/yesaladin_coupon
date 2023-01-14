package shop.yesaladin.coupon.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponEventCode {
    SIGN_UP(1),
    BIRTHDAY(2),
    COUPON_OF_THE_MONTH(3);

    private final int code;
}