package shop.yesaladin.coupon.persistence;

import java.util.Optional;
import javax.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.coupon.domain.model.AmountCoupon;
import shop.yesaladin.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.domain.model.CouponTypeCode;

@DataJpaTest
class JpaQueryCouponRepositoryTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private JpaQueryCouponRepository repository;
    private Coupon coupon;

    @BeforeEach
    void setUp() {
        coupon = AmountCoupon.builder()
                .name("test coupon")
                .quantity(500)
                .minOrderAmount(1000)
                .discountAmount(1000)
                .canBeOverlapped(false)
                .duration(3)
                .couponTypeCode(CouponTypeCode.FIXED_PRICE)
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