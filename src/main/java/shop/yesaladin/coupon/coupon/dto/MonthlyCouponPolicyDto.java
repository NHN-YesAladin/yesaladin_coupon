package shop.yesaladin.coupon.coupon.dto;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;

/**
 * 이달의 쿠폰 생성 후 해당 쿠폰의 정책을 생성하기 위해 사용되는 dto 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public class MonthlyCouponPolicyDto {
    private Coupon coupon;
    private LocalTime openTime;
    private int openDate;
    private int couponQuantity;
}
