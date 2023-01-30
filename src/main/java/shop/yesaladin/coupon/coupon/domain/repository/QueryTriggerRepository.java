package shop.yesaladin.coupon.coupon.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.coupon.coupon.domain.model.Trigger;

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
    Page<Trigger> findAll(Pageable pageable);
}
