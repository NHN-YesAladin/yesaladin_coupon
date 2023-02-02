package shop.yesaladin.coupon.coupon.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.yesaladin.coupon.coupon.domain.model.CouponGroup;
import shop.yesaladin.coupon.coupon.domain.model.Trigger;
import shop.yesaladin.coupon.coupon.domain.model.querydsl.QCouponGroup;
import shop.yesaladin.coupon.coupon.domain.repository.QueryCouponGroupRepository;

/**
 * 쿠폰 그룹을 조회하기 위한 레포지토리 인터페이스의 구현체입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@RequiredArgsConstructor
@Repository
public class QueryDslQueryCouponGroupRepository implements QueryCouponGroupRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<CouponGroup> findCouponGroupByTrigger(Trigger trigger) {
        QCouponGroup couponGroup = QCouponGroup.couponGroup;
        return Optional.ofNullable(queryFactory.select(couponGroup)
                .from(couponGroup)
                .where(couponGroup.coupon.eq(trigger.getCoupon()))
                .where(couponGroup.triggerTypeCode.eq(trigger.getTriggerTypeCode()))
                .orderBy(couponGroup.createdDatetime.desc())
                .fetchFirst());
    }
}
