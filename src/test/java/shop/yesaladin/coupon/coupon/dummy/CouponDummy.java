package shop.yesaladin.coupon.coupon.dummy;

import shop.yesaladin.coupon.code.CouponTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.RateCoupon;

public class CouponDummy {

    public static Coupon dummyRateCoupon() {
        return RateCoupon.builder()
                .id(100L)
                .name("test rateCoupon")
                .isUnlimited(true)
                .couponTypeCode(CouponTypeCode.FIXED_RATE)
                .minOrderAmount(10000)
                .maxDiscountAmount(1000)
                .discountRate(10)
                .build();
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
                .id(100L)
                .name("test rateCoupon")
                .isUnlimited(isUnlimited)
                .couponTypeCode(CouponTypeCode.FIXED_RATE)
                .minOrderAmount(10000)
                .maxDiscountAmount(1000)
                .discountRate(10)
                .build();
    }

}
