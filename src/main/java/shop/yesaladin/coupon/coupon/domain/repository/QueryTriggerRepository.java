package shop.yesaladin.coupon.coupon.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.coupon.coupon.domain.model.Trigger;

public interface QueryTriggerRepository {

    Page<Trigger> findTriggers(Pageable pageable);
}
