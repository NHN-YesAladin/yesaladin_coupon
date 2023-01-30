package shop.yesaladin.coupon.coupon.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.coupon.coupon.dto.CouponIssueRequestDto;
import shop.yesaladin.coupon.coupon.dto.CouponIssueResponseDto;
import shop.yesaladin.coupon.coupon.service.inter.CommandIssueCouponService;

/**
 * 쿠폰 발급 관련 Command 요청을 처리하는 Controller 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/issuances")
public class CommandIssuedCouponController {

    private final CommandIssueCouponService issuanceCommandService;

    /**
     * 쿠폰을 발행합니다.
     *
     * @param dto 쿠폰 발행 정보를 담은 dto
     * @return 발행된 쿠폰 번호가 담긴 DTO
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<List<CouponIssueResponseDto>> issueCoupon(@Valid @RequestBody CouponIssueRequestDto dto) {
        List<CouponIssueResponseDto> body = issuanceCommandService.issueCoupon(dto);
        return ResponseDto.<List<CouponIssueResponseDto>>builder()
                .status(HttpStatus.CREATED)
                .data(body)
                .success(true)
                .build();
    }
}
