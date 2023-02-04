package shop.yesaladin.coupon.coupon.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.CouponGivenStateCode;
import shop.yesaladin.coupon.coupon.domain.model.querydsl.QIssuedCoupon;
import shop.yesaladin.coupon.coupon.domain.repository.CommandCouponRepository;

@RequiredArgsConstructor
@Repository
public class QueryDslCommandCouponRepository implements CommandCouponRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Override
    public Coupon save(Coupon coupon) {
        entityManager.persist(coupon);
        return coupon;
    }

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
}
