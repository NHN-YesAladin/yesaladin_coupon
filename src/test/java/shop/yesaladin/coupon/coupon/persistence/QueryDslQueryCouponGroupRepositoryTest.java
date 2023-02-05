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
import shop.yesaladin.coupon.coupon.dto.CouponGroupAndLimitDto;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class QueryDslQueryCouponGroupRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    QueryDslQueryCouponGroupRepository repository;

    List<Coupon> couponList;

    List<CouponGroup> couponGroupList;

    List<Trigger> triggerList;


    @BeforeEach
    void setup() {
        couponList = new ArrayList<>();
        couponGroupList = new ArrayList<>();
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
            couponGroupList.add(couponGroup);

        }

        CouponGroup test = CouponGroup.builder()
                .coupon(couponList.get(0))
                .triggerTypeCode(TriggerTypeCode.BIRTHDAY)
                .groupCode("test")
                .build();

        em.persist(test);

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("트리거 코드와 쿠폰 ID로 쿠폰 그룹 조회에 성공한다.")
    void findCouponGroupByTriggerTypeCodeAndCouponIdTest() {
        // when
        Optional<CouponGroup> actual = repository.findCouponGroupByTriggerTypeAndCouponId(
                couponGroupList.get(0).getTriggerTypeCode(),
                couponGroupList.get(0).getCoupon().getId()
        );

        // then
        Assertions.assertThat(actual).isPresent();
        Assertions.assertThat(actual.get().getTriggerTypeCode()).isEqualTo(TriggerTypeCode.SIGN_UP);
        Assertions.assertThat(actual.get().getCoupon().getId())
                .isEqualTo(couponList.get(0).getId());
    }

    @Test
    @DisplayName("트리거 타입으로 그룹 코드, 무제한 여부 조회에 성공한다.")
    void findAllCouponGroupWithLimitByTriggerTypeAndCouponId() {
        // when
        List<CouponGroupAndLimitDto> actual = repository.findCouponGroupAndLimitMeta(
                TriggerTypeCode.SIGN_UP, null);

        // then
        Assertions.assertThat(actual).hasSize(4);
    }

    @Test
    @DisplayName("트리거 코드와 쿠폰 Id로 그룹코드, 무제한 여부 조회 시 활성화된 트리거가 존재하지 않는다면 조회되지 않는다.")
    void findAllCouponGroupWithLimitByTriggerTypeAndCouponIdWithInvalidTrigger() {
        // when
        List<CouponGroupAndLimitDto> actual = repository.findCouponGroupAndLimitMeta(
                TriggerTypeCode.BIRTHDAY, couponList.get(0).getId());

        // then
        Assertions.assertThat(actual).isEmpty();
    }
}