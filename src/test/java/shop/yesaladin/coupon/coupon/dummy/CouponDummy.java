package shop.yesaladin.coupon.coupon.dummy;

import java.time.LocalDate;
import shop.yesaladin.coupon.code.CouponTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.RateCoupon;

public class CouponDummy {

    public static Coupon dummyRateCoupon() {
        Coupon coupon = RateCoupon.builder()
                .name("test rateCoupon")
                .isUnlimited(true)
                .expirationDate(LocalDate.of(2024,12,24))
                .couponTypeCode(CouponTypeCode.FIXED_RATE)
                .minOrderAmount(10000)
                .maxDiscountAmount(1000)
                .discountRate(10)
                .build();

        return coupon;
    }

    public static Coupon dummyRateCouponWithId(long id) {
        return RateCoupon.builder()
                .id(id)
                .name("test rateCoupon")
                .isUnlimited(true)
                .couponTypeCode(CouponTypeCode.FIXED_RATE)
                .minOrderAmount(10000)
                .maxDiscountAmount(1000)
                .discountRate(10)
                .build();
    }
    public static Coupon dummyRateCouponWithUnlimited(boolean isUnlimited) {
        return RateCoupon.builder()
                .name("test rateCoupon")
                .isUnlimited(isUnlimited)
                .couponTypeCode(CouponTypeCode.FIXED_RATE)
                .minOrderAmount(10000)
                .maxDiscountAmount(1000)
                .discountRate(10)
                .build();
    }
}
