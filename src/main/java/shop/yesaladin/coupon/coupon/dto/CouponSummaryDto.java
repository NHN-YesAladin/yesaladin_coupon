package shop.yesaladin.coupon.coupon.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.coupon.coupon.domain.model.CouponTypeCode;
import shop.yesaladin.coupon.trigger.TriggerTypeCode;

/**
 * 쿠폰에 대한 요약 정보를 담기 위해 사용하는 dto 클래스 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponSummaryDto {

    private long id;
    private String name;
    private TriggerTypeCode triggerTypeCode;
    private CouponTypeCode couponTypeCode;
    private boolean isUnlimited;
    private Integer duration;
    private LocalDate expirationDate;
    private LocalDateTime createdDateTime;
    private Integer rateMinOrderAmount;
    private Integer amountMinOrderAmount;
    private Integer discountAmount;
    private Integer chargePointAmount;
    private Integer maxDiscountAmount;
    private Integer discountRate;
}