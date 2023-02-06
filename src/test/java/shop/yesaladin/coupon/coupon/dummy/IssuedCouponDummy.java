package shop.yesaladin.coupon.coupon.dummy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import shop.yesaladin.coupon.coupon.domain.model.CouponGivenStateCode;
import shop.yesaladin.coupon.coupon.domain.model.CouponGroup;
import shop.yesaladin.coupon.coupon.domain.model.CouponUsageStateCode;
import shop.yesaladin.coupon.coupon.domain.model.IssuedCoupon;

public class IssuedCouponDummy {

    public static IssuedCoupon dummyIssuedCoupon(String couponCode, CouponGroup couponGroup) {
        return IssuedCoupon.builder()
                .couponCode(couponCode)
                .createdDatetime(LocalDateTime.now())
                .expirationDate(
                        LocalDate.of(2024, 12, 30))
                .couponGroup(couponGroup)
                .couponGivenStateCode(
                        CouponGivenStateCode.NOT_GIVEN)
                .couponUsageStateCode(CouponUsageStateCode.NOT_USED)
                .build();
    }

}
