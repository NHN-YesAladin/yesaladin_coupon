package shop.yesaladin.coupon.domain.repository;

import shop.yesaladin.coupon.domain.model.CouponBound;
import shop.yesaladin.coupon.domain.model.CouponBoundCode;

public interface CommandCouponBoundRepository {

    CouponBound save(CouponBound couponBound);

}
