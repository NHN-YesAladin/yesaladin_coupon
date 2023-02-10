package shop.yesaladin.coupon.coupon.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.dto.CouponGroupAndLimitDto;
import shop.yesaladin.coupon.coupon.service.inter.QueryCouponGroupService;

/**
 * 쿠폰 그룹과 관련된 정보를 조회하는 api 컨트롤러 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/coupon-groups")
public class QueryCouponGroupController {

    private final QueryCouponGroupService queryCouponGroupService;

    @GetMapping
    public ResponseDto<List<CouponGroupAndLimitDto>> getCouponMetaForGive(
            @RequestParam(name = "trigger-type") TriggerTypeCode triggerType,
            @RequestParam(required = false, name = "coupon-id") Long couponId
    ) {
        List<CouponGroupAndLimitDto> result = queryCouponGroupService.getCouponGroupAndLimit(
                triggerType,
                couponId
        );

        return ResponseDto.<List<CouponGroupAndLimitDto>>builder()
                .success(true)
                .data(result)
                .status(HttpStatus.OK)
                .build();
    }
}
