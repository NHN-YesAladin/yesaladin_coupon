package shop.yesaladin.coupon.coupon.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import shop.yesaladin.coupon.code.CouponBoundCode;
import shop.yesaladin.coupon.code.CouponTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.CouponUsageStateCode;
import shop.yesaladin.coupon.coupon.domain.model.IssuedCoupon;

@Getter
@Builder
@AllArgsConstructor
public class MemberCouponSummaryDto {

    private String name;
    private String couponCode;
    private long amount;    // 할인율/할인금액/충전포인트금액
    private CouponTypeCode couponTypeCode;
    private LocalDateTime expireDate;
    private Boolean isUsed;
    // isbn/categoryId/null
    private String couponBound;
    private CouponBoundCode couponBoundCode;

    public static MemberCouponSummaryDto fromEntity(IssuedCoupon issuedCoupon) {
        Coupon coupon = issuedCoupon.getCouponGroup().getCoupon();
        return MemberCouponSummaryDto.builder()
                .name(coupon.getName())
                .couponCode(issuedCoupon.getCouponCode())
                .amount(coupon.getAmount())
                .couponTypeCode(coupon.getCouponTypeCode())
                .expireDate(coupon.getExpirationDate().atTime(LocalTime.MAX))
                .isUsed(issuedCoupon.getCouponUsageStateCode().equals(CouponUsageStateCode.USED))
                .couponBound(coupon.getCouponBound().getBound())
                .couponBoundCode(coupon.getCouponBound().getCouponBoundCode())
                .build();
    }
}