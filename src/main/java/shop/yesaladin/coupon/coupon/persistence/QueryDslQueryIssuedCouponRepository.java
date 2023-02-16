package shop.yesaladin.coupon.coupon.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.yesaladin.coupon.coupon.domain.model.CouponGivenStateCode;
import shop.yesaladin.coupon.coupon.domain.model.CouponUsageStateCode;
import shop.yesaladin.coupon.coupon.domain.model.IssuedCoupon;
import shop.yesaladin.coupon.coupon.domain.model.querydsl.QAmountCoupon;
import shop.yesaladin.coupon.coupon.domain.model.querydsl.QCoupon;
import shop.yesaladin.coupon.coupon.domain.model.querydsl.QCouponBound;
import shop.yesaladin.coupon.coupon.domain.model.querydsl.QCouponGroup;
import shop.yesaladin.coupon.coupon.domain.model.querydsl.QIssuedCoupon;
import shop.yesaladin.coupon.coupon.domain.model.querydsl.QPointCoupon;
import shop.yesaladin.coupon.coupon.domain.model.querydsl.QRateCoupon;
import shop.yesaladin.coupon.coupon.domain.repository.QueryIssuedCouponRepository;
import shop.yesaladin.coupon.coupon.dto.MemberCouponSummaryDto;

/**
 * QueryDsl 을 사용하여 발행쿠폰 관련 정보를 조회하기 위한 Repository 구현체입니다.
 *
 * @author 서민지, 김홍대
 * @since 1.0
 */
@RequiredArgsConstructor
@Repository
public class QueryDslQueryIssuedCouponRepository implements QueryIssuedCouponRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<IssuedCoupon> findIssuedCouponByGroupCodeId(long groupCodeId) {
        QIssuedCoupon issuedCoupon = QIssuedCoupon.issuedCoupon;
        return Optional.ofNullable(queryFactory.selectFrom(issuedCoupon).where(
                issuedCoupon.couponGroup.id.eq(groupCodeId),
                issuedCoupon.couponGivenStateCode.eq(CouponGivenStateCode.NOT_GIVEN),
                issuedCoupon.expirationDate.after(LocalDate.now())
        ).fetchFirst());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberCouponSummaryDto> getMemberCouponSummary(List<String> couponCodeList) {
        QCoupon coupon = QCoupon.coupon;
        QRateCoupon rateCoupon = QRateCoupon.rateCoupon;
        QPointCoupon pointCoupon = QPointCoupon.pointCoupon;
        QAmountCoupon amountCoupon = QAmountCoupon.amountCoupon;
        QCouponGroup couponGroup = QCouponGroup.couponGroup;
        QIssuedCoupon issuedCoupon = QIssuedCoupon.issuedCoupon;
        QCouponBound couponBound = QCouponBound.couponBound;

        return queryFactory.select(Projections.constructor(
                        MemberCouponSummaryDto.class,
                        coupon.name,
                        issuedCoupon.couponCode,
                        rateCoupon.discountRate.coalesce(amountCoupon.discountAmount)
                                .coalesce(pointCoupon.chargePointAmount),
                        rateCoupon.minOrderAmount.coalesce(amountCoupon.minOrderAmount),
                        rateCoupon.maxDiscountAmount,
                        rateCoupon.canBeOverlapped.coalesce(amountCoupon.canBeOverlapped),
                        coupon.couponTypeCode,
                        issuedCoupon.expirationDate,
                        issuedCoupon.usedDatetime.isNotNull(),
                        couponBound.categoryId.stringValue().coalesce(couponBound.isbn).coalesce(""),
                        couponBound.couponBoundCode
                ))
                .from(coupon)
                .innerJoin(couponGroup)
                .on(couponGroup.coupon.eq(coupon))
                .innerJoin(issuedCoupon)
                .on(issuedCoupon.couponGroup.eq(couponGroup))
                .leftJoin(couponBound)
                .on(couponBound.coupon.eq(coupon))
                .leftJoin(rateCoupon)
                .on(rateCoupon.eq(coupon))
                .leftJoin(pointCoupon)
                .on(pointCoupon.eq(coupon))
                .leftJoin(amountCoupon)
                .on(amountCoupon.eq(coupon))
                .where(issuedCoupon.couponCode.in(couponCodeList))
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IssuedCoupon> checkUnavailableIssuedCoupon(
            List<String> couponCodeList, LocalDateTime requestDateTime
    ) {
        QIssuedCoupon issuedCoupon = QIssuedCoupon.issuedCoupon;

        return queryFactory.selectFrom(issuedCoupon)
                .where(issuedCoupon.couponGivenStateCode.ne(CouponGivenStateCode.GIVEN)
                        .or(issuedCoupon.couponUsageStateCode.ne(CouponUsageStateCode.NOT_USED))
                        .or(issuedCoupon.expirationDate.before(requestDateTime.toLocalDate())))
                .where(issuedCoupon.couponCode.in(couponCodeList))
                .fetch();
    }
}
