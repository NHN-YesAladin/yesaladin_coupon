package shop.yesaladin.coupon.coupon.exception;

import shop.yesaladin.coupon.trigger.TriggerTypeCode;

/**
 * 트리거를 찾을 수 없을 때 발생하는 예외 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
public class TriggerNotFoundException extends RuntimeException {

    public TriggerNotFoundException(TriggerTypeCode triggerTypeCode, Long couponId) {
        super("Trigger data not found. trigger : " + triggerTypeCode + " coupon : " + couponId);
    }
}
