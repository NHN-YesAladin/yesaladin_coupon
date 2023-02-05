package shop.yesaladin.coupon.coupon.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import shop.yesaladin.coupon.code.CouponTypeCode;
import shop.yesaladin.coupon.coupon.persistence.converter.CouponTypeCodeConverter;

/**
 * 쿠폰 엔터티 입니다.
 *
 * @author 서민지, 김홍대
 * @since 1.0
 */
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "coupons")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
public abstract class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private boolean isUnlimited;

    @Column
    private String fileUri;

    @Column
    private Integer duration;

    @Column
    private LocalDate expirationDate;

    @CreatedDate
    private LocalDateTime createdDatetime;

    @Column(name = "coupon_type_code_id")
    @Convert(converter = CouponTypeCodeConverter.class)
    private CouponTypeCode couponTypeCode;

    @OneToMany(mappedBy = "coupon")
    private List<Trigger> triggerList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    private CouponBound couponBound;

    public void addTrigger(Trigger trigger) {
        if (Objects.isNull(triggerList)) {
            this.triggerList = new ArrayList<>();
        }
        this.triggerList.add(trigger);
    }

    public long getAmount() {
        CouponTypeCode typeCode = this.couponTypeCode;
        if (typeCode.equals(CouponTypeCode.FIXED_PRICE)) {
            return ((AmountCoupon) this).getDiscountAmount();
        } else if (typeCode.equals(CouponTypeCode.FIXED_RATE)) {
            return ((RateCoupon) this).getDiscountRate();
        } else {
            return ((PointCoupon) this).getChargePointAmount();
        }
    }
}
