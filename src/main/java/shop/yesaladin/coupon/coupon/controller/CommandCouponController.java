package shop.yesaladin.coupon.coupon.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.coupon.coupon.dto.AmountCouponRequestDto;
import shop.yesaladin.coupon.coupon.dto.CouponResponseDto;
import shop.yesaladin.coupon.coupon.dto.PointCouponRequestDto;
import shop.yesaladin.coupon.coupon.dto.RateCouponRequestDto;
import shop.yesaladin.coupon.coupon.service.inter.CommandCouponService;

/**
 * 쿠폰 생성 Command 요청을 처리하는 RestController 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/coupons")
public class CommandCouponController {

    private final CommandCouponService commandCouponService;

    /**
     * 포인트 쿠폰을 생성합니다.
     *
     * @param couponRequestDto 생성할 쿠폰 정보를 담은 Dto
     * @return 생성된 쿠폰의 정보를 담은 Dto
     */
    @PostMapping(params = {"point"})
    @ResponseStatus(HttpStatus.CREATED)
    public CouponResponseDto createPointCoupon(@Valid @ModelAttribute PointCouponRequestDto couponRequestDto) {
        return commandCouponService.createPointCoupon(couponRequestDto);
    }

    /**
     * 정액할인 쿠폰을 생성합니다.
     *
     * @param couponRequestDto 생성할 쿠폰 정보를 담은 Dto
     * @return 생성된 쿠폰의 정보를 담은 Dto
     */
    @PostMapping(params = {"amount"})
    @ResponseStatus(HttpStatus.CREATED)
    public CouponResponseDto createAmountCoupon(@Valid @ModelAttribute AmountCouponRequestDto couponRequestDto) {
        return commandCouponService.createAmountCoupon(couponRequestDto);
    }

    /**
     * 정율할인 쿠폰을 생성합니다.
     *
     * @param couponRequestDto 생성할 쿠폰 정보를 담은 Dto
     * @return 생성된 쿠폰의 정보를 담은 Dto
     */
    @PostMapping(params = {"rate"})
    @ResponseStatus(HttpStatus.CREATED)
    public CouponResponseDto createRateCoupon(@Valid @ModelAttribute RateCouponRequestDto couponRequestDto) {
        return commandCouponService.createRateCoupon(couponRequestDto);
    }
}