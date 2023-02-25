package shop.yesaladin.coupon.coupon.scheduler;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import shop.yesaladin.coupon.coupon.event.CouponCancelGiveEvent;
import shop.yesaladin.coupon.coupon.event.CouponGiveRequestEvent;
import shop.yesaladin.coupon.coupon.event.CouponGivenResultEvent;
import shop.yesaladin.coupon.coupon.queue.GiveCouponMessageQueue;
import shop.yesaladin.coupon.message.CouponCodesAndResultMessage;
import shop.yesaladin.coupon.message.CouponCodesMessage;
import shop.yesaladin.coupon.message.CouponGiveRequestMessage;

@RequiredArgsConstructor
@Component
public class GiveCouponScheduler {

    private final GiveCouponMessageQueue messageQueue;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(fixedDelay = 500)
    public void consumeGiveRequestMessage() {
        CouponGiveRequestMessage request = messageQueue.dequeueGiveRequest();
        if (Objects.nonNull(request)) {
            eventPublisher.publishEvent(new CouponGiveRequestEvent(this, request));
        }
    }

    @Scheduled(fixedDelay = 500)
    public void consumeGivenResultMessage() {
        CouponCodesAndResultMessage result = messageQueue.dequeueGivenResult();
        if (Objects.nonNull(result)) {
            eventPublisher.publishEvent(new CouponGivenResultEvent(this, result));
        }
    }

    @Scheduled(fixedDelay = 500)
    public void consumeGiveCancelMessage() {
        CouponCodesMessage couponCodesMessage = messageQueue.dequeueCancelGiveMessage();
        if (Objects.nonNull(couponCodesMessage)) {
            eventPublisher.publishEvent(new CouponCancelGiveEvent(this, couponCodesMessage));
        }
    }
}
