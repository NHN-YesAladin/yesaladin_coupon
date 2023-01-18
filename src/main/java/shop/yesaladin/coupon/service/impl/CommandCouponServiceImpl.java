package shop.yesaladin.coupon.service.impl;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.domain.model.CouponBound;
import shop.yesaladin.coupon.domain.repository.CommandCouponBoundRepository;
import shop.yesaladin.coupon.domain.repository.CommandCouponRepository;
import shop.yesaladin.coupon.domain.repository.CommandTriggerRepository;
import shop.yesaladin.coupon.dto.CouponRequestDto;
import shop.yesaladin.coupon.dto.CouponResponseDto;
import shop.yesaladin.coupon.service.inter.CommandCouponService;

@RequiredArgsConstructor
@Service
public class CommandCouponServiceImpl implements CommandCouponService {
    private final CommandCouponRepository couponRepository;
    private final CommandCouponBoundRepository couponBoundRepository;
    private final CommandTriggerRepository triggerRepository;


}
