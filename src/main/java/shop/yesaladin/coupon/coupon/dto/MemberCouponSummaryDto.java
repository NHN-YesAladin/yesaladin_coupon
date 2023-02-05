package shop.yesaladin.coupon.coupon.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import shop.yesaladin.coupon.code.CouponBoundCode;
import shop.yesaladin.coupon.code.CouponTypeCode;

@Getter
@Builder
@AllArgsConstructor
public class MemberCouponSummaryDto {

    private String name;
    private String couponCode;
    private int amount;
    private CouponTypeCode couponTypeCode;
    private LocalDate expireDate;
    private Boolean isUsed;
    private String couponBound; // isbn/categoryId/null
    private CouponBoundCode couponBoundCode;
}