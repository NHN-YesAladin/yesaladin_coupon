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
import shop.yesaladin.coupon.service.inter.CommandCouponIssuanceService;

/**
 * 쿠폰 발급 관련 Command 요청을 처리하는 Controller 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/issuances")
public class CommandCouponIssuanceController {

    private final CommandCouponIssuanceService issuanceCommandService;

    /**
     * 쿠폰을 발행합니다.
     *
     * @param dto 쿠폰 발행 정보를 담은 dto
     * @return 발행된 쿠폰 번호가 담긴 DTO
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CouponIssuanceResponseDto issueCoupon(@Valid @RequestBody CouponIssuanceRequestDto dto) {
        return issuanceCommandService.issueCoupon(dto);
    }
}
