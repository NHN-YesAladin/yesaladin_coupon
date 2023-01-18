package shop.yesaladin.coupon.domain.repository;

import shop.yesaladin.coupon.domain.model.Trigger;

public interface CommandTriggerRepository {

    Trigger save(Trigger trigger);
}
