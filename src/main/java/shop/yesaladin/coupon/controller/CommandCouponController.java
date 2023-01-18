package shop.yesaladin.coupon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.coupon.dto.AmountCouponRequestDto;
import shop.yesaladin.coupon.dto.CouponResponseDto;
import shop.yesaladin.coupon.dto.PointCouponRequestDto;
import shop.yesaladin.coupon.dto.RateCouponRequestDto;
import shop.yesaladin.coupon.service.inter.CommandCouponService;

/**
 * 쿠폰 생성을 위한 RestController 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/coupons")
public class CommandCouponController {

    private final CommandCouponService commandCouponService;

    @PostMapping(params = {"point"})
    @ResponseStatus(HttpStatus.CREATED)
    public CouponResponseDto createPointCoupon(PointCouponRequestDto couponRequestDto) {
        return commandCouponService.createPointCoupon(couponRequestDto);
    }

    @PostMapping(params = {"amount"})
    @ResponseStatus(HttpStatus.CREATED)
    public CouponResponseDto createAmountCoupon(AmountCouponRequestDto couponRequestDto) {
        return commandCouponService.createAmountCoupon(couponRequestDto);
    }

    @PostMapping(params = {"rate"})
    @ResponseStatus(HttpStatus.CREATED)
    public CouponResponseDto createRateCoupon(RateCouponRequestDto couponRequestDto) {
        return commandCouponService.createRateCoupon(couponRequestDto);
    }
}