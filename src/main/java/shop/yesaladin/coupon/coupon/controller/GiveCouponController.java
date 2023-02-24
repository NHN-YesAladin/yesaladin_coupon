package shop.yesaladin.coupon.coupon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.coupon.coupon.service.inter.GiveCouponService;
import shop.yesaladin.coupon.message.CouponCodesAndResultMessage;
import shop.yesaladin.coupon.message.CouponCodesMessage;
import shop.yesaladin.coupon.message.CouponGiveRequestMessage;
import shop.yesaladin.coupon.message.CouponGiveRequestResponseMessage;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/give")
public class GiveCouponController {

    private final GiveCouponService giveCouponService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<CouponGiveRequestResponseMessage> requestGiveCoupon(@RequestBody CouponGiveRequestMessage request) {
        CouponGiveRequestResponseMessage response = giveCouponService.giveCoupon(
                request);

        return ResponseDto.<CouponGiveRequestResponseMessage>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .data(response)
                .build();
    }

    @PostMapping("/complete")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<Void> giveCouponComplete(@RequestBody CouponCodesAndResultMessage result) {
        giveCouponService.finishGiveCouponRequest(result);
        return ResponseDto.<Void>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .data(null)
                .build();
    }

    @PostMapping("/cancel")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<Void> cancelGiveCoupon(@RequestBody CouponCodesMessage cancelRequest) {
        giveCouponService.cancelGiveCoupon(cancelRequest);
        return ResponseDto.<Void>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .data(null)
                .build();
    }
}
