package shop.yesaladin.coupon.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.coupon.dto.CouponIssuanceRequestDto;
import shop.yesaladin.coupon.dto.CouponIssuanceResponseDto;
import shop.yesaladin.coupon.service.inter.CouponIssuanceCommandService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/coupons/issuances")
public class CouponIssuanceCommandController {

    private final CouponIssuanceCommandService issuanceCommandService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CouponIssuanceResponseDto issueCoupon(@Valid @RequestBody CouponIssuanceRequestDto dto) {
        return issuanceCommandService.issueCoupon(dto);
    }
}
