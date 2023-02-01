package shop.yesaladin.coupon.coupon.domain.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.Trigger;
import shop.yesaladin.coupon.coupon.dto.CouponSummaryDto;
import shop.yesaladin.coupon.trigger.TriggerTypeCode;

/**
 * 트리거를 조회하기 위한 Repository 인터페이스입니다.
 *
 * @author 서민지
 * @since 1.0
 */
public interface QueryTriggerRepository {

    /**
     * 페이지네이션 된 트리거를 조회합니다.
     *
     * @param pageable 페이지네이션 정보
     * @return 페이지네이션 된 Trigger
     */
    Page<CouponSummaryDto> findAll(Pageable pageable);

    /**
     * 쿠폰과 트리거 타입 코드로 트리거를 조회합니다.
     *
     * @param triggerTypeCode 조회할 트리거 타입 코드
     * @param coupon          조회할 쿠폰 entity
     * @return 조회된 트리거
     */
    Optional<Trigger> findTriggerByTriggerTypeCodeAndCoupon(
            TriggerTypeCode triggerTypeCode,
            Coupon coupon
    );
}
