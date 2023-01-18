package shop.yesaladin.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.yesaladin.coupon.domain.model.CouponTypeCode;

@Getter
@AllArgsConstructor
public class CouponResponseDto {

    private String name;
    private Integer discountRate;
    private Integer discountAmount;
    private Integer chargePointAmount;
    private CouponTypeCode couponTypeCode;
}
