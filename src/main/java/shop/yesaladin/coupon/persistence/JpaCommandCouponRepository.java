package shop.yesaladin.coupon.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.domain.repository.CommandCouponRepository;

public interface JpaCommandCouponRepository extends Repository<Coupon, Long>,
        CommandCouponRepository {

}
