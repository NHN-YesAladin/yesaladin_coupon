package shop.yesaladin.coupon.coupon.domain.repository;

import java.util.Optional;
import shop.yesaladin.coupon.coupon.domain.model.IssuedCoupon;

public interface QueryIssuedCouponRepository {

    Optional<IssuedCoupon> findValidCouponByCouponId(long couponId);

    Optional<IssuedCoupon> findIssuedCouponByGroupCodeId(long groupCodeId);
}
