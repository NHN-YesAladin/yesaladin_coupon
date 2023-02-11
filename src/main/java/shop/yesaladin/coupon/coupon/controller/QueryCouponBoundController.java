package shop.yesaladin.coupon.coupon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.coupon.coupon.dto.CouponBoundResponseDto;
import shop.yesaladin.coupon.coupon.service.inter.QueryCouponBoundService;

/**
 * 쿠폰 적용 범위 관련 정보를 조회하는 컨트롤러 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/coupons/{couponId}/bounds")
public class QueryCouponBoundController {

    private final QueryCouponBoundService queryCouponBoundService;

    /**
     * 쿠폰 ID로 쿠폰의 적용 범위를 조회합니다.
     * @param couponId 조회할 쿠폰의 ID
     * @return 조회된 쿠폰의 적용 범위
     */
    @GetMapping
    public ResponseDto<CouponBoundResponseDto> couponBoundResponseDto(@PathVariable long couponId) {
        CouponBoundResponseDto response = queryCouponBoundService.getCouponBoundByCouponId(couponId);
        return ResponseDto.<CouponBoundResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }
}
