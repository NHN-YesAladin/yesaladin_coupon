package shop.yesaladin.coupon.coupon.dummy;

import java.time.LocalDate;
import shop.yesaladin.coupon.code.CouponTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.AmountCoupon;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.PointCoupon;
import shop.yesaladin.coupon.coupon.domain.model.RateCoupon;

public class CouponDummy {

    public static Coupon dummyRateCoupon() {

        return RateCoupon.builder()
                .name("test rateCoupon")
                .isUnlimited(true)
                .expirationDate(LocalDate.of(2024, 12, 24))
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
                .name("test rateCoupon")
                .isUnlimited(isUnlimited)
                .couponTypeCode(CouponTypeCode.FIXED_RATE)
                .minOrderAmount(10000)
                .maxDiscountAmount(1000)
                .discountRate(10)
                .build();
    }

    public static Coupon dummyCouponWithCouponType(CouponTypeCode couponTypeCode) {
        switch (couponTypeCode) {
            case FIXED_RATE: {
                return RateCoupon.builder()
                        .name("test rateCoupon")
                        .isUnlimited(true)
                        .couponTypeCode(CouponTypeCode.FIXED_RATE)
                        .minOrderAmount(10000)
                        .maxDiscountAmount(1000)
                        .discountRate(10)
                        .build();
            }
            case FIXED_PRICE: {
                return AmountCoupon.builder()
                        .name("test amountCoupon")
                        .isUnlimited(true)
                        .couponTypeCode(CouponTypeCode.FIXED_PRICE)
                        .minOrderAmount(10000)
                        .discountAmount(500)
                        .canBeOverlapped(true)
                        .build();
            }
            case POINT: {
                return PointCoupon.builder()
                        .name("test pointCoupon")
                        .isUnlimited(true)
                        .couponTypeCode(CouponTypeCode.POINT)
                        .chargePointAmount(1000)
                        .build();
            }
            default:
                throw new IllegalArgumentException();
        }
    }
}
