package shop.yesaladin.coupon.domain.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import shop.yesaladin.coupon.persistence.converter.CouponCodeConverter;

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
public abstract class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Builder.Default
    @Column(nullable = false)
    private int quantity = -1;

    @Column
    private String fileUri;

    @Column
    private Integer duration;

    @Column
    private LocalDate expirationDate;

    @Column(name = "coupon_type_code_id")
    @Convert(converter = CouponCodeConverter.class)
    private CouponCode couponTypeCode;

    @OneToMany(mappedBy = "coupon")
    private List<Trigger> triggerList = new ArrayList<>();
}
