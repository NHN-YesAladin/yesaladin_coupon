package shop.yesaladin.coupon.coupon.domain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.Trigger;
import shop.yesaladin.coupon.coupon.dto.CouponSummaryDto;

/**
 * 트리거를 조회하기 위한 Repository 인터페이스입니다.
 *
 * @author 서민지, 김홍대
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
     * @param couponId        조회할 쿠폰 entity
     * @return 조회된 트리거
     */
    Optional<Trigger> findTriggerByTriggerTypeCodeAndCouponId(
            TriggerTypeCode triggerTypeCode,
            long couponId
    );

    /**
     * 트리거 타입 코드로 트리거를 조회합니다.
     *
     * @param triggerTypeCode 조회할 트리거 타입 코드
     * @return 조회된 트리거 리스트
     */
    List<Trigger> findTriggerByTriggerTypeCode(TriggerTypeCode triggerTypeCode);
}
