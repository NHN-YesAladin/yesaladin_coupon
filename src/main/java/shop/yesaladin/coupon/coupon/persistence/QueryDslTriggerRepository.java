package shop.yesaladin.coupon.coupon.persistence;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import shop.yesaladin.coupon.coupon.domain.model.Trigger;
import shop.yesaladin.coupon.coupon.domain.model.querydsl.QTrigger;
import shop.yesaladin.coupon.coupon.domain.repository.QueryTriggerRepository;
import shop.yesaladin.coupon.coupon.dto.CouponSummaryDto;

@RequiredArgsConstructor
@Repository
public class QueryDslTriggerRepository implements QueryTriggerRepository {

    private final JPAQueryFactory queryFactory;

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
}
