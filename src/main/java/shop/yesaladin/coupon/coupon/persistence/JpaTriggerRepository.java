package shop.yesaladin.coupon.coupon.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.coupon.coupon.domain.model.Trigger;
import shop.yesaladin.coupon.coupon.domain.repository.CommandTriggerRepository;
import shop.yesaladin.coupon.coupon.domain.repository.QueryTriggerRepository;

/**
 * JPA 를 사용하여 트리거를 생성하기 위한 Repository 인터페이스입니다.
 *
 * @author 서민지
 * @since 1.0
 */
public interface JpaTriggerRepository extends Repository<Trigger, Long>, CommandTriggerRepository,
        QueryTriggerRepository {

}
