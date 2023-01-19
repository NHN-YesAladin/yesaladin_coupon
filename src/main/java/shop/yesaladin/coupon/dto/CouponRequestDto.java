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
import shop.yesaladin.coupon.domain.model.TriggerTypeCode;

@Getter
@AllArgsConstructor
public abstract class CouponRequestDto {

    public TriggerTypeCode triggerTypeCode;

    @NotBlank(message = "coupon name must be at least 2 characters long")
    @Length(max = 50, message = "coupon name cannot be more than 50 characters")
    protected String name;

    protected Boolean isUnlimited;

    @PositiveOrZero(message = "invalid coupon quantity")
    protected Integer quantity;

    @Length(max = 255, message = "file Uri cannot be more than 255 characters")
    protected String fileUri;

    @PositiveOrZero(message = "invalid duration of use")
    protected Integer duration;

    @Future(message = "invalid coupon expiration date")
    protected LocalDate expirationDate;

    protected CouponTypeCode couponTypeCode;

    public abstract Coupon toEntity();
}
