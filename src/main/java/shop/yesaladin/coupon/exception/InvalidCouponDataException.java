package shop.yesaladin.coupon.exception;

/**
 * 유효하지 않은 쿠폰 데이터로 발행을 요청했을 때 발생하는 예외 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
public class InvalidCouponDataException extends RuntimeException {

    public InvalidCouponDataException(long couponId) {
        super("Invalid coupon data. Request coupon id: " + couponId);
    }
}
