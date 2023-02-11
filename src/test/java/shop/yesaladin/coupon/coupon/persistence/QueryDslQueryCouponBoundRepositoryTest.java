package shop.yesaladin.coupon.coupon.persistence;

import java.util.Optional;
import javax.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.CouponBound;
import shop.yesaladin.coupon.coupon.dummy.CouponBoundDummy;
import shop.yesaladin.coupon.coupon.dummy.CouponDummy;

@SpringBootTest
@Transactional
class QueryDslQueryCouponBoundRepositoryTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private QueryDslQueryCouponBoundRepository repository;
    private CouponBound couponBound;

    @BeforeEach
    void setup() {
        Coupon coupon = CouponDummy.dummyRateCoupon();
        couponBound = CouponBoundDummy.dummyCouponBoundProduct(coupon);

        em.persist(coupon);
        em.persist(couponBound);
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("쿠폰 ID로 쿠폰 범위 조회에 성공한다.")
    void findCouponBoundByCouponIdTest() {
        // when
        Optional<CouponBound> actual = repository.findCouponBoundByCouponId(couponBound.getCouponId());

        // then
        Assertions.assertThat(actual).isPresent();
        Assertions.assertThat(actual.get().getIsbn()).isEqualTo(couponBound.getIsbn());
    }
}