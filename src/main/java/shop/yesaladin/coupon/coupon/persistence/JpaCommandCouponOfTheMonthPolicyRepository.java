package shop.yesaladin.coupon.coupon.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.coupon.coupon.domain.model.CouponOfTheMonthPolicy;
import shop.yesaladin.coupon.coupon.domain.repository.CommandCouponOfTheMonthPolicyRepository;

/**
 * 이달의 쿠폰 정책을 생성하기 위한 레포지토리 인터페이스의 JPA 구현체입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
public interface JpaCommandCouponOfTheMonthPolicyRepository extends
        Repository<CouponOfTheMonthPolicy, Integer>,
        CommandCouponOfTheMonthPolicyRepository {

}
