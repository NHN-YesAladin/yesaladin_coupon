package shop.yesaladin.coupon.coupon.domain.model;

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
import shop.yesaladin.coupon.code.CouponBoundCode;
import shop.yesaladin.coupon.coupon.persistence.converter.CouponBoundCodeConverter;

/**
 * 쿠폰 범위 엔터티 입니다.
 *
 * @author 서민지, 김홍대
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "coupon_bounds")
@Entity
public class CouponBound {

    @Id
    @Column
    private Long couponId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Column(length = 50)
    private String isbn;

    @Column
    private Long categoryId;

    @Column(name = "coupon_bound_code_id")
    @Convert(converter = CouponBoundCodeConverter.class)
    private CouponBoundCode couponBoundCode;
}
