package shop.yesaladin.coupon.coupon.persistence;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.querydsl.QCoupon;
import shop.yesaladin.coupon.coupon.domain.model.querydsl.QTrigger;
import shop.yesaladin.coupon.coupon.domain.repository.QueryCouponRepository;
import shop.yesaladin.coupon.coupon.dto.CouponSummaryDto;

/**
 * QueryDsl 을 사용하여 쿠폰 관련 정보를 가져오기 위한 Repository 구현체입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@RequiredArgsConstructor
@Repository
public class QueryDslQueryCouponRepository implements QueryCouponRepository {

    private final JPAQueryFactory queryFactory;
    private final Clock clock;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Coupon> findCouponById(long couponId) {
        QCoupon coupon = QCoupon.coupon;
        return Optional.ofNullable(queryFactory.select(coupon)
                .from(coupon)
                .where(coupon.id.eq(couponId))
                .fetchFirst());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CouponSummaryDto> findCouponByTriggerCode(
            boolean includeExpired, TriggerTypeCode triggerTypeCode, Pageable pageable
    ) {
        QTrigger trigger = QTrigger.trigger;

        List<Coupon> couponList = queryFactory.select(trigger.coupon)
                .from(trigger)
                .where(trigger.triggerTypeCode.eq(triggerTypeCode))
                .where(includeExpired ? null
                        : trigger.coupon.expirationDate.goe(LocalDate.now(clock)))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(trigger.count()).from(trigger);

        List<CouponSummaryDto> couponSummaryDtoList = couponList.stream()
                .map(coupon -> new CouponSummaryDto().toDto(triggerTypeCode, coupon))
                .collect(Collectors.toList());

        return PageableExecutionUtils.getPage(
                couponSummaryDtoList,
                pageable,
                countQuery::fetchFirst
        );
    }
}
