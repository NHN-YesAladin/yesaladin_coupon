package shop.yesaladin.coupon.coupon.dummy;

import shop.yesaladin.coupon.code.CouponBoundCode;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.CouponBound;

public class CouponBoundDummy {

    public static CouponBound dummyCouponBoundProduct(Coupon coupon) {
        return CouponBound.builder().coupon(coupon).isbn("9791168473690")
                .couponBoundCode(CouponBoundCode.PRODUCT).build();
    }

}
