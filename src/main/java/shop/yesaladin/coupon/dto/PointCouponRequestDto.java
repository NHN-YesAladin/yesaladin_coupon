package shop.yesaladin.coupon.dto;

import java.time.LocalDate;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import shop.yesaladin.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.domain.model.CouponTypeCode;
import shop.yesaladin.coupon.domain.model.PointCoupon;
import shop.yesaladin.coupon.domain.model.TriggerTypeCode;

/**
 * 포인트 쿠폰 생성 요청 시 사용하는 Dto 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Getter
public class PointCouponRequestDto extends CouponRequestDto {

    public PointCouponRequestDto(
            TriggerTypeCode triggerTypeCode,
            @NotBlank(message = "coupon name must be at least 2 characters long") @Length(max = 50, message = "coupon name cannot be more than 50 characters") String name,
            Boolean isUnlimited,
            @PositiveOrZero(message = "invalid coupon quantity") Integer quantity,
            @Length(max = 255, message = "file Uri cannot be more than 255 characters") String fileUri,
            @PositiveOrZero(message = "invalid duration of use") Integer duration,
            @Future(message = "invalid coupon expiration date") LocalDate expirationDate,
            CouponTypeCode couponTypeCode,
            int chargePointAmount
    ) {
        super(
                triggerTypeCode,
                name,
                isUnlimited,
                quantity,
                fileUri,
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
                .name(this.name)
                .isUnlimited(this.isUnlimited)
                .fileUri(this.fileUri)
                .duration(this.duration)
                .expirationDate(this.expirationDate)
                .couponTypeCode(this.couponTypeCode)
                .chargePointAmount(this.chargePointAmount)
                .build();
    }
}
