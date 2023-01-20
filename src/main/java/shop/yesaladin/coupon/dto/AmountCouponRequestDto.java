package shop.yesaladin.coupon.dto;

import java.time.LocalDate;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;
import shop.yesaladin.coupon.domain.model.AmountCoupon;
import shop.yesaladin.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.domain.model.CouponBoundCode;
import shop.yesaladin.coupon.domain.model.CouponTypeCode;
import shop.yesaladin.coupon.trigger.TriggerTypeCode;

/**
 * 정액할인 쿠폰 생성 요청 시 사용하는 Dto 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Getter
public class AmountCouponRequestDto extends CouponRequestDto {

    public AmountCouponRequestDto(
            TriggerTypeCode triggerTypeCode,
            @NotBlank(message = "coupon name must be at least 2 characters long") @Length(max = 50, message = "coupon name cannot be more than 50 characters") String name,
            Boolean isUnlimited,
            @PositiveOrZero(message = "invalid coupon quantity") Integer quantity,
            MultipartFile imageFile,
            String imageFileUri,
            @PositiveOrZero(message = "invalid duration of use") Integer duration,
            @Future(message = "invalid coupon expiration date") LocalDate expirationDate,
            CouponTypeCode couponTypeCode,
            int minOrderAmount,
            int discountAmount,
            boolean canBeOverlapped,
            CouponBoundCode couponBoundCode,
            String ISBN,
            Long categoryId
    ) {
        super(
                triggerTypeCode,
                name,
                isUnlimited,
                quantity,
                imageFile,
                imageFileUri,
                duration,
                expirationDate,
                couponTypeCode
        );
        this.minOrderAmount = minOrderAmount;
        this.discountAmount = discountAmount;
        this.canBeOverlapped = canBeOverlapped;
        this.couponBoundCode = couponBoundCode;
        this.ISBN = ISBN;
        this.categoryId = categoryId;
    }

    @PositiveOrZero(message = "invalid minimum order amount")
    private int minOrderAmount;

    @Positive(message = "invalid discount amount")
    private int discountAmount;
    private boolean canBeOverlapped;
    private CouponBoundCode couponBoundCode;
    private String ISBN;
    private Long categoryId;

    public Coupon toEntity() {
        return AmountCoupon.builder()
                .name(this.getName())
                .isUnlimited(this.getIsUnlimited())
                .fileUri(this.getImageFileUri())
                .duration(this.getDuration())
                .expirationDate(this.getExpirationDate())
                .couponTypeCode(this.getCouponTypeCode())
                .minOrderAmount(this.minOrderAmount)
                .canBeOverlapped(this.canBeOverlapped)
                .build();
    }

}
