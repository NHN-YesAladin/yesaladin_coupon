package shop.yesaladin.coupon.coupon.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.coupon.coupon.domain.model.CouponGroup;
import shop.yesaladin.coupon.coupon.domain.repository.CommandCouponGroupRepository;

/**
 * JPA 를 사용하여 쿠폰그룹을 생성하기 위한 Repository 인터페이스입니다.
 *
 * @author 서민지
 * @since 1.0
 */
public interface JpaCommandCouponGroupRepository extends Repository<CouponGroup, Long>,
        CommandCouponGroupRepository {

}
