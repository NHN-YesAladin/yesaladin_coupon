package shop.yesaladin.coupon.coupon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.coupon.coupon.service.impl.CouponConsumerServiceImpl.CouponCodesAndResultMessage;
import shop.yesaladin.coupon.coupon.service.inter.CouponConsumerService;
import shop.yesaladin.coupon.message.CouponGiveRequestMessage;
import shop.yesaladin.coupon.message.CouponGiveRequestResponseMessage;


@RequiredArgsConstructor
@RestController
@RequestMapping("/give")
public class GiveCouponController {

    private final CouponConsumerService couponConsumerService;

    @PostMapping("/request")
    public CouponGiveRequestResponseMessage giveRequest(CouponGiveRequestMessage message) {
        return couponConsumerService.consumeCouponGiveRequestMessage(message);
    }

    @PostMapping("/given")
    public void given(CouponCodesAndResultMessage message) {
        couponConsumerService.consumeCouponGivenMessage(message);
    }
}
