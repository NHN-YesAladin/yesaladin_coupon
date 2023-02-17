package shop.yesaladin.coupon.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.yesaladin.coupon.code.TriggerTypeCode;

@Getter
@AllArgsConstructor
public class CouponTriggerDto {

    private TriggerTypeCode triggerTypeCode;
    private long couponId;
}
