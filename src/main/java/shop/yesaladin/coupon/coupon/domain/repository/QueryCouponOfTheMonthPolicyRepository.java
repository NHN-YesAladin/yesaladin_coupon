package shop.yesaladin.coupon.coupon.domain.repository;

import java.util.Optional;
import shop.yesaladin.coupon.coupon.domain.model.CouponOfTheMonthPolicy;

/**
 * 이달의 쿠폰 정책을 조회하는 Repository 인터페이스입니다.
 *
 * @author 서민지
 * @since 1.0
 */
public interface QueryCouponOfTheMonthPolicyRepository {

    /**
     * 가장 최근에 등록된 이달의 쿠폰 정책을 조회합니다.
     *
     * @return 가장 최근에 등록된 이달의 쿠폰 정책
     */
    Optional<CouponOfTheMonthPolicy> findLatestCouponOfTheMonthPolicy();
}
