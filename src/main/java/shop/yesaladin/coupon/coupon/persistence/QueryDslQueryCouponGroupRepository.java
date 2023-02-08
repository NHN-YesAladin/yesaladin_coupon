package shop.yesaladin.coupon.coupon.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.CouponGroup;
import shop.yesaladin.coupon.coupon.domain.model.querydsl.QCouponGroup;
import shop.yesaladin.coupon.coupon.domain.model.querydsl.QTrigger;
import shop.yesaladin.coupon.coupon.domain.repository.QueryCouponGroupRepository;
import shop.yesaladin.coupon.coupon.dto.CouponGroupAndLimitDto;

/**
 * 쿠폰 그룹을 조회하기 위한 레포지토리 인터페이스의 구현체입니다.
 *
 * @author 김홍대, 서민지
 * @since 1.0
 */
@RequiredArgsConstructor
@Repository
public class QueryDslQueryCouponGroupRepository implements QueryCouponGroupRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<CouponGroup> findCouponGroupByTriggerTypeAndCouponId(
            TriggerTypeCode triggerTypeCode, long couponId
    ) {

        QCouponGroup couponGroup = QCouponGroup.couponGroup;
        return Optional.ofNullable(queryFactory.select(couponGroup)
                .from(couponGroup)
                .where(couponGroup.coupon.id.eq(couponId))
                .where(couponGroup.triggerTypeCode.eq(triggerTypeCode))
                .orderBy(couponGroup.createdDatetime.desc())
                .fetchFirst());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CouponGroupAndLimitDto> findCouponGroupAndLimitMeta(
            TriggerTypeCode triggerTypeCode, Long couponId
    ) {
        QCouponGroup couponGroup = QCouponGroup.couponGroup;
        QTrigger trigger = QTrigger.trigger;

        return queryFactory.select(Projections.constructor(
                        CouponGroupAndLimitDto.class,
                        couponGroup.groupCode,
                        trigger.coupon.isUnlimited.isFalse()
                ))
                .from(trigger)
                .where(trigger.triggerTypeCode.eq(triggerTypeCode))
                .where(Objects.nonNull(couponId) ? trigger.coupon.id.eq(couponId) : null)
                .innerJoin(couponGroup)
                .on(trigger.triggerTypeCode.eq(couponGroup.triggerTypeCode)
                        .and(trigger.coupon.eq(couponGroup.coupon)))
                .fetchJoin()
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<CouponGroup> findCouponGroupByTriggerType(TriggerTypeCode triggerTypeCode) {
        QCouponGroup couponGroup = QCouponGroup.couponGroup;
        return Optional.ofNullable(queryFactory.selectFrom(couponGroup)
                .where(couponGroup.triggerTypeCode.eq(triggerTypeCode))
                .orderBy(couponGroup.createdDatetime.desc())
                .fetchFirst());

    }
}
