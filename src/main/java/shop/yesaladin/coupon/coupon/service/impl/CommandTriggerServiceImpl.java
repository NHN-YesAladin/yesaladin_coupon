package shop.yesaladin.coupon.coupon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.domain.repository.CommandTriggerRepository;
import shop.yesaladin.coupon.coupon.service.inter.CommandTriggerService;

/**
 * CommandTriggerService 의 구현체 클래스입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class CommandTriggerServiceImpl implements CommandTriggerService {

    private final CommandTriggerRepository commandTriggerRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void stopIssueCoupon(TriggerTypeCode triggerTypeCode, long couponId) {
        commandTriggerRepository.deleteByTriggerTypeCodeAndCouponId(triggerTypeCode, couponId);
    }
}
