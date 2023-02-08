package shop.yesaladin.coupon.coupon.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import java.time.LocalDateTime;
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
    public long updateCouponGivenStateAndDateTime(
            List<String> couponCodeList, CouponGivenStateCode givenStateCode
    ) {
        QIssuedCoupon issuedCoupon = QIssuedCoupon.issuedCoupon;
        JPAUpdateClause updateClause = queryFactory.update(issuedCoupon);

        if (givenStateCode.equals(CouponGivenStateCode.GIVEN)) {
            updateClause.set(issuedCoupon.givenDatetime, LocalDateTime.now());
        } else if (givenStateCode.equals(CouponGivenStateCode.NOT_GIVEN)) {
            updateClause.set(issuedCoupon.givenDatetime, (LocalDateTime) null);
        }

        long count = updateClause.set(issuedCoupon.couponGivenStateCode, givenStateCode)
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
    public long updateCouponUsageStateAndDateTime(
            List<String> couponCodeList, CouponUsageStateCode usageStateCode
    ) {
        QIssuedCoupon issuedCoupon = QIssuedCoupon.issuedCoupon;
        JPAUpdateClause updateClause = queryFactory.update(issuedCoupon);

        if (usageStateCode.equals(CouponUsageStateCode.USED)) {
            updateClause.set(issuedCoupon.usedDatetime, LocalDateTime.now());
        } else if (usageStateCode.equals(CouponUsageStateCode.NOT_USED)) {
            updateClause.set(issuedCoupon.usedDatetime, (LocalDateTime) null);
        }

        long count = updateClause.set(issuedCoupon.couponUsageStateCode, usageStateCode)
                .where(issuedCoupon.couponCode.in(couponCodeList))
                .execute();

        entityManager.clear();
        entityManager.flush();

        return count;
    }
}
