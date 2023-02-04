package shop.yesaladin.coupon.coupon.domain.repository;

import java.util.List;
import shop.yesaladin.coupon.coupon.domain.model.CouponGivenStateCode;
import shop.yesaladin.coupon.coupon.domain.model.CouponUsageStateCode;

public interface CommandIssuedCouponRepository {
    long updateCouponGivenState(List<String> couponCodeList, CouponGivenStateCode givenStateCode);

    long updateCouponUsageState(List<String> couponCodeList, CouponUsageStateCode usageStateCode);
}
