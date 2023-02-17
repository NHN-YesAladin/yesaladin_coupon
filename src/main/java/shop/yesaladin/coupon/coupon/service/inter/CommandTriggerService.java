package shop.yesaladin.coupon.coupon.service.inter;

import shop.yesaladin.coupon.code.TriggerTypeCode;

/**
 * 쿠폰 발행 트리거와 관련된 서비스 interface 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
public interface CommandTriggerService {

    /**
     * 트리거 타입 코드와 쿠폰 아이디로 찾은 쿠폰의 발행을 중단합니다.
     *
     * @param triggerTypeCode 트리거 타입 코드
     * @param couponId        발행을 중단할 쿠폰 아이디
     */
    void stopIssueCoupon(TriggerTypeCode triggerTypeCode, long couponId);
}
