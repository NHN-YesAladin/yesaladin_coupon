package shop.yesaladin.coupon.coupon.exception;

import shop.yesaladin.coupon.trigger.TriggerTypeCode;

/**
 * 쿠폰 그룹 데이터가 존재하지 않을 시 발생하는 예외 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
public class CouponGroupNotFoundException extends RuntimeException {

    public CouponGroupNotFoundException(TriggerTypeCode triggerTypeCode, Long couponId) {
        super("Coupon group data not found. trigger : " + triggerTypeCode
                + " coupon : " + couponId);
    }
}
