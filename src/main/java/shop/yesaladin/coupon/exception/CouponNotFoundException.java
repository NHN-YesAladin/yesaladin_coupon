package shop.yesaladin.coupon.exception;

public class CouponNotFoundException extends RuntimeException {

    public CouponNotFoundException(long couponId) {
        super("Coupon Not Found. Request coupon id: " + couponId);
    }
}
