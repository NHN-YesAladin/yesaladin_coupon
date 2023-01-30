package shop.yesaladin.coupon.coupon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.coupon.coupon.dto.CouponSummaryDto;
import shop.yesaladin.coupon.coupon.service.inter.QueryCouponService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/coupons")
public class QueryCouponController {

    private final QueryCouponService queryCouponService;

    @GetMapping
    public Page<CouponSummaryDto> getCouponList(Pageable pageable) {
        return queryCouponService.getTriggeredCouponList(pageable);
    }
}
