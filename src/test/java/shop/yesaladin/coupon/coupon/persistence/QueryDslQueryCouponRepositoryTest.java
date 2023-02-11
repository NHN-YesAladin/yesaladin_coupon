package shop.yesaladin.coupon.coupon.persistence;

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
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.coupon.code.CouponTypeCode;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.AmountCoupon;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.PointCoupon;
import shop.yesaladin.coupon.coupon.domain.model.Trigger;
import shop.yesaladin.coupon.coupon.dto.CouponSummaryDto;

@Transactional
@SpringBootTest
class QueryDslQueryCouponRepositoryTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private QueryDslQueryCouponRepository repository;
    private Coupon coupon;
    private Coupon triggeredCoupon;

    @BeforeEach
    void setUp() {
        coupon = AmountCoupon.builder()
                .name("test coupon")
                .minOrderAmount(1000)
                .discountAmount(1000)
                .canBeOverlapped(false)
                .duration(3)
                .couponTypeCode(CouponTypeCode.FIXED_PRICE)
                .build();

        em.persist(coupon);

        triggeredCoupon = PointCoupon.builder()
                .name("triggeredCoupon")
                .chargePointAmount(1000)
                .couponTypeCode(CouponTypeCode.POINT)
                .duration(3)
                .build();

        em.persist(triggeredCoupon);

        Trigger trigger = Trigger.builder()
                .triggerTypeCode(TriggerTypeCode.SIGN_UP)
                .coupon(triggeredCoupon)
                .build();
        em.persist(trigger);
        
    }

    @Test
    @DisplayName("ID로 쿠폰 정보 조회에 성공한다.")
    void findCouponByIdTest() {
        // when
        Optional<Coupon> actual = repository.findCouponById(coupon.getId());

        // then
        Assertions.assertThat(actual).isNotEmpty().contains(coupon);
    }

    @Test
    @DisplayName("트리거 코드로 쿠폰 조회에 성공한다.")
    void findCouponByTriggerTypeCodeTest() {
        // when
        Page<CouponSummaryDto> actual = repository.findCouponByTriggerCode(
                TriggerTypeCode.SIGN_UP,
                PageRequest.of(0, 10)
        );

        // then
        Assertions.assertThat(actual).hasSize(1);
        CouponSummaryDto actualCouponSummaryDto = actual.getContent().get(0);
        Assertions.assertThat(actualCouponSummaryDto.getName())
                .isEqualTo(triggeredCoupon.getName());
        Assertions.assertThat(actualCouponSummaryDto.getCouponTypeCode()).isEqualTo(CouponTypeCode.POINT);
        Assertions.assertThat(actualCouponSummaryDto.getTriggerTypeCode()).isEqualTo(TriggerTypeCode.SIGN_UP);
        Assertions.assertThat(actualCouponSummaryDto.getId()).isEqualTo(triggeredCoupon.getId());

    }
}