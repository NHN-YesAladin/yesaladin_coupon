package shop.yesaladin.coupon.coupon.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.coupon.coupon.persistence.converter.CouponGivenStateCodeConverter;
import shop.yesaladin.coupon.coupon.persistence.converter.CouponUsageStateCodeConverter;

/**
 * 발행 쿠폰 엔터티 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "issued_coupons")
@Entity
public class IssuedCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "CHAR(36)")
    private String couponCode;

    @Column(nullable = false)
    private LocalDateTime createdDatetime;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @Column
    private LocalDateTime givenDatetime;

    @Column
    private LocalDateTime usedDatetime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Column(name = "coupon_given_state_code_id")
    @Convert(converter = CouponGivenStateCodeConverter.class)
    private CouponGivenStateCode couponGivenStateCode;

    @Column(name = "coupon_usage_state_code_id")
    @Convert(converter = CouponUsageStateCodeConverter.class)
    private CouponUsageStateCode couponUsageStateCode;
}
