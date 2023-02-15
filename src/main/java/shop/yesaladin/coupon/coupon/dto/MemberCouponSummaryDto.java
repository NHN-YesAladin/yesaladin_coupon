package shop.yesaladin.coupon.coupon.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import shop.yesaladin.coupon.code.CouponBoundCode;
import shop.yesaladin.coupon.code.CouponTypeCode;

/**
 * 회원 쿠폰 목록에 필요한 쿠폰 요약 정보를 담은 dto 입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Getter
@Builder
@ToString
@AllArgsConstructor
public class MemberCouponSummaryDto {

    private String name;
    private String couponCode;
    private int amount;     // 금액(포인트, 할인율, 할인금액)
    private Integer minOrderAmount;
    private Integer maxDiscountAmount;
    private Boolean canBeOverlapped;
    private CouponTypeCode couponTypeCode;
    private LocalDate expireDate;
    private Boolean isUsed;
    private String couponBound; // isbn/categoryId/null
    private CouponBoundCode couponBoundCode;
}