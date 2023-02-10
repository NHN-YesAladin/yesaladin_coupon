package shop.yesaladin.coupon.coupon.persistence;

import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.CouponOfTheMonthPolicy;
import shop.yesaladin.coupon.coupon.dummy.CouponDummy;

@DataJpaTest
class JpaCommandCouponOfTheMonthPolicyRepositoryTest {

    @Autowired
    private JpaCommandCouponOfTheMonthPolicyRepository repository;
    @Autowired
    private TestEntityManager em;
    private Coupon coupon;

    @BeforeEach
    void setup() {
        coupon = em.persistAndFlush(CouponDummy.dummyRateCoupon());
    }


    @Test
    @DisplayName("이달의 쿠폰 정책 저장에 성공한다.")
    void saveSuccessTest() {
        // given
        CouponOfTheMonthPolicy policy = CouponOfTheMonthPolicy.builder()
                .coupon(coupon)
                .openDate(1)
                .openTime(LocalTime.of(0, 0))
                .build();
        // when
        CouponOfTheMonthPolicy actual = repository.save(policy);

        // then
        Assertions.assertThat(em.find(CouponOfTheMonthPolicy.class, actual.getId()))
                .isEqualTo(actual);
    }
}