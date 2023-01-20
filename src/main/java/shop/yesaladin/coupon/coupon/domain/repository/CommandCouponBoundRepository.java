package shop.yesaladin.coupon.coupon.domain.repository;

import shop.yesaladin.coupon.coupon.domain.model.CouponBound;

/**
 * 쿠폰의 적용 범위를 생성하는 Repository 인터페이스입니다.
 *
 * @author 서민지
 * @since 1.0
 */
public interface CommandCouponBoundRepository {

    /**
     * 쿠폰의 적용 범위(전제/특정 카테고리/특정 상품)를 생성합니다.
     *
     * @param couponBound 생성할 쿠폰 적용 범위
     * @return 생성된 쿠폰 적용 범위
     */
    CouponBound save(CouponBound couponBound);

}
