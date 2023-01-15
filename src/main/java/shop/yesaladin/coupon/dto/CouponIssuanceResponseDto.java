package shop.yesaladin.coupon.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponIssuanceResponseDto {

    private final List<String> createdCouponCodes;
}
