package shop.yesaladin.coupon.coupon.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import shop.yesaladin.coupon.message.CouponCodesAndResultMessage;

@Getter
public class CouponGivenResultEvent extends ApplicationEvent {

    private final CouponCodesAndResultMessage result;

    public CouponGivenResultEvent(Object source, CouponCodesAndResultMessage result) {
        super(source);
        this.result = result;
    }
}
