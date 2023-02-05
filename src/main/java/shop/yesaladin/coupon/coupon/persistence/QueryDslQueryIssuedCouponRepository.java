package shop.yesaladin.coupon.coupon.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.yesaladin.coupon.coupon.domain.model.CouponGivenStateCode;
import shop.yesaladin.coupon.coupon.domain.model.IssuedCoupon;
import shop.yesaladin.coupon.coupon.domain.model.querydsl.QCouponBound;
import shop.yesaladin.coupon.coupon.domain.model.querydsl.QIssuedCoupon;
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
        QIssuedCoupon issuedCoupon = QIssuedCoupon.issuedCoupon;
        QCouponBound couponBound = QCouponBound.couponBound;

        List<IssuedCoupon> issuedCouponList = queryFactory.select(issuedCoupon)
                .from(issuedCoupon)
                .innerJoin(issuedCoupon.couponGroup)
                .innerJoin(issuedCoupon.couponGroup.coupon)
                .innerJoin(couponBound)
                .on(issuedCoupon.couponGroup.coupon.id.eq(couponBound.couponId))
                .where(issuedCoupon.couponCode.in(couponCodeList))
                .fetch();

        return issuedCouponList.stream()
                .map(MemberCouponSummaryDto::fromEntity)
                .collect(Collectors.toList());
    }
}
