package shop.yesaladin.coupon.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.domain.model.CouponTypeCode;
import shop.yesaladin.coupon.domain.model.RateCoupon;
import shop.yesaladin.coupon.domain.repository.CommandCouponBoundRepository;
import shop.yesaladin.coupon.domain.repository.CommandCouponRepository;
import shop.yesaladin.coupon.domain.repository.CommandTriggerRepository;
import shop.yesaladin.coupon.service.inter.CommandCouponService;
import shop.yesaladin.coupon.service.inter.CommandIssueCouponService;

class CommandCouponServiceImplTest {

    private CommandCouponRepository commandCouponRepository;
    private CommandCouponBoundRepository commandCouponBoundRepository;
    private CommandTriggerRepository commandTriggerRepository;
    private CommandIssueCouponService commandIssueCouponService;
    private CommandCouponService commandCouponService;
    private Coupon coupon;

    @BeforeEach
    void setUp() {
        commandCouponRepository = Mockito.mock(CommandCouponRepository.class);
        commandCouponBoundRepository = Mockito.mock(CommandCouponBoundRepository.class);
        commandTriggerRepository = Mockito.mock(CommandTriggerRepository.class);
        commandIssueCouponService = Mockito.mock(CommandIssueCouponService.class);

        commandCouponService = new CommandCouponServiceImpl(
                commandCouponRepository,
                commandCouponBoundRepository,
                commandTriggerRepository,
                commandIssueCouponService
        );
    }

    // TODO
    @Test
    @DisplayName("무제한 쿠폰 생성 후 자동 발행 성공")
    void createPointCoupon() {
        // when
        coupon = RateCoupon.builder()
                .name("10% 할인 쿠폰")
                .isUnlimited(true)
                .couponTypeCode(CouponTypeCode.FIXED_RATE)
                .minOrderAmount(10000)
                .maxDiscountAmount(1000)
                .discountRate(10)
                .build();

        // 어떤 메소드들이 호출되는지, 어떤 매개 변수로 동작되는지 테스트, 횟수 등?
        // 리턴 타입 확인

    }

}