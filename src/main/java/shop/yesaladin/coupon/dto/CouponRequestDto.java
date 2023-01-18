package shop.yesaladin.coupon.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import shop.yesaladin.coupon.domain.model.AmountCoupon;
import shop.yesaladin.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.domain.model.CouponBoundCode;
import shop.yesaladin.coupon.domain.model.CouponTypeCode;
import shop.yesaladin.coupon.domain.model.PointCoupon;
import shop.yesaladin.coupon.domain.model.RateCoupon;
import shop.yesaladin.coupon.domain.model.TriggerTypeCode;

/**
 * 쿠폰 생성 시 사용하는 DTO 클래스 입니다.
 * (정액할인 쿠폰, 정율할인 쿠폰, 포인트 쿠폰)
 *
 * @author 서민지
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public class CouponRequestDto {

    private TriggerTypeCode triggerTypeCode;

    @NotBlank(message = "coupon name must be at least 2 characters long")
    @Length(max = 50, message = "coupon name cannot be more than 50 characters")
    private String name;

    @PositiveOrZero(message = "invalid coupon quantity")
    private int quantity;

    @Length(max = 255, message = "file Uri cannot be more than 255 characters")
    private String fileUri;

    @PositiveOrZero(message = "invalid duration of use")
    private Integer duration;

    @Future(message = "invalid coupon expiration date")
    private LocalDate expirationDate;

    // TODO validation 추가
    private CouponTypeCode couponTypeCode;

    @PositiveOrZero(message = "invalid point amount")
    private Integer chargePointAmount;

    @PositiveOrZero(message = "invalid minimum order amount")
    private int minOrderAmount;

    @PositiveOrZero(message = "invalid discount amount")
    private Integer discountAmount;

    @PositiveOrZero(message = "invalid maximum discount amount")
    private Integer maxDiscountAmount;

    @PositiveOrZero(message = "invalid discount rate")
    @Max(value = 100, message = "discount rate cannot exceed 100")
    private Integer discountRate;

    private boolean canBeOverlapped;

    // 적용 범위
    private CouponBoundCode couponBoundCode;
    private String ISBN;
    private Long categoryId;

    public Coupon toEntity(CouponTypeCode couponTypeCode) {
        if (couponTypeCode.equals(CouponTypeCode.FIXED_PRICE)) {
            return toAmountCouponEntity();
        } else if (couponTypeCode.equals(CouponTypeCode.FIXED_RATE)) {
            return toRateCouponEntity();
        }
        return toPointCouponEntity();
    }

    public AmountCoupon toAmountCouponEntity() {
        return AmountCoupon.builder()
                .name(this.name)
                .quantity(this.quantity)
                .fileUri(this.fileUri)
                .duration(this.duration)
                .expirationDate(this.expirationDate)
                .couponTypeCode(this.couponTypeCode)
                .minOrderAmount(this.minOrderAmount)
                .canBeOverlapped(this.canBeOverlapped)
                .build();
    }

    public RateCoupon toRateCouponEntity() {
        return RateCoupon.builder()
                .name(this.name)
                .quantity(this.quantity)
                .fileUri(this.fileUri)
                .duration(this.duration)
                .expirationDate(this.expirationDate)
                .couponTypeCode(this.couponTypeCode)
                .minOrderAmount(this.minOrderAmount)
                .maxDiscountAmount(this.maxDiscountAmount)
                .discountRate(this.discountRate)
                .canBeOverlapped(this.canBeOverlapped)
                .build();
    }

    public PointCoupon toPointCouponEntity() {
        return PointCoupon.builder()
                .name(this.name)
                .quantity(this.quantity)
                .fileUri(this.fileUri)
                .duration(this.duration)
                .expirationDate(this.expirationDate)
                .couponTypeCode(this.couponTypeCode)
                .chargePointAmount(this.chargePointAmount)
                .build();
    }
}
