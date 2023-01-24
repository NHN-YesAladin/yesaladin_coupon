package shop.yesaladin.coupon.coupon.exception;

/**
 * 쿠폰 정보를 찾을 수 없을 때 발생하는 예외 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
public class CouponNotFoundException extends RuntimeException {

    public CouponNotFoundException(long couponId) {
        super("Coupon Not Found. Request coupon id: " + couponId);
    }
}
