package shop.yesaladin.coupon.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.domain.repository.QueryCouponRepository;

/**
 * JPA를 사용하여 쿠폰 관련 정보를 가져오기 위한 Repository 인터페이스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
public interface JpaQueryCouponRepository extends Repository<Coupon, Long>, QueryCouponRepository {

}
