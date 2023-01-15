package shop.yesaladin.coupon.dto;

import javax.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponIssuanceRequestDto {

    @PositiveOrZero
    private long couponId;

    private Integer quantity;
}
