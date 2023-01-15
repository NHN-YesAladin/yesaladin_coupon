package shop.yesaladin.coupon.exception;

public class InvalidCouponDataException extends RuntimeException {

    public InvalidCouponDataException(long couponId) {
        super("Invalid coupon data. Request coupon id: " + couponId);
    }
}
