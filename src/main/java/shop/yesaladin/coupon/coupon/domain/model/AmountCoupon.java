package shop.yesaladin.coupon.coupon.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 정액 할인 쿠폰 엔티티입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "amount_coupons")
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class AmountCoupon extends Coupon {

    @Column(nullable = false)
    private int minOrderAmount;

    @Column(nullable = false)
    private int discountAmount;

    @Column(nullable = false)
    private boolean canBeOverlapped;
}