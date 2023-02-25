package shop.yesaladin.coupon.coupon.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import shop.yesaladin.coupon.message.CouponCodesMessage;

@Getter
public class CouponCancelGiveEvent extends ApplicationEvent {

    private final CouponCodesMessage message;

    public CouponCancelGiveEvent(Object source, CouponCodesMessage message) {
        super(source);
        this.message = message;
    }
}
