package shop.yesaladin.coupon.coupon.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.coupon.coupon.domain.model.CouponGroup;
import shop.yesaladin.coupon.coupon.domain.repository.CommandCouponGroupRepository;

public interface JpaCommandCouponGroupRepository extends Repository<CouponGroup, Long>,
        CommandCouponGroupRepository {

}
