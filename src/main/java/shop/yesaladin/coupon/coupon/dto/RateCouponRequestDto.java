package shop.yesaladin.coupon.coupon.dto;

import java.time.LocalDate;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.multipart.MultipartFile;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.CouponBoundCode;
import shop.yesaladin.coupon.coupon.domain.model.CouponTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.RateCoupon;

/**
 * 정율할인 쿠폰 생성 요청 시 사용하는 Dto 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Getter
public class RateCouponRequestDto extends CouponRequestDto {

    public RateCouponRequestDto(
            TriggerTypeCode triggerTypeCode,
            @NotBlank(message = "coupon name must be at least 2 characters long") @Length(max = 50, message = "coupon name cannot be more than 50 characters") String name,
            Boolean isUnlimited,
            @PositiveOrZero(message = "invalid coupon quantity") Integer quantity,
            MultipartFile imageFile,
            String imageFileUri,
            @PositiveOrZero(message = "invalid duration of use") Integer duration,
            @DateTimeFormat(iso = ISO.DATE) LocalDate expirationDate,
            CouponTypeCode couponTypeCode,
            int minOrderAmount,
            int maxDiscountAmount,
            int discountRate,
            boolean canBeOverlapped,
            CouponBoundCode couponBoundCode,
            String isbn,
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
        this.maxDiscountAmount = maxDiscountAmount;
        this.discountRate = discountRate;
        this.canBeOverlapped = canBeOverlapped;
        this.couponBoundCode = couponBoundCode;
        this.isbn = isbn;
        this.categoryId = categoryId;
    }

    @PositiveOrZero(message = "invalid minimum order amount")
    private int minOrderAmount;

    @Positive(message = "invalid maximum discount amount")
    private int maxDiscountAmount;

    @Positive(message = "invalid discount rate")
    @Max(value = 100, message = "discount rate cannot exceed 100")
    private int discountRate;
    private boolean canBeOverlapped;
    private CouponBoundCode couponBoundCode;
    private String isbn;
    private Long categoryId;

    public Coupon toEntity() {
        return RateCoupon.builder()
                .name(this.getName())
                .isUnlimited(this.getIsUnlimited())
                .fileUri(this.getImageFileUri())
                .duration(this.getDuration())
                .expirationDate(this.getExpirationDate())
                .createdDatetime(null)
                .couponTypeCode(this.getCouponTypeCode())
                .minOrderAmount(this.minOrderAmount)
                .maxDiscountAmount(this.maxDiscountAmount)
                .discountRate(this.discountRate)
                .canBeOverlapped(this.canBeOverlapped)
                .build();
    }

}
