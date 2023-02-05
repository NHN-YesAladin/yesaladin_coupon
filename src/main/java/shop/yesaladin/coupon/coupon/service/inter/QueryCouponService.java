package shop.yesaladin.coupon.coupon.service.inter;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.coupon.coupon.dto.CouponSummaryDto;
import shop.yesaladin.coupon.coupon.dto.MemberCouponSummaryDto;

/**
 * 쿠폰 조회 관련 서비스 인터페이스입니다.
 *
 * @author 서민지
 * @since 1.0
 */
public interface QueryCouponService {

    /**
     * 모든 트리거에 대한 쿠폰 목록을 페이지네이션하여 조회합니다.
     *
     * @param pageable 페이지네이션 정보
     * @return 페이지네이션 된 트리거를 가진 쿠폰 요약 정보
     */
    Page<CouponSummaryDto> getTriggeredCouponList(Pageable pageable);

    List<MemberCouponSummaryDto> getMemberCouponList(List<String> couponCodeList);
}
