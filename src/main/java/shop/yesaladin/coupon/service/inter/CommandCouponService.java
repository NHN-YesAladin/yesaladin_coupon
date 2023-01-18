package shop.yesaladin.coupon.service.inter;

import javax.transaction.Transactional;
import shop.yesaladin.coupon.dto.CouponRequestDto;
import shop.yesaladin.coupon.dto.CouponResponseDto;

public interface CommandCouponService {

    @Transactional
    CouponResponseDto createCoupon(CouponRequestDto couponRequestDto);
}
