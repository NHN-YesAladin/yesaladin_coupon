package shop.yesaladin.coupon.coupon.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.coupon.coupon.domain.repository.QueryIssuedCouponRepository;
import shop.yesaladin.coupon.coupon.domain.repository.QueryTriggerRepository;
import shop.yesaladin.coupon.coupon.dto.CouponSummaryDto;
import shop.yesaladin.coupon.coupon.dto.MemberCouponSummaryDto;
import shop.yesaladin.coupon.coupon.service.inter.QueryCouponService;

/**
 * QueryCouponService 인터페이스의 구현체 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class QueryCouponServiceImpl implements QueryCouponService {

    private final QueryTriggerRepository queryTriggerRepository;
    private final QueryIssuedCouponRepository queryIssuedCouponRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CouponSummaryDto> getTriggeredCouponList(Pageable pageable) {
        return queryTriggerRepository.findAll(pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<MemberCouponSummaryDto> getMemberCouponList(List<String> couponCodeList) {
        return queryIssuedCouponRepository.getMemberCouponSummary(couponCodeList);
    }
}
