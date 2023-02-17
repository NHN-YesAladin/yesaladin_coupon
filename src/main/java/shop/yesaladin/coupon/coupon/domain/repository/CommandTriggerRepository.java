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

    void deleteByTriggerTypeCodeAndCoupon_Id(
            TriggerTypeCode triggerTypeCode,
            long couponId
    );
}
