package shop.yesaladin.coupon.coupon.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.coupon.code.CouponBoundCode;
import shop.yesaladin.coupon.code.CouponTypeCode;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.config.StorageConfiguration;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.RateCoupon;
import shop.yesaladin.coupon.coupon.domain.repository.CommandCouponBoundRepository;
import shop.yesaladin.coupon.coupon.domain.repository.CommandCouponGroupRepository;
import shop.yesaladin.coupon.coupon.domain.repository.CommandCouponRepository;
import shop.yesaladin.coupon.coupon.domain.repository.CommandTriggerRepository;
import shop.yesaladin.coupon.coupon.dto.CouponResponseDto;
import shop.yesaladin.coupon.coupon.dto.RateCouponRequestDto;
import shop.yesaladin.coupon.coupon.service.inter.CommandIssuedCouponService;
import shop.yesaladin.coupon.file.service.inter.ObjectStorageService;

class CommandCouponServiceImplTest {

    private CommandCouponRepository couponRepository;
    private CommandCouponBoundRepository couponBoundRepository;
    private CommandTriggerRepository triggerRepository;
    private CommandCouponGroupRepository couponGroupRepository;
    private CommandIssuedCouponService issueCouponService;
    private CommandCouponServiceImpl couponService;
    private ObjectStorageService objectStorageService;
    private StorageConfiguration storageConfiguration;
    private Coupon coupon;

    @BeforeEach
    void setUp() {
        couponRepository = Mockito.mock(CommandCouponRepository.class);
        couponBoundRepository = Mockito.mock(CommandCouponBoundRepository.class);
        triggerRepository = Mockito.mock(CommandTriggerRepository.class);
        couponGroupRepository = Mockito.mock(CommandCouponGroupRepository.class);
        issueCouponService = Mockito.mock(CommandIssuedCouponService.class);
        objectStorageService = Mockito.mock(ObjectStorageService.class);

        couponService = new CommandCouponServiceImpl(
                couponRepository,
                couponBoundRepository,
                triggerRepository,
                couponGroupRepository,
                issueCouponService,
                objectStorageService,
                storageConfiguration
        );
    }

    @Test
    @DisplayName("할인 쿠폰 생성 - 쿠폰, 트리거, 쿠폰 적용 범위 테이블이 등록 성공")
    void createPointCoupon() {
        // given
        String couponName = "10% 할인 쿠폰";
        coupon = RateCoupon.builder()
                .id(1L)
                .name(couponName)
                .isUnlimited(true)
                .couponTypeCode(CouponTypeCode.FIXED_RATE)
                .minOrderAmount(10000)
                .maxDiscountAmount(1000)
                .discountRate(10)
                .build();

        RateCouponRequestDto requestDto = new RateCouponRequestDto(
                TriggerTypeCode.SIGN_UP,
                couponName,
                true,
                null,
                null,
                null,
                10,
                null,
                CouponTypeCode.FIXED_RATE,
                10000,
                2000,
                10,
                false,
                CouponBoundCode.ALL,
                null,
                null
        );

        // when
        Mockito.when(couponRepository.save(any())).thenReturn(coupon);
        CouponResponseDto couponResponseDto = couponService.createRateCoupon(
                requestDto);

        // then
        Assertions.assertThat(couponResponseDto.getName()).isEqualTo(couponName);
        Assertions.assertThat(couponResponseDto.getCouponTypeCode())
                .isEqualTo(CouponTypeCode.FIXED_RATE);
        verify(couponRepository).save(any());
        verify(triggerRepository).save(any());
        verify(couponGroupRepository).save(any());
        // 생일쿠폰이므로 생성시 쿠폰 발행이 동작하지 않음
        verify(issueCouponService, times(0)).issueCoupon(any());
        verify(couponBoundRepository).save(any());
    }
}