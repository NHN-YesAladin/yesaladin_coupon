package shop.yesaladin.coupon.coupon.service.inter;

import shop.yesaladin.coupon.coupon.domain.model.CouponOfTheMonthPolicy;

/**
 * 이달의 쿠폰과 관련된 서비스 인터페이스입니다.
 *
 * @author 서민지
 * @since 1.0
 */
public interface CouponOfTheMonthService {

    /**
     * 가장 최근에 등록된 이달의 쿠폰 정책을 조회합니다.
     *
     * @return 가장 최근에 등록된 이달의 쿠폰 정책
     */
    CouponOfTheMonthPolicy getLatestPolicy();
}
