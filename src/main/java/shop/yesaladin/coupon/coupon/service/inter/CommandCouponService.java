package shop.yesaladin.coupon.coupon.service.inter;

import shop.yesaladin.coupon.coupon.dto.AmountCouponRequestDto;
import shop.yesaladin.coupon.coupon.dto.CouponResponseDto;
import shop.yesaladin.coupon.coupon.dto.PointCouponRequestDto;
import shop.yesaladin.coupon.coupon.dto.RateCouponRequestDto;

/**
 * 쿠폰과 관련된 CUD 작업을 하는 서비스 인터페이스입니다.
 *
 * @author 서민지
 * @since 1.0
 */
public interface CommandCouponService {

    /**
     * 포인트 쿠폰 생성 요청으로 포인트 쿠폰을 생성합니다. 쿠폰, 포인트 쿠폰, 쿠폰 적용 범위, 트리거 테이블에 레코드가 삽입됩니다.
     *
     * @param couponRequestDto 쿠폰 생성 요청 정보를 담은 dto
     * @return 생성된 쿠폰 정보를 담은 dto
     */
    CouponResponseDto createPointCoupon(PointCouponRequestDto couponRequestDto);

    /**
     * 정액할인 쿠폰 생성 요청으로 포인트 쿠폰을 생성합니다. 쿠폰, 정액할인 쿠폰, 쿠폰 적용 범위, 트리거 테이블에 레코드가 삽입됩니다.
     *
     * @param couponRequestDto 쿠폰 생성 요청 정보를 담은 dto
     * @return 생성된 쿠폰 정보를 담은 dto
     */
    CouponResponseDto createAmountCoupon(AmountCouponRequestDto couponRequestDto);

    /**
     * 정율할인 쿠폰 생성 요청으로 포인트 쿠폰을 생성합니다. 쿠폰, 정율할인 쿠폰, 쿠폰 적용 범위, 트리거 테이블에 레코드가 삽입됩니다.
     *
     * @param couponRequestDto 쿠폰 생성 요청 정보를 담은 dto
     * @return 생성된 쿠폰 정보를 담은 dto
     */
    CouponResponseDto createRateCoupon(RateCouponRequestDto couponRequestDto);
}
