package shop.yesaladin.coupon.coupon.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import shop.yesaladin.coupon.code.CouponBoundCode;
import shop.yesaladin.coupon.code.CouponTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.AmountCoupon;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.CouponBound;

@DataJpaTest
@ActiveProfiles("local-test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaCouponBoundRepositoryTest {

    @Autowired
    private JpaCommandCouponRepository commandCouponRepository;
    @Autowired
    private JpaCouponBoundRepository couponBoundRepository;
    private Coupon coupon;

    @BeforeEach
    void setUp() {
        coupon = AmountCoupon.builder()
                .name("test coupon")
                .couponTypeCode(CouponTypeCode.FIXED_PRICE)
                .minOrderAmount(10000)
                .discountAmount(1000)
                .build();
    }

    @Test
    void save() {
        // given
        Coupon savedCoupon = commandCouponRepository.save(coupon);
        CouponBound couponBound = CouponBound.builder()
                .coupon(savedCoupon)
                .couponBoundCode(CouponBoundCode.ALL)
                .build();

        // when
        CouponBound save = couponBoundRepository.save(couponBound);

        // then
        assertThat(save.getCouponId()).isEqualTo(savedCoupon.getId());
        assertThat(save.getCouponBoundCode()).isEqualTo(CouponBoundCode.ALL);
    }
}