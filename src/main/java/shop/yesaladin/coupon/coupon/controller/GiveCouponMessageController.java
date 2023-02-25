package shop.yesaladin.coupon.coupon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.coupon.coupon.queue.GiveCouponMessageQueue;
import shop.yesaladin.coupon.message.CouponCodesAndResultMessage;
import shop.yesaladin.coupon.message.CouponCodesMessage;
import shop.yesaladin.coupon.message.CouponGiveRequestMessage;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/messages/give")
public class GiveCouponMessageController {

    private final GiveCouponMessageQueue messageQueue;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void requestGiveCoupon(@RequestBody CouponGiveRequestMessage request) {
        messageQueue.enqueueGiveRequest(request);
    }

    @PostMapping("/given")
    @ResponseStatus(HttpStatus.CREATED)
    public void giveCouponComplete(@RequestBody CouponCodesAndResultMessage result) {
        messageQueue.enqueueGivenResult(result);
    }

    @PostMapping("/cancel")
    @ResponseStatus(HttpStatus.CREATED)
    public void cancelGiveCoupon(@RequestBody CouponCodesMessage cancelRequest) {
        messageQueue.enqueueCancelGiveMessage(cancelRequest);
    }
}
