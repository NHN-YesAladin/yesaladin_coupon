package shop.yesaladin.coupon.coupon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.coupon.coupon.dto.CouponSummaryDto;
import shop.yesaladin.coupon.coupon.dto.PaginatedResponseDto;
import shop.yesaladin.coupon.coupon.service.inter.QueryCouponService;

/**
 * 쿠폰 조회 요청을 처리하는 RestController 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/coupons")
public class QueryCouponController {

    private final QueryCouponService queryCouponService;

    /**
     * 모든 트리거에 대한 쿠폰 목록을 페이지네이션하여 조회합니다.
     *
     * @param pageable 페이지네이션 정보
     * @return 페이지네이션 된 트리거를 가진 쿠폰 요약 정보
     */
    @GetMapping
    public PaginatedResponseDto<CouponSummaryDto> getCouponList(Pageable pageable) {

        Page<CouponSummaryDto> data = queryCouponService.getTriggeredCouponList(
                pageable);

        return PaginatedResponseDto.<CouponSummaryDto>builder()
                .currentPage(data.getNumber())
                .totalPage(data.getTotalPages())
                .totalDataCount(data.getTotalElements())
                .dataList(data.getContent())
                .build();
    }
}
