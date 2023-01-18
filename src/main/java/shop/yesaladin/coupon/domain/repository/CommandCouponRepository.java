package shop.yesaladin.coupon.domain.repository;

import shop.yesaladin.coupon.domain.model.Coupon;

public interface CommandCouponRepository {

    Coupon save(Coupon coupon);
}
