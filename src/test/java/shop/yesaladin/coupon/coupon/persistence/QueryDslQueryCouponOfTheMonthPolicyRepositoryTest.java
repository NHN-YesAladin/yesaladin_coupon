package shop.yesaladin.coupon.coupon.persistence;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
import shop.yesaladin.coupon.coupon.domain.model.CouponOfTheMonthPolicy;
import shop.yesaladin.coupon.coupon.dummy.CouponDummy;

@Transactional
@SpringBootTest
class QueryDslQueryCouponOfTheMonthPolicyRepositoryTest {

    @Autowired
    QueryDslQueryCouponOfTheMonthPolicyRepository repository;
    @Autowired
    EntityManager entityManager;
    private Coupon coupon;

    @BeforeEach
    void setUp() {
        coupon = CouponDummy.dummyRateCoupon();
        entityManager.persist(coupon);
    }

    @Test
    @DisplayName("가장 최근에 등록된 이달쿠 정책 조회 성공")
    void test() {
        // given
        CouponOfTheMonthPolicy first = CouponOfTheMonthPolicy.builder()
                .coupon(coupon)
                .openDate(1)
                .openTime(LocalTime.of(0, 0))
                .quantity(1)
                .createdDateTime(LocalDateTime.now())
                .build();
        entityManager.persist(first);

        CouponOfTheMonthPolicy later = CouponOfTheMonthPolicy.builder()
                .coupon(coupon)
                .openDate(2)
                .openTime(LocalTime.of(0, 0))
                .quantity(2)
                .createdDateTime(LocalDateTime.now())
                .build();
        entityManager.persist(later);

        // when
        Optional<CouponOfTheMonthPolicy> resultOptional = repository.findLatestCouponOfTheMonthPolicy();

        // then
        Assertions.assertThat(resultOptional).isPresent();
        CouponOfTheMonthPolicy result = resultOptional.get();
        Assertions.assertThat(result).isEqualTo(later);
    }
}