package shop.yesaladin.coupon.coupon.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.yesaladin.coupon.coupon.domain.model.CouponGivenStateCode;
import shop.yesaladin.coupon.coupon.domain.model.IssuedCoupon;
import shop.yesaladin.coupon.coupon.domain.model.querydsl.QIssuedCoupon;
import shop.yesaladin.coupon.coupon.domain.repository.QueryIssuedCouponRepository;

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

}
