package shop.yesaladin.coupon.coupon.service.inter;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.coupon.code.TriggerTypeCode;
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

    /**
     * 쿠폰코드로 회원쿠폰 목록에 사용할 쿠폰 요약 정보를 조회합니다.
     *
     * @param couponCodeList 회원이 가진 쿠폰코드 리스트
     * @return 회원이 가진 쿠폰의 요약 정보 리스트
     */
    List<MemberCouponSummaryDto> getMemberCouponList(List<String> couponCodeList);

    /**
     * 특정 트리거 코드로 발행되는 쿠폰 목록을 페이지네이션하여 조회합니다.
     *
     * @param includeExpired  유효기간이 경과한 쿠폰 조회 여부
     * @param triggerTypeCode 조회할 쿠폰의 트리거 타입 코드
     * @param pageable        페이지네이션 정보
     * @return 페이지네이션 된 쿠폰 요약 정보
     */
    Page<CouponSummaryDto> getCouponListByTriggerTypeCode(
            boolean includeExpired,
            TriggerTypeCode triggerTypeCode,
            Pageable pageable
    );
}
