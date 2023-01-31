package shop.yesaladin.coupon.coupon.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import shop.yesaladin.coupon.coupon.domain.model.querydsl.QAmountCoupon;
import shop.yesaladin.coupon.coupon.domain.model.querydsl.QPointCoupon;
import shop.yesaladin.coupon.coupon.domain.model.querydsl.QRateCoupon;
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
        QPointCoupon pointCoupon = QPointCoupon.pointCoupon;
        QRateCoupon rateCoupon = QRateCoupon.rateCoupon;
        QAmountCoupon amountCoupon = QAmountCoupon.amountCoupon;

        List<CouponSummaryDto> list = queryFactory.select(Projections.constructor(
                        CouponSummaryDto.class,
                        trigger.coupon.id,
                        trigger.coupon.name,
                        trigger.triggerTypeCode,
                        trigger.coupon.couponTypeCode,
                        trigger.coupon.isUnlimited,
                        trigger.coupon.duration,
                        trigger.coupon.expirationDate,
                        trigger.coupon.createdDatetime,
                        rateCoupon.minOrderAmount,
                        amountCoupon.minOrderAmount,
                        amountCoupon.discountAmount,
                        pointCoupon.chargePointAmount,
                        rateCoupon.maxDiscountAmount,
                        rateCoupon.discountRate
                ))
                .from(trigger)
                .leftJoin(pointCoupon)
                .on(trigger.coupon.id.eq(pointCoupon.id))
                .leftJoin(rateCoupon)
                .on(trigger.coupon.id.eq(rateCoupon.id))
                .leftJoin(amountCoupon)
                .on(trigger.coupon.id.eq(amountCoupon.id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(trigger.count()).from(trigger);

        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchFirst);
    }
}
