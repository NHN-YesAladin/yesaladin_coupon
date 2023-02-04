package shop.yesaladin.coupon.coupon.domain.repository;

import shop.yesaladin.coupon.coupon.domain.model.Coupon;

/**
 * 쿠폰을 생성하는 Repository 인터페이스입니다.
 *
 * @author 서민지
 * @since 1.0
 */
public interface CommandCouponRepository {

    /**
     * 쿠폰을 생성합니다.
     *
     * @param coupon 생성할 쿠폰
     * @return 생성된 쿠폰
     */
    Coupon save(Coupon coupon);
}
