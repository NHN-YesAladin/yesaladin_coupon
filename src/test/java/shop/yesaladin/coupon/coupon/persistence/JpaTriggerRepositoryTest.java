package shop.yesaladin.coupon.coupon.persistence;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import shop.yesaladin.coupon.code.CouponTypeCode;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.AmountCoupon;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.Trigger;

@DataJpaTest
@ActiveProfiles("local-test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaTriggerRepositoryTest {

    @Autowired
    private JpaCommandCouponRepository commandCouponRepository;
    @Autowired
    private JpaTriggerRepository triggerRepository;
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
        Trigger trigger = Trigger.builder()
                .id(savedCoupon.getId())
                .triggerTypeCode(TriggerTypeCode.SIGN_UP)
                .coupon(savedCoupon)
                .build();

        // when
        Trigger save = triggerRepository.save(trigger);

        // then
        Assertions.assertThat(save.getTriggerTypeCode()).isEqualTo(trigger.getTriggerTypeCode());
        Assertions.assertThat(save.getCoupon().getId()).isEqualTo(savedCoupon.getId());
    }
}