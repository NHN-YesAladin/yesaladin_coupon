package shop.yesaladin.coupon.coupon.persistence;

import org.apache.ibatis.annotations.Mapper;
import shop.yesaladin.coupon.coupon.domain.repository.InsertIssuedCouponRepository;

/**
 * MyBatis 를 사용하여 쿠폰 발행 테이블에 데이터를 삽입하기 위한 Mapper 인터페이스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Mapper
public interface MyBatisInsertIssuedCouponMapper extends InsertIssuedCouponRepository {

}
