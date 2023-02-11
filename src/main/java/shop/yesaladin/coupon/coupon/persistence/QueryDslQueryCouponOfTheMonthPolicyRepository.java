package shop.yesaladin.coupon.coupon.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.yesaladin.coupon.coupon.domain.model.CouponOfTheMonthPolicy;
import shop.yesaladin.coupon.coupon.domain.model.querydsl.QCouponOfTheMonthPolicy;
import shop.yesaladin.coupon.coupon.domain.repository.QueryCouponOfTheMonthPolicyRepository;

/**
 * 이달의 쿠폰 정책을 조회하는 Repository 의 구현 클래스입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@RequiredArgsConstructor
@Repository
public class QueryDslQueryCouponOfTheMonthPolicyRepository implements
        QueryCouponOfTheMonthPolicyRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<CouponOfTheMonthPolicy> findLatestCouponOfTheMonthPolicy() {
        QCouponOfTheMonthPolicy policy = QCouponOfTheMonthPolicy.couponOfTheMonthPolicy;

        return Optional.ofNullable(queryFactory.selectFrom(policy)
                .innerJoin(policy.coupon)
                .fetchJoin()
                .orderBy(policy.id.desc())
                .fetchFirst());
    }
}
