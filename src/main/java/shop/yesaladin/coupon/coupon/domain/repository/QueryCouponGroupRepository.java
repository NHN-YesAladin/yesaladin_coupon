package shop.yesaladin.coupon.coupon.domain.repository;

import java.util.Optional;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.CouponGroup;

/**
 * 쿠폰 그룹 데이터 조회용 레포지토리 인터페이스입니다.
 *
 * @author 김홍대, 서민지
 * @since 1.0
 */
public interface QueryCouponGroupRepository {

    /**
     * 트리거 entity 를 받아 해당 정보로 쿠폰 그룹을 조회합니다.
     *
     * @param triggerTypeCode 조회할 트리거 타입 코드
     * @param couponId        조회할 쿠폰 id
     * @return 조회된 데이터 중 가장 최근에 생성된 데이터
     */
    Optional<CouponGroup> findCouponGroupByTriggerTypeAndCouponId(
            TriggerTypeCode triggerTypeCode,
            long couponId
    );

    /**
     * 트리거 타입 코드로 쿠폰 그룹을 조회합니다.
     *
     * @param triggerTypeCode 조회할 트리거 타입 코드
     * @return 조회 데이터 중 가장 최근에 생성된 쿠폰 그룹
     */
    Optional<CouponGroup> findCouponGroupByTriggerType(TriggerTypeCode triggerTypeCode);
}
