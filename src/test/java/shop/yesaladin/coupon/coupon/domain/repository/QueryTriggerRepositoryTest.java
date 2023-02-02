package shop.yesaladin.coupon.coupon.domain.repository;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.CouponTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.RateCoupon;
import shop.yesaladin.coupon.coupon.domain.model.Trigger;
import shop.yesaladin.coupon.coupon.dto.CouponSummaryDto;
import shop.yesaladin.coupon.trigger.TriggerTypeCode;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class QueryTriggerRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    QueryTriggerRepository triggerRepository;

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
            Trigger trigger = Trigger.builder()
                    .coupon(coupon)
                    .triggerTypeCode(triggerTypeCodeArray[i % triggerTypeCodeArray.length])
                    .build();
            em.persist(trigger);
            triggerList.add(trigger);
        }

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("트리거 정보와 쿠폰 정보가 모두 들어있는 CouponSummaryDto의 페이지네이션 된 리스트를 반환한다.")
    void findAllWithPaginationTest() {
        // given
        int pageSize = 10;

        // when
        Page<CouponSummaryDto> actual = triggerRepository.findAll(PageRequest.of(0, pageSize));

        // then
        Assertions.assertThat(actual).hasSize(pageSize);
        for (int i = 0; i < pageSize; i++) {
            CouponSummaryDto actualDto = actual.getContent().get(i);
            Trigger expectedTrigger = triggerList.get(i);
            RateCoupon expectedCoupon = (RateCoupon) expectedTrigger.getCoupon();

            Assertions.assertThat(actualDto.getId()).isEqualTo(expectedCoupon.getId());
            Assertions.assertThat(actualDto.getName()).isEqualTo(expectedCoupon.getName());
            Assertions.assertThat(actualDto.getTriggerTypeCode())
                    .isEqualTo(expectedTrigger.getTriggerTypeCode());
            Assertions.assertThat(actualDto.getCouponTypeCode())
                    .isEqualTo(CouponTypeCode.FIXED_RATE);
            Assertions.assertThat(actualDto.getIsUnlimited())
                    .isEqualTo(expectedCoupon.isUnlimited());
            Assertions.assertThat(actualDto.getDuration()).isEqualTo(expectedCoupon.getDuration());
            Assertions.assertThat(actualDto.getExpirationDate())
                    .isEqualTo(expectedCoupon.getExpirationDate());
            Assertions.assertThat(actualDto.getCreatedDateTime())
                    .isEqualTo(expectedCoupon.getCreatedDatetime());
            Assertions.assertThat(actualDto.getMinOrderAmount())
                    .isEqualTo(expectedCoupon.getMinOrderAmount());
            Assertions.assertThat(actualDto.getDiscountAmount()).isNull();
            Assertions.assertThat(actualDto.getChargePointAmount()).isNull();
            Assertions.assertThat(actualDto.getMaxDiscountAmount())
                    .isEqualTo(expectedCoupon.getMaxDiscountAmount());
            Assertions.assertThat(actualDto.getDiscountRate())
                    .isEqualTo(expectedCoupon.getDiscountRate());
        }
    }

    @Test
    @DisplayName("트리거 타입 코드와 쿠폰 id로 트리거를 가져온다.")
    void findTriggerByTriggerTypeCodeAndCouponTest() {
        // when
        Optional<Trigger> actual = triggerRepository.findTriggerByTriggerTypeCodeAndCouponId(
                TriggerTypeCode.SIGN_UP,
                couponList.get(0).getId()
        );
        // then
        Assertions.assertThat(actual).isPresent();
        Assertions.assertThat(actual.get().getCoupon().getId())
                .isEqualTo(couponList.get(0).getId());
        Assertions.assertThat(actual.get().getTriggerTypeCode()).isEqualTo(TriggerTypeCode.SIGN_UP);
    }
}