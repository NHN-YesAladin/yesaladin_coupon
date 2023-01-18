package shop.yesaladin.coupon.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.coupon.domain.model.CouponBound;
import shop.yesaladin.coupon.domain.repository.CommandCouponBoundRepository;

public interface JpaCouponBoundRepository extends Repository<CouponBound, Long>,
        CommandCouponBoundRepository {

}
