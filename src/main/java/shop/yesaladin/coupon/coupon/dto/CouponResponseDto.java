package shop.yesaladin.coupon.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.yesaladin.coupon.code.CouponTypeCode;

/**
 * 쿠폰 생성 후 생성된 쿠폰에 대한 정보를 담은 Dto 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public class CouponResponseDto {

    private String name;
    private CouponTypeCode couponTypeCode;
}
