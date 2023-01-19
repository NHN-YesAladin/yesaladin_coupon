package shop.yesaladin.coupon.dto;

import java.time.LocalDate;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import shop.yesaladin.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.domain.model.CouponBoundCode;
import shop.yesaladin.coupon.domain.model.CouponTypeCode;
import shop.yesaladin.coupon.domain.model.RateCoupon;
import shop.yesaladin.coupon.domain.model.TriggerTypeCode;

/**
 * 정율할인 쿠폰 생성 요청 시 사용하는 Dto 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public class RateCouponRequestDto {

    private TriggerTypeCode triggerTypeCode;

    @NotBlank(message = "coupon name must be at least 2 characters long")
    @Length(max = 50, message = "coupon name cannot be more than 50 characters")
    private String name;

    private boolean isUnlimited;

    @PositiveOrZero(message = "invalid coupon quantity")
    private Integer quantity;

    @Length(max = 255, message = "file Uri cannot be more than 255 characters")
    private String fileUri;

    @PositiveOrZero(message = "invalid duration of use")
    private Integer duration;

    @Future(message = "invalid coupon expiration date")
    private LocalDate expirationDate;

    // TODO validation 추가
    private CouponTypeCode couponTypeCode;

    @PositiveOrZero(message = "invalid minimum order amount")
    private int minOrderAmount;

    @Positive(message = "invalid maximum discount amount")
    private int maxDiscountAmount;

    @Positive(message = "invalid discount rate")
    @Max(value = 100, message = "discount rate cannot exceed 100")
    private int discountRate;

    private boolean canBeOverlapped;

    // 적용 범위
    private CouponBoundCode couponBoundCode;
    private String ISBN;
    private Long categoryId;

    public Coupon toEntity() {
        return RateCoupon.builder()
                .name(this.name)
                .isUnlimited(this.isUnlimited)
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

}
