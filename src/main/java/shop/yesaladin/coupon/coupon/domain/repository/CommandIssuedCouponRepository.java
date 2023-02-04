package shop.yesaladin.coupon.coupon.domain.repository;

import java.util.List;
import shop.yesaladin.coupon.coupon.domain.model.CouponGivenStateCode;

public interface CommandIssuedCouponRepository {
    long updateCouponGivenState(List<String> couponCodeList, CouponGivenStateCode givenStateCode);
}
