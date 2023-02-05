package shop.yesaladin.coupon.coupon.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.domain.repository.QueryCouponGroupRepository;
import shop.yesaladin.coupon.coupon.dto.CouponGroupAndLimitDto;
import shop.yesaladin.coupon.coupon.service.inter.QueryCouponGroupService;

@RequiredArgsConstructor
@Service
public class QueryCouponGroupServiceImpl implements QueryCouponGroupService {

    private final QueryCouponGroupRepository queryCouponGroupRepository;

    @Override
    public List<CouponGroupAndLimitDto> getCouponGroupAndLimit(
            TriggerTypeCode triggerTypeCode,
            Long couponId
    ) {
        return queryCouponGroupRepository.findCouponGroupAndLimitMeta(triggerTypeCode, couponId);
    }
}
