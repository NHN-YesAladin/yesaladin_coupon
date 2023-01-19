package shop.yesaladin.coupon.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.coupon.domain.model.CouponBound;
import shop.yesaladin.coupon.domain.repository.CommandCouponBoundRepository;

/**
 * JPA 를 사용하여 쿠폰 적용 범위를 생성하기 위한 Repository 인터페이스입니다.
 *
 * @author 서민지
 * @since 1.0
 */
public interface JpaCouponBoundRepository extends Repository<CouponBound, Long>,
        CommandCouponBoundRepository {

}
