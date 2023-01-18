package shop.yesaladin.coupon.service.inter;

import shop.yesaladin.coupon.dto.CouponRequestDto;
import shop.yesaladin.coupon.dto.CouponResponseDto;
import shop.yesaladin.coupon.dto.PointCouponRequestDto;

public interface CommandCouponService {

    CouponResponseDto createCoupon(CouponRequestDto couponRequestDto);

    CouponResponseDto createPointCoupon(PointCouponRequestDto couponRequestDto);
}
