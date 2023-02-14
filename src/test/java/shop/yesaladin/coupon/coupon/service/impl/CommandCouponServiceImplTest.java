package shop.yesaladin.coupon.coupon.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import shop.yesaladin.coupon.code.CouponBoundCode;
import shop.yesaladin.coupon.code.CouponTypeCode;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.config.StorageConfiguration;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.RateCoupon;
import shop.yesaladin.coupon.coupon.domain.repository.CommandCouponBoundRepository;
import shop.yesaladin.coupon.coupon.domain.repository.CommandCouponGroupRepository;
import shop.yesaladin.coupon.coupon.domain.repository.CommandCouponOfTheMonthPolicyRepository;
import shop.yesaladin.coupon.coupon.domain.repository.CommandCouponRepository;
import shop.yesaladin.coupon.coupon.domain.repository.CommandTriggerRepository;
import shop.yesaladin.coupon.coupon.dto.CouponResponseDto;
import shop.yesaladin.coupon.coupon.dto.RateCouponRequestDto;
import shop.yesaladin.coupon.coupon.dummy.CouponDummy;
import shop.yesaladin.coupon.coupon.service.inter.CommandIssuedCouponService;
import shop.yesaladin.coupon.file.service.inter.ObjectStorageService;
import shop.yesaladin.coupon.scheduler.CouponOfTheCouponScheduler;

@SuppressWarnings("unchecked")
class CommandCouponServiceImplTest {

    private CommandCouponOfTheMonthPolicyRepository couponOfTheMonthPolicyRepository;
    private CommandCouponRepository couponRepository;
    private CommandCouponBoundRepository couponBoundRepository;
    private CommandTriggerRepository triggerRepository;
    private CommandCouponGroupRepository couponGroupRepository;
    private CommandIssuedCouponService issueCouponService;
    private CommandCouponServiceImpl couponService;
    private ObjectStorageService objectStorageService;
    private StorageConfiguration storageConfiguration;
    private CouponOfTheCouponScheduler couponOfTheCouponScheduler;
    private RedisTemplate<String, String> redisTemplate;
    private HashOperations hashOperations;
    private Coupon coupon;

    @BeforeEach
    void setUp() {
        couponOfTheMonthPolicyRepository = Mockito.mock(CommandCouponOfTheMonthPolicyRepository.class);
        couponRepository = Mockito.mock(CommandCouponRepository.class);
        couponBoundRepository = Mockito.mock(CommandCouponBoundRepository.class);
        triggerRepository = Mockito.mock(CommandTriggerRepository.class);
        couponGroupRepository = Mockito.mock(CommandCouponGroupRepository.class);
        issueCouponService = Mockito.mock(CommandIssuedCouponService.class);
        objectStorageService = Mockito.mock(ObjectStorageService.class);
        couponOfTheCouponScheduler = Mockito.mock(CouponOfTheCouponScheduler.class);
        redisTemplate = Mockito.mock(RedisTemplate.class);
        hashOperations = Mockito.mock(HashOperations.class);
        Mockito.when(redisTemplate.opsForValue()).thenReturn(Mockito.mock(ValueOperations.class));

        couponService = new CommandCouponServiceImpl(
                couponOfTheMonthPolicyRepository,
                couponRepository,
                couponBoundRepository,
                triggerRepository,
                couponGroupRepository,
                issueCouponService,
                objectStorageService,
                storageConfiguration,
                couponOfTheCouponScheduler,
                redisTemplate
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

    @Test
    @DisplayName("이달의 쿠폰 생성시 이달의 쿠폰 정책과 함께 생성된다.")
    void createCouponOfTheMonthTest() {
        // given
        RateCoupon coupon = (RateCoupon) CouponDummy.dummyRateCouponWithId(1L);
        RateCouponRequestDto requestDto = new RateCouponRequestDto(
                TriggerTypeCode.COUPON_OF_THE_MONTH,
                coupon.getName(),
                coupon.isUnlimited(),
                100,
                1,
                LocalTime.of(0, 0),
                null,
                null,
                null,
                LocalDate.of(2023, 2, 10),
                coupon.getCouponTypeCode(),
                coupon.getMinOrderAmount(),
                coupon.getMaxDiscountAmount(),
                coupon.getDiscountRate(),
                coupon.isCanBeOverlapped(),
                CouponBoundCode.ALL,
                null,
                null
        );
        Mockito.when(couponRepository.save(any())).thenReturn(coupon);
        Mockito.when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        Mockito.doNothing().when(hashOperations).put(any(), any(), any());

        // when
        CouponResponseDto actual = couponService.createRateCoupon(requestDto);

        // then
        verify(couponRepository).save(any());
        verify(triggerRepository).save(any());
        verify(couponGroupRepository).save(any());
        verify(couponBoundRepository).save(any());
        verify(couponOfTheMonthPolicyRepository).save(argThat(arg -> arg.getOpenDate() == 1
                && arg.getOpenTime().equals(LocalTime.of(0, 0))));

        Assertions.assertThat(actual.getName()).isEqualTo(coupon.getName());
        Assertions.assertThat(actual.getCouponTypeCode()).isEqualTo(CouponTypeCode.FIXED_RATE);
    }
}