package shop.yesaladin.coupon.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import shop.yesaladin.coupon.persistence.converter.CouponCodeConverter;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "coupons")
@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "min_order_amount", nullable = false)
    private int minOrderAmount;

    @Column(name = "max_discount_amount")
    private Integer maxDiscountAmount;

    @Column(name = "discount_rate")
    private Integer discountRate;

    @Column(name = "discount_amount")
    private Integer discountAmount;

    @Column(name = "can_be_overlapped", nullable = false)
    private boolean canBeOverlapped;

    @Column(name = "file_uri", length = 255)
    private String fileUri;

    @Column(name = "open_datetime", nullable = false)
    private LocalDateTime openDatetime;

    @Column
    private Integer duration;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "coupon_type_code_id")
    @Convert(converter = CouponCodeConverter.class)
    private CouponCode couponTypeCode;

    @Column(name = "issuance_code_id")
    @Convert(converter = CouponCodeConverter.class)
    private CouponCode issuanceCode;
}
