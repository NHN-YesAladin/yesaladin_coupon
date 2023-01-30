package shop.yesaladin.coupon.coupon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shop.yesaladin.coupon.coupon.domain.model.Trigger;
import shop.yesaladin.coupon.coupon.domain.repository.QueryTriggerRepository;
import shop.yesaladin.coupon.coupon.dto.CouponSummaryDto;
import shop.yesaladin.coupon.coupon.service.inter.QueryCouponService;

@RequiredArgsConstructor
@Service
public class QueryCouponServiceImpl implements QueryCouponService {

    private final QueryTriggerRepository queryTriggerRepository;

    @Override
    public Page<CouponSummaryDto> getTriggeredCouponList(Pageable pageable) {
        Page<Trigger> triggerList = queryTriggerRepository.findTriggers(pageable);
        return triggerList.map(trigger -> {
            return new CouponSummaryDto().toDto(trigger.getTriggerTypeCode(), trigger.getCoupon());
        });
    }
}
