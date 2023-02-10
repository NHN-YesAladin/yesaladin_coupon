package shop.yesaladin.coupon.coupon.domain.repository;

import shop.yesaladin.coupon.coupon.domain.model.CouponOfTheMonthPolicy;

/**
 * 이달의 쿠폰 정책을 생성하는 레포지토리 인터페이스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
public interface CommandCouponOfTheMonthPolicyRepository {

    CouponOfTheMonthPolicy save(CouponOfTheMonthPolicy policy);
}
