package shop.yesaladin.coupon.coupon.persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.coupon.code.CouponTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.CouponBound;
import shop.yesaladin.coupon.coupon.domain.model.CouponGroup;
import shop.yesaladin.coupon.coupon.domain.model.IssuedCoupon;
import shop.yesaladin.coupon.coupon.dto.MemberCouponSummaryDto;
import shop.yesaladin.coupon.coupon.dummy.CouponBoundDummy;
import shop.yesaladin.coupon.coupon.dummy.CouponDummy;
import shop.yesaladin.coupon.coupon.dummy.CouponGroupDummy;
import shop.yesaladin.coupon.coupon.dummy.IssuedCouponDummy;

@Transactional
@SpringBootTest
@AutoConfigureTestEntityManager
@ActiveProfiles("local-test")
class QueryDslQueryIssuedCouponRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private QueryDslQueryIssuedCouponRepository repository;
    private Coupon coupon;

    @BeforeEach
    void setUp() {
        coupon = CouponDummy.dummyRateCoupon();
    }

    @Test
    @DisplayName("그룹코드 아이디로 지급 가능한 쿠폰 조회 성공")
    void findIssuedCouponByGroupCodeIdTest() {
        // given
        entityManager.persist(coupon);

        CouponBound couponBound = CouponBoundDummy.dummyCouponBoundProduct(coupon);
        entityManager.persist(couponBound);

        CouponGroup couponGroup = CouponGroupDummy.dummyCouponGroup(coupon);
        CouponGroup actual = entityManager.persist(couponGroup);
        long couponGroupId = actual.getId();

        String couponCode = UUID.randomUUID().toString();
        IssuedCoupon validIssuedCoupon = IssuedCouponDummy.dummyIssuedCoupon(
                couponCode,
                couponGroup
        );
        entityManager.persist(validIssuedCoupon);

        // when
        Optional<IssuedCoupon> result = repository.findIssuedCouponByGroupCodeId(
                couponGroupId);

        // then
        Assertions.assertThat(result).isPresent();
        IssuedCoupon issuedCoupon = result.get();
        Assertions.assertThat(issuedCoupon).isEqualTo(validIssuedCoupon);
    }

    @Test
    @DisplayName("그룹코드 아이디로 지급 가능한 쿠폰 조회 실패")
    void findIssuedCouponByGroupCodeIdFailTest() {
        // given
        entityManager.persist(coupon);

        CouponBound couponBound = CouponBoundDummy.dummyCouponBoundProduct(coupon);
        entityManager.persist(couponBound);

        CouponGroup couponGroup = CouponGroupDummy.dummyCouponGroup(coupon);
        CouponGroup actual = entityManager.persist(couponGroup);
        long couponGroupId = actual.getId();

        String couponCode = UUID.randomUUID().toString();
        IssuedCoupon nonvalidIssuedCoupon = IssuedCouponDummy.dummyGivenIssuedCouponWithExpirationDate(
                couponCode,
                couponGroup,
                LocalDate.now().minusDays(1)
        );
        entityManager.persist(nonvalidIssuedCoupon);

        // when
        Optional<IssuedCoupon> result = repository.findIssuedCouponByGroupCodeId(
                couponGroupId);

        // then
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("쿠폰코드 리스트로 정율할인 쿠폰 MemberCouponSummaryDto 조회 성공")
    void getMemberCouponSummaryRateCouponTest() {
        // given
        entityManager.persist(coupon);
        int amount = coupon.getAmount();

        CouponBound couponBound = CouponBoundDummy.dummyCouponBoundProduct(coupon);
        entityManager.persist(couponBound);

        CouponGroup couponGroup = CouponGroupDummy.dummyCouponGroup(coupon);
        entityManager.persist(couponGroup);

        String couponCode = UUID.randomUUID().toString();
        IssuedCoupon issuedCoupon = IssuedCouponDummy.dummyIssuedCoupon(couponCode, couponGroup);
        String couponCode2 = UUID.randomUUID().toString();
        IssuedCoupon issuedCoupon2 = IssuedCouponDummy.dummyIssuedCoupon(couponCode2, couponGroup);
        entityManager.persist(issuedCoupon);
        entityManager.persist(issuedCoupon2);

        // when
        List<MemberCouponSummaryDto> memberCouponSummary = repository.getMemberCouponSummary(
                List.of(
                        couponCode, couponCode2
                ));

        // then
        Assertions.assertThat(memberCouponSummary).hasSize(2);
        Assertions.assertThat(memberCouponSummary.get(0).getCouponCode()).isEqualTo(couponCode);
        Assertions.assertThat(memberCouponSummary.get(1).getAmount()).isEqualTo(amount);
    }

    @Test
    @DisplayName("쿠폰코드 리스트로 정액할인 쿠폰 MemberCouponSummaryDto 조회 성공")
    void getMemberCouponSummaryAmountCouponTest() {
        // given
        Coupon coupon = CouponDummy.dummyCouponWithCouponType(CouponTypeCode.FIXED_PRICE);
        entityManager.persist(coupon);
        int amount = coupon.getAmount();

        CouponBound couponBound = CouponBoundDummy.dummyCouponBoundCategory(coupon);
        entityManager.persist(couponBound);

        CouponGroup couponGroup = CouponGroupDummy.dummyCouponGroup(coupon);
        entityManager.persist(couponGroup);

        String couponCode = UUID.randomUUID().toString();
        IssuedCoupon issuedCoupon = IssuedCouponDummy.dummyIssuedCoupon(couponCode, couponGroup);
        String couponCode2 = UUID.randomUUID().toString();
        IssuedCoupon issuedCoupon2 = IssuedCouponDummy.dummyIssuedCoupon(couponCode2, couponGroup);
        entityManager.persist(issuedCoupon);
        entityManager.persist(issuedCoupon2);

        // when
        List<MemberCouponSummaryDto> memberCouponSummary = repository.getMemberCouponSummary(
                List.of(
                        couponCode, couponCode2
                ));

        // then
        Assertions.assertThat(memberCouponSummary).hasSize(2);
        Assertions.assertThat(memberCouponSummary.get(0).getCouponCode()).isEqualTo(couponCode);
        Assertions.assertThat(memberCouponSummary.get(1).getAmount()).isEqualTo(amount);
    }

    @Test
    @DisplayName("사용 요청한 쿠폰의 코드로부터 유효하지 않은 쿠폰이 존재하는지 체크")
    void checkUnavailableIssuedCouponTest() {
        // given
        entityManager.persist(coupon);

        CouponBound couponBound = CouponBoundDummy.dummyCouponBoundProduct(coupon);
        entityManager.persist(couponBound);

        CouponGroup couponGroup = CouponGroupDummy.dummyCouponGroup(coupon);
        entityManager.persist(couponGroup);

        List<String> couponCodes = List.of(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()
        );
        IssuedCoupon notGivenIssuedCoupon = IssuedCouponDummy.dummyIssuedCoupon(
                couponCodes.get(0),
                couponGroup
        );
        IssuedCoupon usedIssuedCoupon = IssuedCouponDummy.dummyUsedIssuedCoupon(
                couponCodes.get(1),
                couponGroup
        );
        IssuedCoupon expiredIssuedCoupon = IssuedCouponDummy.dummyGivenIssuedCouponWithExpirationDate(
                couponCodes.get(2),
                couponGroup,
                LocalDate.now().minusDays(1)
        );

        entityManager.persist(notGivenIssuedCoupon);
        entityManager.persist(usedIssuedCoupon);
        entityManager.persist(expiredIssuedCoupon);

        // when
        List<IssuedCoupon> issuedCoupons = repository.checkUnavailableIssuedCoupon(
                couponCodes,
                LocalDateTime.now()
        );

        // then
        Assertions.assertThat(issuedCoupons).hasSize(3);
    }
}