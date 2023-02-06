package shop.yesaladin.coupon.coupon.domain.repository;

import shop.yesaladin.coupon.coupon.domain.model.CouponGroup;

/**
 * 쿠폰 그룹을 생성하는 Repository 인터페이스입니다.
 *
 * @author 서민지
 * @since 1.0
 */
public interface CommandCouponGroupRepository {

    /**
     * 쿠폰 그룹을 생성합니다.
     *
     * @param couponGroup 생성할 쿠폰 그룹
     * @return 생성된 쿠폰 그룹
     */
    CouponGroup save(CouponGroup couponGroup);
}
