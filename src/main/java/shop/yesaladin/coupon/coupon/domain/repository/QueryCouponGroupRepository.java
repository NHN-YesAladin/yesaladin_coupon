package shop.yesaladin.coupon.coupon.domain.repository;

import java.util.Optional;
import shop.yesaladin.coupon.coupon.domain.model.CouponGroup;
import shop.yesaladin.coupon.coupon.domain.model.Trigger;

/**
 * 쿠폰 그룹 데이터 조회용 레포지토리 인터페이스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
public interface QueryCouponGroupRepository {

    /**
     * 트리거 entity를 받아 해당 정보로 쿠폰 그룹을 조회합니다.
     *
     * @param trigger 조회할 데이터를 담고 있는 triggerEntity
     * @return 조회된 데이터 중 가장 최근에 생성된 데이터
     */
    Optional<CouponGroup> findCouponGroupByTrigger(Trigger trigger);
}
