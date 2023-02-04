package shop.yesaladin.coupon.coupon.persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.CouponGroup;
import shop.yesaladin.coupon.coupon.domain.model.CouponTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.RateCoupon;
import shop.yesaladin.coupon.coupon.domain.model.Trigger;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class QueryDslQueryCouponGroupRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    QueryDslQueryCouponGroupRepository repository;

    List<Coupon> couponList;

    List<Trigger> triggerList;

    @BeforeEach
    void setup() {
        couponList = new ArrayList<>();
        triggerList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            RateCoupon coupon = RateCoupon.builder()
                    .name("테스트용 쿠폰" + i)
                    .isUnlimited(i % 2 == 0)
                    .duration(i % 2 == 0 ? 10 : null)
                    .expirationDate(i % 2 == 0 ? null : LocalDate.of(2023, 2, 1))
                    .createdDatetime(LocalDateTime.of(2023, 2, 2, 0, 0))
                    .couponTypeCode(CouponTypeCode.FIXED_RATE)
                    .minOrderAmount(1000)
                    .maxDiscountAmount(10000)
                    .discountRate(10)
                    .canBeOverlapped(i % 2 == 0)
                    .build();
            em.persist(coupon);
            couponList.add(coupon);

            TriggerTypeCode[] triggerTypeCodeArray = TriggerTypeCode.values();
            TriggerTypeCode triggerTypeCode = triggerTypeCodeArray[i % triggerTypeCodeArray.length];
            Trigger trigger = Trigger.builder()
                    .coupon(coupon)
                    .triggerTypeCode(triggerTypeCode)
                    .build();
            em.persist(trigger);
            triggerList.add(trigger);

            CouponGroup couponGroup = CouponGroup.builder()
                    .coupon(coupon)
                    .triggerTypeCode(triggerTypeCode)
                    .groupCode(triggerTypeCode.name() + "-" + coupon.getId())
                    .build();
            em.persist(couponGroup);
        }

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("트리거 entity로 쿠폰 그룹 조회에 성공한다.")
    void findCouponGroupByTriggerTest() {
        // when
        Optional<CouponGroup> actual = repository.findCouponGroupByTrigger(triggerList.get(0));

        // then
        Assertions.assertThat(actual).isPresent();
        Assertions.assertThat(actual.get().getTriggerTypeCode()).isEqualTo(TriggerTypeCode.SIGN_UP);
        Assertions.assertThat(actual.get().getCoupon().getId())
                .isEqualTo(couponList.get(0).getId());
    }
}