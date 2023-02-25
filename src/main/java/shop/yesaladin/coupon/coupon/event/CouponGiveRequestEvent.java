package shop.yesaladin.coupon.coupon.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import shop.yesaladin.coupon.message.CouponGiveRequestMessage;

@Getter
public class CouponGiveRequestEvent extends ApplicationEvent {

    private final CouponGiveRequestMessage request;

    public CouponGiveRequestEvent(Object source, CouponGiveRequestMessage request) {
        super(source);
        this.request = request;
    }
}
