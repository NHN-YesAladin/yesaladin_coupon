package shop.yesaladin.coupon.coupon.dto;

import java.time.LocalDate;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.CouponTypeCode;
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

    private MultipartFile imageFile;
    @Setter
    private String imageFileUri;

    @PositiveOrZero(message = "invalid duration of use")
    private Integer duration;

    @Future(message = "invalid coupon expiration date")
    private LocalDate expirationDate;

    private CouponTypeCode couponTypeCode;

    public abstract Coupon toEntity();
}
