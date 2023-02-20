package shop.yesaladin.coupon.coupon.persistence;

import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import shop.yesaladin.coupon.code.CouponTypeCode;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.AmountCoupon;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.Trigger;

@DataJpaTest
@ActiveProfiles("local-test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaCommandTriggerRepositoryTest {

    @Autowired
    private JpaCommandCouponRepository commandCouponRepository;
    @Autowired
    private JpaCommandTriggerRepository triggerRepository;
    @Autowired
    private TestEntityManager entityManager;
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

    @Test
    void delete() {
        Coupon savedCoupon = entityManager.persist(coupon);
        TriggerTypeCode triggerTypeCode = TriggerTypeCode.SIGN_UP;

        Trigger trigger = Trigger.builder()
                .triggerTypeCode(triggerTypeCode)
                .coupon(savedCoupon)
                .build();
        Trigger savedTrigger = entityManager.persistAndFlush(trigger);

        // when
        Optional<Trigger> beforeDelete = Optional.ofNullable(entityManager.find(
                Trigger.class,
                savedTrigger.getId()
        ));

        triggerRepository.deleteByTriggerTypeCodeAndCouponId(triggerTypeCode, savedCoupon.getId());
        Optional<Trigger> afterDelete = Optional.ofNullable(entityManager.find(
                Trigger.class,
                savedTrigger.getId()
        ));

        // then
        Assertions.assertThat(beforeDelete).isPresent();
        Assertions.assertThat(afterDelete).isEmpty();
    }
}