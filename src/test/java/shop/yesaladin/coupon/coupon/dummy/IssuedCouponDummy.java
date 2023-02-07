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
                .expirationDateTime(
                        LocalDate.of(2024, 12, 30).atStartOfDay())
                .couponGroup(couponGroup)
                .couponGivenStateCode(
                        CouponGivenStateCode.NOT_GIVEN)
                .couponUsageStateCode(CouponUsageStateCode.NOT_USED)
                .build();
    }

    public static IssuedCoupon dummyGivenIssuedCoupon(String couponCode, CouponGroup couponGroup) {
        return IssuedCoupon.builder()
                .couponCode(couponCode)
                .createdDatetime(LocalDateTime.now())
                .expirationDateTime(
                        LocalDate.of(2024, 12, 30).atStartOfDay())
                .couponGroup(couponGroup)
                .couponGivenStateCode(
                        CouponGivenStateCode.GIVEN)
                .givenDatetime(LocalDateTime.now().minusDays(1L))
                .couponUsageStateCode(CouponUsageStateCode.NOT_USED)
                .build();
    }

    public static IssuedCoupon dummyUsedIssuedCoupon(String couponCode, CouponGroup couponGroup) {
        return IssuedCoupon.builder()
                .couponCode(couponCode)
                .createdDatetime(LocalDateTime.now())
                .expirationDateTime(
                        LocalDate.of(2024, 12, 30).atStartOfDay())
                .couponGroup(couponGroup)
                .couponGivenStateCode(
                        CouponGivenStateCode.GIVEN)
                .givenDatetime(LocalDateTime.now().minusDays(1L))
                .usedDatetime(LocalDateTime.now().minusHours(5))
                .couponUsageStateCode(CouponUsageStateCode.USED)
                .build();
    }
}
