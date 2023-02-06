package shop.yesaladin.coupon.coupon.domain.repository;

import java.util.List;
import java.util.Optional;
import shop.yesaladin.coupon.coupon.domain.model.IssuedCoupon;
import shop.yesaladin.coupon.coupon.dto.MemberCouponSummaryDto;

/**
 * 발행쿠폰을 조회하는 Repository 인터페이스입니다.
 *
 * @author 서민지
 * @since 1.0
 */
public interface QueryIssuedCouponRepository {

    /**
     * 쿠폰그룹의 PK로 유효한(미지급/만료일 이내) 발행쿠폰을 조회합니다.
     *
     * @param groupCodeId 조회할 발행쿠폰의 쿠폰그룹 PK
     * @return 유효한 발행쿠폰
     */
    Optional<IssuedCoupon> findIssuedCouponByGroupCodeId(long groupCodeId);

    /**
     * 쿠폰코드로 회원쿠폰 목록에 사용할 쿠폰 요약 정보를 조회합니다.
     *
     * @param couponCodeList 회원이 가진 쿠폰코드 리스트
     * @return 회원이 가진 쿠폰의 요약 정보 리스트
     */
    List<MemberCouponSummaryDto> getMemberCouponSummary(List<String> couponCodeList);
}