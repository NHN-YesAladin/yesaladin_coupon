package shop.yesaladin.coupon.coupon.service.inter;

import shop.yesaladin.coupon.coupon.dto.CouponBoundResponseDto;

/**
 * 쿠폰 범위 관련 데이터를 조회하는 서비스 인터페이스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
public interface QueryCouponBoundService {

    /**
     * 쿠폰 ID로 쿠폰 적용 범위 정보를 조회합니다.
     *
     * @param couponId 조회할 쿠폰의 id
     * @return 쿠폰 적용 범위 정보
     */
    CouponBoundResponseDto getCouponBoundByCouponId(long couponId);
}