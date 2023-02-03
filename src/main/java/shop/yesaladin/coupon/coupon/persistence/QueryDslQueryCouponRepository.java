package shop.yesaladin.coupon.coupon.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.querydsl.QCoupon;
import shop.yesaladin.coupon.coupon.domain.model.querydsl.QTrigger;
import shop.yesaladin.coupon.coupon.domain.repository.QueryCouponRepository;

/**
 * QueryDsl을 사용하여 쿠폰 관련 정보를 가져오기 위한 Repository 인터페이스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@RequiredArgsConstructor
@Repository
public class QueryDslQueryCouponRepository implements QueryCouponRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Coupon> findCouponById(long couponId) {
        QCoupon coupon = QCoupon.coupon;
        return Optional.ofNullable(queryFactory.select(coupon)
                .from(coupon)
                .where(coupon.id.eq(couponId))
                .fetchFirst());
    }

    @Override
    public List<Coupon> findCouponByTriggerCode(TriggerTypeCode triggerTypeCode) {
        QTrigger trigger = QTrigger.trigger;

        return queryFactory.select(trigger.coupon)
                .from(trigger)
                .where(trigger.triggerTypeCode.eq(triggerTypeCode))
                .fetch();
    }
}
