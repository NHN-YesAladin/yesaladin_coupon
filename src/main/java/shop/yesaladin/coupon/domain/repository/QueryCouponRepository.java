package shop.yesaladin.coupon.domain.repository;

import java.util.Optional;
import shop.yesaladin.coupon.domain.model.Coupon;

/**
 * 쿠폰 관련 데이터를 조회하기 위한 Repository 인터페이스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
public interface QueryCouponRepository {

    /**
     * 쿠폰의 PK로 쿠폰 정보를 가져오는 메서드입니다.
     *
     * @param couponId 정보를 가져올 쿠폰의 ID
     * @return 쿠폰의 정보(Optional)
     */

    Optional<Coupon> findCouponById(long couponId);
}
