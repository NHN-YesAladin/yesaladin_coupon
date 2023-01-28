package shop.yesaladin.coupon.coupon.domain.repository;

import shop.yesaladin.coupon.coupon.domain.model.CouponGroup;

public interface CommandCouponGroupRepository {

    CouponGroup save(CouponGroup couponGroup);
}
