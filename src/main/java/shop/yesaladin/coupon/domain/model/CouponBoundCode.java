package shop.yesaladin.coupon.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponBoundCode {

    ALL(1),
    CATEGORY(2),
    PRODUCT(3);

    private final int code;
}
