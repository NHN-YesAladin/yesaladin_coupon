package shop.yesaladin.coupon.coupon.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.multipart.MultipartFile;
import shop.yesaladin.coupon.code.CouponTypeCode;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.PointCoupon;

/**
 * 포인트 쿠폰 생성 요청 시 사용하는 Dto 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Getter
@Setter
public class PointCouponRequestDto extends CouponRequestDto {

    public PointCouponRequestDto(
            TriggerTypeCode triggerTypeCode,
            @NotBlank(message = "coupon name must be at least 2 characters long") @Length(max = 50, message = "coupon name cannot be more than 50 characters") String name,
            Boolean isUnlimited,
            @PositiveOrZero(message = "invalid coupon quantity") Integer quantity,
            @Range(min = 1, max = 31) Integer couponOpenDate,
            @DateTimeFormat(pattern = "HH:mm") LocalTime couponOpenTime,
            MultipartFile imageFile,
            String imageFileUri,
            @PositiveOrZero(message = "invalid duration of use") Integer duration,
            @DateTimeFormat(iso = ISO.DATE) LocalDate expirationDate,
            CouponTypeCode couponTypeCode,
            int chargePointAmount
    ) {
        super(
                triggerTypeCode,
                name,
                isUnlimited,
                quantity,
                couponOpenDate,
                couponOpenTime,
                imageFile,
                imageFileUri,
                duration,
                expirationDate,
                couponTypeCode
        );
        this.chargePointAmount = chargePointAmount;
    }

    @Positive(message = "invalid point amount")
    private int chargePointAmount;

    public Coupon toEntity() {
        return PointCoupon.builder()
                .name(this.getName())
                .isUnlimited(this.getIsUnlimited())
                .fileUri(this.getImageFileUri())
                .duration(this.getDuration())
                .expirationDate(this.getExpirationDate())
                .createdDatetime(null)
                .couponTypeCode(this.getCouponTypeCode())
                .chargePointAmount(this.chargePointAmount)
                .build();
    }
}
