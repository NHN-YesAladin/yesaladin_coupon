package shop.yesaladin.coupon.persistence;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.domain.model.CouponCode;

@DataJpaTest
class JpaCouponQueryRepositoryTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private JpaCouponQueryRepository repository;
    private Coupon coupon;

    @BeforeEach
    void setUp() {
        coupon = Coupon.builder()
                .name("test coupon")
                .quantity(500)
                .minOrderAmount(1000)
                .maxDiscountAmount(1000)
                .discountAmount(1000)
                .canBeOverlapped(false)
                .openDatetime(LocalDateTime.of(2023, 1, 1, 0, 0))
                .couponTypeCode(CouponCode.POINT)
                .issuanceCode(CouponCode.AUTO_ISSUANCE)
                .build();

        em.persist(coupon);
    }

    @Test
    @DisplayName("ID로 쿠폰 정보 조회에 성공한다.")
    void findCouponByIdTest() {
        // when
        Optional<Coupon> actual = repository.findCouponById(coupon.getId());

        // then
        Assertions.assertThat(actual).isNotEmpty().contains(coupon);
    }
}