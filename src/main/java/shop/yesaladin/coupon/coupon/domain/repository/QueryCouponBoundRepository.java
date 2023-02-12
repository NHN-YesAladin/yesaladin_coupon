package shop.yesaladin.coupon.coupon.domain.repository;

import java.util.Optional;
import shop.yesaladin.coupon.coupon.domain.model.CouponBound;

/**
 * 쿠폰 적용 범위 정보를 조회하는 레포지토리 인터페이스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
public interface QueryCouponBoundRepository {

    /**
     * 쿠폰 ID로 해당 쿠폰의 적용 범위 정보를 조회합니다.
     * @param couponId 조회할 쿠폰의 id
     * @return 조회된 쿠폰 적용범위
     */
    Optional<CouponBound> findCouponBoundByCouponId(long couponId);
}
