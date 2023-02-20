package shop.yesaladin.coupon.coupon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.persistence.JpaCommandTriggerRepository;
import shop.yesaladin.coupon.coupon.service.inter.CommandTriggerService;

@RequiredArgsConstructor
@Service
public class CommandTriggerServiceImpl implements CommandTriggerService {

    private final JpaCommandTriggerRepository commandTriggerRepository;

    @Override
    @Transactional
    public void stopIssueCoupon(TriggerTypeCode triggerTypeCode, long couponId) {
        commandTriggerRepository.deleteByTriggerTypeCodeAndCoupon_Id(triggerTypeCode, couponId);
    }
}
