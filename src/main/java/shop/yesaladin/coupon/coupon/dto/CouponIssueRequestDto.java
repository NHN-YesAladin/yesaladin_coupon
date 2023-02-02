package shop.yesaladin.coupon.coupon.dto;

import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.coupon.trigger.TriggerTypeCode;
import shop.yesaladin.coupon.validator.annotation.EnumValue;

/**
 * 쿠폰 발급 요청시 사용하는 DTO 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponIssueRequestDto {

    @NotNull
    @EnumValue(enumClass = TriggerTypeCode.class)
    private String triggerTypeCode;

    @PositiveOrZero(message = "invalid coupon id: ${validatedValue}")
    private Long couponId;

    @Positive(message = "invalid coupon issuance quantity: ${validatedValue}")
    private Integer quantity;

    public boolean requestWithTriggerCodeAndCouponId() {
        return Objects.nonNull(this.triggerTypeCode) && Objects.nonNull(this.couponId);
    }
}
