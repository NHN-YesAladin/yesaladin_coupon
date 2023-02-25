package shop.yesaladin.coupon.coupon.queue;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.springframework.stereotype.Component;
import shop.yesaladin.coupon.message.CouponCodesAndResultMessage;
import shop.yesaladin.coupon.message.CouponCodesMessage;
import shop.yesaladin.coupon.message.CouponGiveRequestMessage;

@Component
public class GiveCouponMessageQueue {

    private final static List<CouponGiveRequestMessage> giveRequestMessageQueue = Collections.synchronizedList(
            new LinkedList<>());
    private final static List<CouponCodesAndResultMessage> givenMessageQueue = Collections.synchronizedList(
            new LinkedList<>());
    private final static List<CouponCodesMessage> cancelGiveMessageQueue = Collections.synchronizedList(
            new LinkedList<>());

    public void enqueueGiveRequest(CouponGiveRequestMessage request) {
        giveRequestMessageQueue.add(request);
    }

    public void enqueueGivenResult(CouponCodesAndResultMessage result) {
        givenMessageQueue.add(result);
    }

    public void enqueueCancelGiveMessage(CouponCodesMessage message) {
        cancelGiveMessageQueue.add(message);
    }

    public CouponGiveRequestMessage dequeueGiveRequest() {
        if (giveRequestMessageQueue.isEmpty()) {
            return null;
        }
        return giveRequestMessageQueue.remove(0);
    }

    public CouponCodesAndResultMessage dequeueGivenResult() {
        if (givenMessageQueue.isEmpty()) {
            return null;
        }
        return givenMessageQueue.remove(0);
    }

    public CouponCodesMessage dequeueCancelGiveMessage() {
        if (cancelGiveMessageQueue.isEmpty()) {
            return null;
        }
        return cancelGiveMessageQueue.remove(0);
    }
}
