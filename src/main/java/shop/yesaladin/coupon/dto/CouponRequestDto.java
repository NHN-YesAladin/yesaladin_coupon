package shop.yesaladin.coupon.dto;

import java.time.LocalDate;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import shop.yesaladin.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.domain.model.CouponTypeCode;
import shop.yesaladin.coupon.trigger.TriggerTypeCode;

@Getter
@AllArgsConstructor
public abstract class CouponRequestDto {

    private TriggerTypeCode triggerTypeCode;

    @NotBlank(message = "coupon name must be at least 2 characters long")
    @Length(max = 50, message = "coupon name cannot be more than 50 characters")
    private String name;

    private Boolean isUnlimited;

    @PositiveOrZero(message = "invalid coupon quantity")
    private Integer quantity;

    @Length(max = 255, message = "file Uri cannot be more than 255 characters")
    private String fileUri;

    @PositiveOrZero(message = "invalid duration of use")
    private Integer duration;

    @Future(message = "invalid coupon expiration date")
    private LocalDate expirationDate;

    private CouponTypeCode couponTypeCode;

    public abstract Coupon toEntity();
}
