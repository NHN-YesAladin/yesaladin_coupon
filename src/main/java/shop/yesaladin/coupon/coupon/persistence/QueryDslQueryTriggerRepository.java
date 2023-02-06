package shop.yesaladin.coupon.coupon.persistence;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.Trigger;
import shop.yesaladin.coupon.coupon.domain.model.querydsl.QTrigger;
import shop.yesaladin.coupon.coupon.domain.repository.QueryTriggerRepository;
import shop.yesaladin.coupon.coupon.dto.CouponSummaryDto;

/**
 * QueryDsl 을 사용하여 트리거 관련 정보를 조회하기 위한 Repository 구현체입니다.
 *
 * @author 서민지, 김홍대
 * @since 1.0
 */
@RequiredArgsConstructor
@Repository
public class QueryDslQueryTriggerRepository implements QueryTriggerRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CouponSummaryDto> findAll(Pageable pageable) {
        QTrigger trigger = QTrigger.trigger;

        List<Trigger> triggerList = queryFactory.select(trigger)
                .from(trigger)
                .innerJoin(trigger.coupon)
                .fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<CouponSummaryDto> list = triggerList.stream()
                .map(t -> new CouponSummaryDto().toDto(t.getTriggerTypeCode(), t.getCoupon()))
                .collect(Collectors.toList());

        JPAQuery<Long> countQuery = queryFactory.select(trigger.count()).from(trigger);

        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchFirst);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Trigger> findTriggerByTriggerTypeCodeAndCouponId(
            TriggerTypeCode triggerTypeCode, long couponId
    ) {
        QTrigger trigger = QTrigger.trigger;

        return Optional.ofNullable(queryFactory.select(trigger)
                .from(trigger)
                .where(trigger.triggerTypeCode.eq(triggerTypeCode))
                .where(trigger.coupon.id.eq(couponId))
                .fetchFirst());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Trigger> findTriggerByTriggerTypeCode(TriggerTypeCode triggerTypeCode) {
        QTrigger trigger = QTrigger.trigger;

        return queryFactory.select(trigger)
                .from(trigger)
                .where(trigger.triggerTypeCode.eq(triggerTypeCode))
                .fetch();
    }
}
