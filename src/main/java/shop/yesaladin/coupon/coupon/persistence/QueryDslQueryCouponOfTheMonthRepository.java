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
 */
@RequiredArgsConstructor
@Repository
public class QueryDslQueryCouponOfTheMonthRepository implements
        QueryCouponOfTheMonthPolicyRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<CouponOfTheMonthPolicy> findFirstByOrderByIdDesc() {
        QCouponOfTheMonthPolicy policy = QCouponOfTheMonthPolicy.couponOfTheMonthPolicy;

        return Optional.ofNullable(queryFactory.selectFrom(policy)
                .innerJoin(policy.coupon)
                .fetchJoin()
                .orderBy(policy.id.desc())
                .fetchFirst());
    }
}
