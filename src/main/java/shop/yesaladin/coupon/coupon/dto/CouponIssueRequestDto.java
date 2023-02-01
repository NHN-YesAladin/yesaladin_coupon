package shop.yesaladin.coupon.coupon.dto;

import java.util.Objects;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.ScriptAssert;
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
@ScriptAssert(lang = "javascript", script = "!_this.triggerTypeCode || !_this.couponId", message = "The request must have either triggerTypeCode or couponId.")
public class CouponIssueRequestDto {

    @EnumValue(enumClass = TriggerTypeCode.class)
    private String triggerTypeCode;

    @PositiveOrZero(message = "invalid coupon id: ${validatedValue}")
    private Long couponId;

    @Positive(message = "invalid coupon issuance quantity: ${validatedValue}")
    private Integer quantity;

    public boolean requestWithTriggerCode() {
        return Objects.nonNull(triggerTypeCode);
    }
}
