package shop.yesaladin.coupon.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.coupon.domain.model.Trigger;
import shop.yesaladin.coupon.domain.repository.CommandTriggerRepository;

public interface JpaTriggerRepository extends Repository<Trigger, Long>, CommandTriggerRepository {

}
