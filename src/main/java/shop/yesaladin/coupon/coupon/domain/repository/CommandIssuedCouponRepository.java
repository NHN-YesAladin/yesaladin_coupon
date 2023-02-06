package shop.yesaladin.coupon.coupon.domain.repository;

import java.util.List;
import shop.yesaladin.coupon.coupon.domain.model.CouponGivenStateCode;
import shop.yesaladin.coupon.coupon.domain.model.CouponUsageStateCode;

/**
 * 발행쿠폰을 수정하는 Repository 인터페이스입니다.
 *
 * @author 서민지
 * @since 1.0
 */
public interface CommandIssuedCouponRepository {

    /**
     * 쿠폰코드로 조회한 발행쿠폰의 지급상태를 수정합니다.
     *
     * @param couponCodeList 수정할 발행쿠폰의 쿠폰코드 리스트
     * @param givenStateCode 수정할 지급상태 코드
     * @return 수정된 발행쿠폰 수
     */
    long updateCouponGivenState(List<String> couponCodeList, CouponGivenStateCode givenStateCode);

    /**
     * 쿠폰코드로 조회한 발행쿠폰의 사용상태를 수정합니다.
     *
     * @param couponCodeList 수정할 발행쿠폰의 쿠폰코드 리스트
     * @param usageStateCode 수정할 사용상태 코드
     * @return 수정된 발행쿠폰 수
     */
    long updateCouponUsageState(List<String> couponCodeList, CouponUsageStateCode usageStateCode);
}
