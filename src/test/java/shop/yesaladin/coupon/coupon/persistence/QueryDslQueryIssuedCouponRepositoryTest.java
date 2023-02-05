package shop.yesaladin.coupon.coupon.persistence;

import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
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
class QueryDslQueryIssuedCouponRepositoryTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private QueryDslQueryIssuedCouponRepository queryIssuedCouponRepository;

    @Test
    @DisplayName("쿠폰코드 리스트로 MemberCouponSummaryDto 조회 성공")
    void test() {
        Coupon coupon = CouponDummy.dummyRateCoupon();
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

        List<MemberCouponSummaryDto> memberCouponSummary = queryIssuedCouponRepository.getMemberCouponSummary(
                List.of(
                        couponCode, couponCode2
                ));

        Assertions.assertThat(memberCouponSummary).hasSize(2);
        Assertions.assertThat(memberCouponSummary.get(0).getCouponCode()).isEqualTo(couponCode);
        Assertions.assertThat(memberCouponSummary.get(1).getAmount()).isEqualTo(amount);
    }
}