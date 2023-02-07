package shop.yesaladin.coupon.coupon.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.CouponGivenStateCode;
import shop.yesaladin.coupon.coupon.domain.model.CouponGroup;
import shop.yesaladin.coupon.coupon.domain.model.CouponUsageStateCode;
import shop.yesaladin.coupon.coupon.domain.model.IssuedCoupon;
import shop.yesaladin.coupon.coupon.dummy.CouponDummy;
import shop.yesaladin.coupon.coupon.dummy.CouponGroupDummy;
import shop.yesaladin.coupon.coupon.dummy.IssuedCouponDummy;

@Transactional
@AutoConfigureTestEntityManager
@SpringBootTest
class QueryDslCommandIssuedCouponRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private QueryDslCommandIssuedCouponRepository repository;
    private IssuedCoupon issuedCoupon;
    private CouponGroup couponGroup;
    private Coupon coupon;
    private String couponCode;
    private List<String> couponCodeList;

    @BeforeEach
    void setUp() {
        couponCode = UUID.randomUUID().toString();
        couponCodeList = List.of(couponCode);
        coupon = CouponDummy.dummyRateCoupon();
        couponGroup = CouponGroupDummy.dummyCouponGroup(coupon);
        issuedCoupon = IssuedCouponDummy.dummyIssuedCoupon(couponCode, couponGroup);
        em.persist(coupon);
        em.persist(couponGroup);
    }

    @Test
    @DisplayName("발행쿠폰 지급완료 업데이트 성공")
    void updateCouponGivenStateAndDateTime_GivenSuccessTest() {
        // given
        IssuedCoupon before = em.persist(issuedCoupon);
        CouponGivenStateCode couponGivenStateCode = CouponGivenStateCode.GIVEN;

        // when
        long count = repository.updateCouponGivenStateAndDateTime(
                couponCodeList,
                couponGivenStateCode
        );

        // then
        IssuedCoupon after = em.find(IssuedCoupon.class, before.getId());
        Assertions.assertThat(count).isEqualTo(1);
        Assertions.assertThat(before.getCouponGivenStateCode())
                .isEqualTo(CouponGivenStateCode.NOT_GIVEN);
        Assertions.assertThat(after.getCouponGivenStateCode()).isEqualTo(couponGivenStateCode);
        Assertions.assertThat(after.getGivenDatetime()).isBefore(LocalDateTime.now());
    }

    @Test
    @DisplayName("발행쿠폰 미지급 업데이트 성공")
    void updateCouponGivenStateAndDateTime_GivenFailTest() {
        // given
        issuedCoupon = IssuedCouponDummy.dummyGivenIssuedCoupon(couponCode, couponGroup);
        IssuedCoupon before = em.persist(issuedCoupon);

        CouponGivenStateCode couponGivenStateCode = CouponGivenStateCode.NOT_GIVEN;

        // when
        long count = repository.updateCouponGivenStateAndDateTime(
                couponCodeList,
                couponGivenStateCode
        );

        // then
        IssuedCoupon result = em.find(IssuedCoupon.class, before.getId());
        Assertions.assertThat(count).isEqualTo(1);
        Assertions.assertThat(result.getCouponGivenStateCode()).isEqualTo(couponGivenStateCode);
        Assertions.assertThat(result.getGivenDatetime()).isNull();
    }

    @Test
    @DisplayName("발행쿠폰 사용완료 업데이트 성공")
    void updateCouponUsageStateAndDateTime_UsageSuccessTest() {
        // given
        IssuedCoupon before = em.persist(issuedCoupon);
        CouponUsageStateCode couponUsageStateCode = CouponUsageStateCode.USED;

        // when
        long count = repository.updateCouponUsageStateAndDateTime(
                couponCodeList,
                couponUsageStateCode
        );

        // then
        IssuedCoupon after = em.find(IssuedCoupon.class, before.getId());
        Assertions.assertThat(count).isEqualTo(1);
        Assertions.assertThat(before.getCouponUsageStateCode())
                .isEqualTo(CouponUsageStateCode.NOT_USED);
        Assertions.assertThat(after.getCouponUsageStateCode()).isEqualTo(couponUsageStateCode);
        Assertions.assertThat(after.getUsedDatetime()).isBefore(LocalDateTime.now());
    }

    @Test
    @DisplayName("발행쿠폰 미사용 업데이트 성공")
    void updateCouponUsageStateAndDateTime_UsageFailTest() {
        // given
        issuedCoupon = IssuedCouponDummy.dummyUsedIssuedCoupon(couponCode, couponGroup);
        IssuedCoupon before = em.persist(issuedCoupon);
        CouponUsageStateCode couponUsageStateCode = CouponUsageStateCode.NOT_USED;

        // when
        long count = repository.updateCouponUsageStateAndDateTime(
                couponCodeList,
                couponUsageStateCode
        );

        // then
        IssuedCoupon after = em.find(IssuedCoupon.class, before.getId());
        Assertions.assertThat(count).isEqualTo(1);
        Assertions.assertThat(before.getCouponUsageStateCode())
                .isEqualTo(CouponUsageStateCode.USED);
        Assertions.assertThat(after.getCouponUsageStateCode()).isEqualTo(couponUsageStateCode);
        Assertions.assertThat(after.getUsedDatetime()).isNull();
    }
}