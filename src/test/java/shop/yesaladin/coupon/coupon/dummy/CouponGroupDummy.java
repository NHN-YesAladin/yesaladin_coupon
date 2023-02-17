package shop.yesaladin.coupon.coupon.dummy;

import java.time.LocalDateTime;
import java.util.UUID;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.CouponGroup;

public class CouponGroupDummy {

    public static CouponGroup dummyCouponGroup(Coupon coupon) {
        return CouponGroup.builder()
                .triggerTypeCode(TriggerTypeCode.MEMBER_GRADE_WHITE)
                .coupon(coupon)
                .groupCode(UUID.randomUUID().toString())
                .createdDatetime(LocalDateTime.now())
                .build();
    }

    public static CouponGroup dummyCouponGroupWithGroupCode(Coupon coupon, String groupCode) {
        return CouponGroup.builder()
                .triggerTypeCode(TriggerTypeCode.MEMBER_GRADE_WHITE)
                .coupon(coupon)
                .groupCode(groupCode)
                .createdDatetime(LocalDateTime.now())
                .build();
    }
}