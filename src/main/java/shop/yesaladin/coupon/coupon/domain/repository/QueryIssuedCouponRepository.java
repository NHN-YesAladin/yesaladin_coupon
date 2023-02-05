package shop.yesaladin.coupon.coupon.domain.repository;

import java.util.List;
import java.util.Optional;
import shop.yesaladin.coupon.coupon.domain.model.IssuedCoupon;
import shop.yesaladin.coupon.coupon.dto.MemberCouponSummaryDto;

public interface QueryIssuedCouponRepository {

    Optional<IssuedCoupon> findIssuedCouponByGroupCodeId(long groupCodeId);

    List<MemberCouponSummaryDto> getMemberCouponSummary(List<String> couponCodeList);
}
