package shop.yesaladin.coupon.coupon.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.coupon.coupon.dto.CouponSummaryDto;
import shop.yesaladin.coupon.coupon.dto.MemberCouponSummaryDto;
import shop.yesaladin.coupon.coupon.dto.MemberCouponSummaryRequestDto;
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
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<PaginatedResponseDto<CouponSummaryDto>> getCouponList(Pageable pageable) {

        Page<CouponSummaryDto> couponList = queryCouponService.getTriggeredCouponList(pageable);

        PaginatedResponseDto<CouponSummaryDto> data = PaginatedResponseDto.<CouponSummaryDto>builder()
                .currentPage(couponList.getNumber())
                .totalPage(couponList.getTotalPages())
                .totalDataCount(couponList.getTotalElements())
                .dataList(couponList.getContent())
                .build();

        return ResponseDto.<PaginatedResponseDto<CouponSummaryDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(data)
                .build();
    }

    /**
     * 회원이 가진 쿠폰코드에 대한 쿠폰 정보를 조회합니다.
     *
     * @param memberCouponRequestDto 회원이 가진 쿠폰코드 리스트
     * @return 회원이 가진 쿠폰에 대한 요약 정보
     */
    @GetMapping(params = {"couponCodes"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<List<MemberCouponSummaryDto>> getMemberCouponList(@Valid @ModelAttribute MemberCouponSummaryRequestDto memberCouponRequestDto) {
        List<MemberCouponSummaryDto> memberCouponList = queryCouponService.getMemberCouponList(
                memberCouponRequestDto.getCouponCodeList());

        return ResponseDto.<List<MemberCouponSummaryDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(memberCouponList)
                .build();
    }
}
