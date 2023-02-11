package shop.yesaladin.coupon.coupon.domain.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.dto.CouponSummaryDto;

/**
 * 쿠폰 관련 데이터를 조회하기 위한 Repository 인터페이스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
public interface QueryCouponRepository {

    /**
     * 쿠폰의 PK로 쿠폰 정보를 가져오는 메서드입니다.
     *
     * @param couponId 정보를 가져올 쿠폰의 ID
     * @return 쿠폰의 정보(Optional)
     */

    Optional<Coupon> findCouponById(long couponId);

    /**
     * 트리거 코드로 쿠폰 정보를 페이지네이션하여 가져옵니다.
     *
     * @param triggerTypeCode 정보를 가져올 트리거 코드
     * @param pageable        페이지네이션을 위한 pageable 객체
     * @return 해당 트리거를 가진 모든 쿠폰 리스트
     */
    Page<CouponSummaryDto> findCouponByTriggerCode(TriggerTypeCode triggerTypeCode, Pageable pageable);
}
