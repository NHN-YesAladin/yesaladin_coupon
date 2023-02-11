package shop.yesaladin.coupon.coupon.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.yesaladin.coupon.coupon.domain.model.CouponBound;
import shop.yesaladin.coupon.coupon.domain.model.querydsl.QCouponBound;
import shop.yesaladin.coupon.coupon.domain.repository.QueryCouponBoundRepository;

@RequiredArgsConstructor
@Repository
public class QueryDslQueryCouponBoundRepository implements QueryCouponBoundRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<CouponBound> findCouponBoundByCouponId(long couponId) {
        QCouponBound couponBound = QCouponBound.couponBound;
        return Optional.ofNullable(queryFactory.select(couponBound)
                .from(couponBound)
                .where(couponBound.coupon.id.eq(couponId))
                .fetchFirst());
    }
}
