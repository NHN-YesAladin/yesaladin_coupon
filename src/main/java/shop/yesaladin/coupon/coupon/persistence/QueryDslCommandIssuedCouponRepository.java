package shop.yesaladin.coupon.coupon.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.yesaladin.coupon.coupon.domain.model.CouponGivenStateCode;
import shop.yesaladin.coupon.coupon.domain.model.CouponUsageStateCode;
import shop.yesaladin.coupon.coupon.domain.model.querydsl.QIssuedCoupon;
import shop.yesaladin.coupon.coupon.domain.repository.CommandIssuedCouponRepository;

/**
 * QueryDsl 을 사용하여 발행쿠폰 관련 정보를 생성/수정/삭제 위한 Repository 구현체입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@RequiredArgsConstructor
@Repository
public class QueryDslCommandIssuedCouponRepository implements CommandIssuedCouponRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public long updateCouponGivenState(
            List<String> couponCodeList, CouponGivenStateCode givenStateCode
    ) {
        QIssuedCoupon issuedCoupon = QIssuedCoupon.issuedCoupon;

        long count = queryFactory.update(issuedCoupon)
                .set(issuedCoupon.couponGivenStateCode, givenStateCode)
                .where(issuedCoupon.couponCode.in(couponCodeList))
                .execute();

        entityManager.clear();
        entityManager.flush();

        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long updateCouponUsageState(
            List<String> couponCodeList, CouponUsageStateCode usageStateCode
    ) {
        QIssuedCoupon issuedCoupon = QIssuedCoupon.issuedCoupon;

        long count = queryFactory.update(issuedCoupon)
                .set(issuedCoupon.couponUsageStateCode, usageStateCode)
                .where(issuedCoupon.couponCode.in(couponCodeList))
                .execute();

        entityManager.clear();
        entityManager.flush();

        return count;
    }
}
