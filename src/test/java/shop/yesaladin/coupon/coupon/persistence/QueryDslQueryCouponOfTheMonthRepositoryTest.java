package shop.yesaladin.coupon.coupon.persistence;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.CouponOfTheMonthPolicy;
import shop.yesaladin.coupon.coupon.dummy.CouponDummy;

@Transactional
@SpringBootTest
class QueryDslQueryCouponOfTheMonthRepositoryTest {

    @Autowired
    QueryDslQueryCouponOfTheMonthRepository repository;
    @Autowired
    EntityManager entityManager;
    private Coupon coupon;

    @BeforeEach
    void setUp() {
        coupon = CouponDummy.dummyRateCoupon();
        entityManager.persist(coupon);
    }

    @Test
    void test() {
        CouponOfTheMonthPolicy first = CouponOfTheMonthPolicy.builder()
                .coupon(coupon)
                .openDate(1)
                .openTime(LocalTime.of(0, 0))
                .quantity(1)
                .createdDateTime(LocalDateTime.now())
                .build();
        entityManager.persist(first);

        CouponOfTheMonthPolicy second = CouponOfTheMonthPolicy.builder()
                .coupon(coupon)
                .openDate(2)
                .openTime(LocalTime.of(0, 0))
                .quantity(2)
                .createdDateTime(LocalDateTime.now())
                .build();
        entityManager.persist(second);

        // when
        Optional<CouponOfTheMonthPolicy> result = repository.findFirstByOrderByIdDesc();

        // then
        Assertions.assertThat(result.get().getOpenDate()).isEqualTo(2);
        System.out.println(result.get().getCoupon().getName());
    }
}