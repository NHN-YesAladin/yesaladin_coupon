package shop.yesaladin.coupon.domain.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import shop.yesaladin.coupon.persistence.converter.CouponBoundCodeConverter;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "coupon_bounds")
@Entity
public class CouponBound {

    @Id
    @Column(name = "coupon_id")
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Column(length = 50)
    private String ISBN;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "coupon_bound_code_id")
    @Convert(converter = CouponBoundCodeConverter.class)
    private CouponBoundCode couponBoundCode;
}
