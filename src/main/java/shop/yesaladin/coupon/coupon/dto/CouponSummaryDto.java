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

/**
 * 쿠폰에 대한 요약 정보를 담기 위해 사용하는 dto 클래스 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponSummaryDto {

    private long id;
    private String name;
    private TriggerTypeCode triggerTypeCode;
    private CouponTypeCode couponTypeCode;
    private Boolean isUnlimited;
    private Integer duration;
    private LocalDate expirationDate;
    private LocalDateTime createdDateTime;
    private Integer minOrderAmount;
    private Integer discountAmount;
    private Integer chargePointAmount;
    private Integer maxDiscountAmount;
    private Integer discountRate;

    /**
     * Coupon(포인트/정액할인/정율할인)을 CouponSummaryDto 로 변환하는 메서드 입니다.
     *
     * @param triggerTypeCode coupon 의 triggerTypeCode
     * @param coupon          요약할 Coupon entity
     * @return coupon 타입별로 필요한 정보를 담은 dto
     */
    public CouponSummaryDto toDto(TriggerTypeCode triggerTypeCode, Coupon coupon) {
        CouponTypeCode couponType = coupon.getCouponTypeCode();

        CouponSummaryDtoBuilder builder = CouponSummaryDto.builder()
                .id(coupon.getId())
                .name(coupon.getName())
                .triggerTypeCode(triggerTypeCode)
                .couponTypeCode(couponType)
                .isUnlimited(coupon.isUnlimited())
                .duration(coupon.getDuration())
                .expirationDate(coupon.getExpirationDate())
                .createdDateTime(coupon.getCreatedDatetime());

        if (CouponTypeCode.POINT.equals(couponType)) {
            PointCoupon pointCoupon = (PointCoupon) coupon;
            return builder.chargePointAmount(pointCoupon.getChargePointAmount()).build();
        } else if (CouponTypeCode.FIXED_RATE.equals(couponType)) {
            RateCoupon rateCoupon = (RateCoupon) coupon;
            return builder.minOrderAmount(rateCoupon.getMinOrderAmount())
                    .maxDiscountAmount(rateCoupon.getMaxDiscountAmount())
                    .discountRate(rateCoupon.getDiscountRate())
                    .build();
        } else {
            AmountCoupon amountCoupon = (AmountCoupon) coupon;
            return builder.minOrderAmount(amountCoupon.getMinOrderAmount())
                    .discountAmount(amountCoupon.getDiscountAmount())
                    .build();
        }
    }
}