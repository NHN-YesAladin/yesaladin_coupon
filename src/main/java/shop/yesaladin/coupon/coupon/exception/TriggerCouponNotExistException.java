package shop.yesaladin.coupon.coupon.exception;

import shop.yesaladin.coupon.trigger.TriggerTypeCode;

/**
 * 트리거로 쿠폰 발급을 시도할 때 해당 트리거에 매칭되는 쿠폰이 없을 때 발생하는 예외 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
public class TriggerCouponNotExistException extends RuntimeException {

    public TriggerCouponNotExistException(TriggerTypeCode triggerTypeCode) {
        super("Coupon not exist for trigger: " + triggerTypeCode);
    }
}
