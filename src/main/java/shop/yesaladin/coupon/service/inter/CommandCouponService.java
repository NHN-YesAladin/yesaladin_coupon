package shop.yesaladin.coupon.service.inter;

import shop.yesaladin.coupon.dto.AmountCouponRequestDto;
import shop.yesaladin.coupon.dto.CouponResponseDto;
import shop.yesaladin.coupon.dto.PointCouponRequestDto;
import shop.yesaladin.coupon.dto.RateCouponRequestDto;

public interface CommandCouponService {

    CouponResponseDto createPointCoupon(PointCouponRequestDto couponRequestDto);

    CouponResponseDto createAmountCoupon(AmountCouponRequestDto couponRequestDto);

    CouponResponseDto createRateCoupon(RateCouponRequestDto couponRequestDto);
}
