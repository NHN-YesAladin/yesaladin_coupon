package shop.yesaladin.coupon.coupon.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.CouponGroup;
import shop.yesaladin.coupon.coupon.domain.model.querydsl.QCouponGroup;
import shop.yesaladin.coupon.coupon.domain.repository.QueryCouponGroupRepository;

/**
 * 쿠폰 그룹을 조회하기 위한 레포지토리 인터페이스의 구현체입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@RequiredArgsConstructor
@Repository
public class QueryDslQueryCouponGroupRepository implements QueryCouponGroupRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<CouponGroup> findCouponGroupByTriggerTypeAndCouponId(
            TriggerTypeCode triggerTypeCode,
            long couponId
    ) {

        QCouponGroup couponGroup = QCouponGroup.couponGroup;
        return Optional.ofNullable(queryFactory.select(couponGroup)
                .from(couponGroup)
                .where(couponGroup.coupon.id.eq(couponId))
                .where(couponGroup.triggerTypeCode.eq(triggerTypeCode))
                .orderBy(couponGroup.createdDatetime.desc())
                .fetchFirst());
    }

    @Override
    public Optional<CouponGroup> findCouponGroupByTriggerType(TriggerTypeCode triggerTypeCode) {
        QCouponGroup couponGroup = QCouponGroup.couponGroup;
        return Optional.ofNullable(queryFactory.selectFrom(couponGroup)
                .where(couponGroup.triggerTypeCode.eq(triggerTypeCode))
                .orderBy(couponGroup.createdDatetime.desc())
                .fetchFirst());
    }
}
