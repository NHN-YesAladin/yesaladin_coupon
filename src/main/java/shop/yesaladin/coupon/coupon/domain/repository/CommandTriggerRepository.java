package shop.yesaladin.coupon.coupon.domain.repository;

import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.Trigger;

/**
 * 특정 쿠폰 발급을 나타내는 트리거를 생성, 삭제하는 Repository 인터페이스입니다.
 *
 * @author 서민지
 * @since 1.0
 */
public interface CommandTriggerRepository {

    /**
     * 쿠폰 발급 트리거를 생성합니다.
     *
     * @param trigger 생성할 트리거
     * @return 생성된 트리거
     */
    Trigger save(Trigger trigger);

    /**
     * 트리거 타입 코드와 쿠폰 아이디에 해당하는 트리거를 삭제합니다.
     *
     * @param triggerTypeCode 삭제할 트리거의 트리거 타입 코드
     * @param couponId        삭제할 트리거의 쿠폰 아이디
     */
    void deleteByTriggerTypeCodeAndCoupon_Id(
            TriggerTypeCode triggerTypeCode,
            long couponId
    );
}
