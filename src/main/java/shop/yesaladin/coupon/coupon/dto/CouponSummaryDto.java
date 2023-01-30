package shop.yesaladin.coupon.coupon.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.coupon.coupon.domain.model.AmountCoupon;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.CouponTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.PointCoupon;
import shop.yesaladin.coupon.coupon.domain.model.RateCoupon;
import shop.yesaladin.coupon.trigger.TriggerTypeCode;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponSummaryDto {

    private long id;
    private String name;
    private TriggerTypeCode triggerTypeCode;
    private CouponTypeCode couponTypeCode;
    private boolean isUnlimited;
    private Integer duration;
    private LocalDate expirationDate;
    private LocalDateTime createdDateTime;
    private Integer minOrderAmount;
    private Integer discountAmount;
    private Integer chargePointAmount;
    private Integer maxDiscountAmount;
    private Integer discountRate;

    public CouponSummaryDto toDto(TriggerTypeCode triggerTypeCode, Coupon coupon) {
        CouponTypeCode couponTypeCode = coupon.getCouponTypeCode();

        CouponSummaryDtoBuilder builder = CouponSummaryDto.builder()
                .id(coupon.getId())
                .name(coupon.getName())
                .triggerTypeCode(triggerTypeCode)
                .couponTypeCode(couponTypeCode)
                .isUnlimited(coupon.isUnlimited())
                .duration(coupon.getDuration())
                .expirationDate(coupon.getExpirationDate())
                .createdDateTime(coupon.getCreatedDatetime());

        if (CouponTypeCode.POINT.equals(couponTypeCode)) {
            PointCoupon pointCoupon = (PointCoupon) coupon;
            return builder.chargePointAmount(pointCoupon.getChargePointAmount()).build();
        } else if (CouponTypeCode.FIXED_PRICE.equals(couponTypeCode)) {
            RateCoupon rateCoupon = (RateCoupon) coupon;
            return builder.minOrderAmount(rateCoupon.getMinOrderAmount())
                    .maxDiscountAmount(rateCoupon.getMaxDiscountAmount())
                    .discountAmount(rateCoupon.getDiscountRate())
                    .build();
        } else {
            AmountCoupon amountCoupon = (AmountCoupon) coupon;
            return builder.minOrderAmount(amountCoupon.getMinOrderAmount())
                    .discountAmount(amountCoupon.getDiscountAmount())
                    .build();
        }
    }
}