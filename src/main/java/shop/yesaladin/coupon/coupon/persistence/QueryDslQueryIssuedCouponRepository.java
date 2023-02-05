package shop.yesaladin.coupon.coupon.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.yesaladin.coupon.coupon.domain.model.CouponGivenStateCode;
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

@RequiredArgsConstructor
@Repository
public class QueryDslQueryIssuedCouponRepository implements QueryIssuedCouponRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<IssuedCoupon> findIssuedCouponByGroupCodeId(long groupCodeId) {
        QIssuedCoupon issuedCoupon = QIssuedCoupon.issuedCoupon;
        return Optional.ofNullable(queryFactory.selectFrom(
                        issuedCoupon)
                .where(
                        issuedCoupon.couponGroup.id.eq(groupCodeId),
                        issuedCoupon.couponGivenStateCode.eq(
                                CouponGivenStateCode.NOT_GIVEN),
                        issuedCoupon.expirationDate.after(
                                LocalDate.now())
                ).fetchFirst());
    }

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
                        rateCoupon.discountRate
                                .nullif(amountCoupon.discountAmount)
                                .nullif(pointCoupon.chargePointAmount),
                        coupon.couponTypeCode,
                        issuedCoupon.expirationDate,
                        issuedCoupon.usedDatetime.isNotNull(),
                        couponBound.categoryId.stringValue().nullif(couponBound.isbn).nullif(""),
                        couponBound.couponBoundCode
                ))
                .from(coupon)
                .innerJoin(couponGroup)
                .on(couponGroup.coupon.eq(coupon))
                .innerJoin(issuedCoupon)
                .on(issuedCoupon.couponGroup.eq(couponGroup))
                .innerJoin(couponBound)
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
}
