package shop.yesaladin.coupon.persistence;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.coupon.domain.model.AmountCoupon;
import shop.yesaladin.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.domain.model.CouponTypeCode;
import shop.yesaladin.coupon.domain.model.Trigger;
import shop.yesaladin.coupon.trigger.TriggerTypeCode;

@DataJpaTest
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