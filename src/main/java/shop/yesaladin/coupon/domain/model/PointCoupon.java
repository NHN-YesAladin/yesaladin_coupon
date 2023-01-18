package shop.yesaladin.coupon.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "point_coupons")
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class PointCoupon extends Coupon {

    @Column(nullable = false)
    private int chargePointAmount;
}