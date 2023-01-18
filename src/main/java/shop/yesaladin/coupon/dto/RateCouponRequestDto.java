package shop.yesaladin.coupon.dto;

import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import shop.yesaladin.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.domain.model.CouponBoundCode;
import shop.yesaladin.coupon.domain.model.CouponTypeCode;
import shop.yesaladin.coupon.domain.model.RateCoupon;
import shop.yesaladin.coupon.domain.model.TriggerTypeCode;

@Getter
@AllArgsConstructor
public class RateCouponRequestDto {

    private TriggerTypeCode triggerTypeCode;

    @NotBlank(message = "coupon name must be at least 2 characters long")
    @Length(max = 50, message = "coupon name cannot be more than 50 characters")
    private String name;

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
    private Integer minOrderAmount;

    @PositiveOrZero(message = "invalid maximum discount amount")
    private Integer maxDiscountAmount;

    @PositiveOrZero(message = "invalid discount rate")
    @Max(value = 100, message = "discount rate cannot exceed 100")
    private Integer discountRate;

    private Boolean canBeOverlapped;

    // 적용 범위
    private CouponBoundCode couponBoundCode;
    private String ISBN;
    private Long categoryId;

    public Coupon toEntity() {
        return RateCoupon.builder()
                .name(this.name)
                .quantity(Objects.isNull(this.quantity) ? -1 : this.quantity)
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
