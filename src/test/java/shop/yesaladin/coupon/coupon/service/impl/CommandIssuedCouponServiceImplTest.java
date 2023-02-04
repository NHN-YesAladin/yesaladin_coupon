package shop.yesaladin.coupon.coupon.service.impl;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.config.IssuanceConfiguration;
import shop.yesaladin.coupon.coupon.domain.model.CouponGroup;
import shop.yesaladin.coupon.coupon.domain.model.CouponTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.RateCoupon;
import shop.yesaladin.coupon.coupon.domain.model.Trigger;
import shop.yesaladin.coupon.coupon.domain.repository.CommandIssuedCouponRepository;
import shop.yesaladin.coupon.coupon.domain.repository.InsertIssuedCouponRepository;
import shop.yesaladin.coupon.coupon.domain.repository.QueryCouponGroupRepository;
import shop.yesaladin.coupon.coupon.domain.repository.QueryTriggerRepository;
import shop.yesaladin.coupon.coupon.dto.CouponIssueRequestDto;
import shop.yesaladin.coupon.coupon.dto.CouponIssueResponseDto;

class CommandIssuedCouponServiceImplTest {

    private IssuanceConfiguration issuanceConfig;
    private CommandIssuedCouponRepository issuedCouponRepository;
    private InsertIssuedCouponRepository insertRepository;
    private QueryTriggerRepository queryTriggerRepository;
    private QueryCouponGroupRepository queryCouponGroupRepository;
    private CommandIssuedCouponServiceImpl service;
    private final Clock clock = Clock.fixed(
            Instant.parse("2023-01-01T00:00:00.00Z"),
            ZoneId.of("UTC")
    );

    @BeforeEach
    void setUp() {
        issuanceConfig = Mockito.mock(IssuanceConfiguration.class);
        issuedCouponRepository = Mockito.mock(CommandIssuedCouponRepository.class);
        insertRepository = Mockito.mock(InsertIssuedCouponRepository.class);
        queryTriggerRepository = Mockito.mock(QueryTriggerRepository.class);
        queryCouponGroupRepository = Mockito.mock(QueryCouponGroupRepository.class);
        service = new CommandIssuedCouponServiceImpl(
                issuanceConfig,
                issuedCouponRepository,
                queryTriggerRepository,
                queryCouponGroupRepository,
                insertRepository,
                clock
        );
    }

    @Test
    @DisplayName("수량 제한 쿠폰 발행에 성공한다.")
    void limitedCouponIssuanceSuccess() {
        // given
        CouponIssueRequestDto requestDto = new CouponIssueRequestDto(
                TriggerTypeCode.SIGN_UP.name(),
                1L,
                1000
        );
        RateCoupon coupon = RateCoupon.builder()
                .id(1L)
                .name("테스트용 쿠폰")
                .isUnlimited(false)
                .duration(10)
                .expirationDate(null)
                .createdDatetime(LocalDateTime.of(2023, 2, 2, 0, 0))
                .couponTypeCode(CouponTypeCode.FIXED_RATE)
                .minOrderAmount(1000)
                .maxDiscountAmount(10000)
                .discountRate(10)
                .canBeOverlapped(true)
                .build();
        CouponGroup couponGroup = CouponGroup.builder()
                .coupon(coupon)
                .triggerTypeCode(TriggerTypeCode.SIGN_UP)
                .groupCode("test-group")
                .build();

        Mockito.when(queryCouponGroupRepository.findCouponGroupByTriggerTypeAndCouponId(
                TriggerTypeCode.SIGN_UP,
                1L
        )).thenReturn(Optional.of(couponGroup));

        // when
        List<CouponIssueResponseDto> actual = service.issueCoupon(requestDto);

        // then
        Mockito.verify(queryCouponGroupRepository, Mockito.times(1))
                .findCouponGroupByTriggerTypeAndCouponId(TriggerTypeCode.SIGN_UP, 1L);
        Mockito.verify(insertRepository, Mockito.times(1))
                .insertIssuedCoupon(Mockito.argThat(arg -> arg.size() == 1000));
        Assertions.assertThat(actual).hasSize(1);
        Assertions.assertThat(actual.get(0).getCreatedCouponCodes()).hasSize(1000);
    }

    @Test
    @DisplayName("수량 무제한 쿠폰 발행에 성공한다.")
    void unlimitedCouponIssuanceSuccess() {
        // given
        CouponIssueRequestDto requestDto = new CouponIssueRequestDto(
                TriggerTypeCode.SIGN_UP.name(),
                1L,
                null
        );
        RateCoupon coupon = RateCoupon.builder()
                .id(1L)
                .name("테스트용 쿠폰")
                .isUnlimited(true)
                .duration(10)
                .expirationDate(null)
                .createdDatetime(LocalDateTime.of(2023, 2, 2, 0, 0))
                .couponTypeCode(CouponTypeCode.FIXED_RATE)
                .minOrderAmount(1000)
                .maxDiscountAmount(10000)
                .discountRate(10)
                .canBeOverlapped(true)
                .build();
        CouponGroup couponGroup = CouponGroup.builder()
                .coupon(coupon)
                .triggerTypeCode(TriggerTypeCode.SIGN_UP)
                .groupCode("test-group")
                .build();

        Mockito.when(queryCouponGroupRepository.findCouponGroupByTriggerTypeAndCouponId(
                TriggerTypeCode.SIGN_UP,
                1L
        )).thenReturn(Optional.of(couponGroup));
        Mockito.when(issuanceConfig.getUnlimitedCouponIssueSize()).thenReturn(100);

        // when
        List<CouponIssueResponseDto> actual = service.issueCoupon(requestDto);

        // then
        Mockito.verify(queryCouponGroupRepository, Mockito.times(1))
                .findCouponGroupByTriggerTypeAndCouponId(TriggerTypeCode.SIGN_UP, 1L);
        Mockito.verify(insertRepository, Mockito.times(1))
                .insertIssuedCoupon(Mockito.argThat(arg -> arg.size() == 100));
        Assertions.assertThat(actual).hasSize(1);
        Assertions.assertThat(actual.get(0).getCreatedCouponCodes()).hasSize(100);
    }

    @Test
    @DisplayName("트리거 타입 코드만으로 쿠폰 발행에 성공한다.")
    void couponIssuanceWithOnlyTriggerTypeCodeSuccess() {
        // given
        CouponIssueRequestDto requestDto = new CouponIssueRequestDto(
                TriggerTypeCode.SIGN_UP.name(),
                null,
                1000
        );
        RateCoupon coupon = RateCoupon.builder()
                .id(1L)
                .name("테스트용 쿠폰")
                .isUnlimited(false)
                .duration(10)
                .expirationDate(null)
                .createdDatetime(LocalDateTime.of(2023, 2, 2, 0, 0))
                .couponTypeCode(CouponTypeCode.FIXED_RATE)
                .minOrderAmount(1000)
                .maxDiscountAmount(10000)
                .discountRate(10)
                .canBeOverlapped(true)
                .build();
        Trigger trigger = Trigger.builder()
                .triggerTypeCode(TriggerTypeCode.SIGN_UP)
                .coupon(coupon)
                .build();
        CouponGroup couponGroup = CouponGroup.builder()
                .coupon(coupon)
                .triggerTypeCode(TriggerTypeCode.SIGN_UP)
                .groupCode("test-group")
                .build();

        Mockito.when(queryTriggerRepository.findTriggerByTriggerTypeCode(TriggerTypeCode.SIGN_UP))
                .thenReturn(List.of(trigger));
        Mockito.when(queryCouponGroupRepository.findCouponGroupByTriggerTypeAndCouponId(
                trigger.getTriggerTypeCode(),
                1L
        )).thenReturn(Optional.of(couponGroup));

        // when
        List<CouponIssueResponseDto> actual = service.issueCoupon(requestDto);

        // then
        Mockito.verify(queryTriggerRepository, Mockito.times(1))
                .findTriggerByTriggerTypeCode(TriggerTypeCode.SIGN_UP);
        Mockito.verify(queryCouponGroupRepository, Mockito.times(1))
                .findCouponGroupByTriggerTypeAndCouponId(trigger.getTriggerTypeCode(), 1L);
        Mockito.verify(insertRepository, Mockito.times(1))
                .insertIssuedCoupon(Mockito.argThat(arg -> arg.size() == 1000));
        Assertions.assertThat(actual).hasSize(1);
        Assertions.assertThat(actual.get(0).getCreatedCouponCodes()).hasSize(1000);
    }

    @Test
    @DisplayName("자동 발행 쿠폰 발행에 성공한다.")
    void autoIssuanceCouponIssuanceSuccess() {
        // given
        CouponIssueRequestDto requestDto = new CouponIssueRequestDto(
                TriggerTypeCode.SIGN_UP.name(),
                null,
                1000
        );
        RateCoupon coupon = RateCoupon.builder()
                .id(1L)
                .name("테스트용 쿠폰")
                .isUnlimited(false)
                .duration(10)
                .expirationDate(null)
                .createdDatetime(LocalDateTime.of(2023, 2, 2, 0, 0))
                .couponTypeCode(CouponTypeCode.FIXED_RATE)
                .minOrderAmount(1000)
                .maxDiscountAmount(10000)
                .discountRate(10)
                .canBeOverlapped(true)
                .build();
        Trigger trigger = Trigger.builder()
                .triggerTypeCode(TriggerTypeCode.SIGN_UP)
                .coupon(coupon)
                .build();
        CouponGroup couponGroup = CouponGroup.builder()
                .coupon(coupon)
                .triggerTypeCode(TriggerTypeCode.SIGN_UP)
                .groupCode("test-group")
                .build();

        Mockito.when(queryTriggerRepository.findTriggerByTriggerTypeCode(TriggerTypeCode.SIGN_UP))
                .thenReturn(List.of(trigger));
        Mockito.when(queryCouponGroupRepository.findCouponGroupByTriggerTypeAndCouponId(
                trigger.getTriggerTypeCode(),
                1L
        )).thenReturn(Optional.of(couponGroup));

        // when
        List<CouponIssueResponseDto> actual = service.issueCoupon(requestDto);

        // then
        Mockito.verify(queryTriggerRepository, Mockito.times(1))
                .findTriggerByTriggerTypeCode(TriggerTypeCode.SIGN_UP);
        Mockito.verify(queryCouponGroupRepository, Mockito.times(1))
                .findCouponGroupByTriggerTypeAndCouponId(trigger.getTriggerTypeCode(), 1L);
        Mockito.verify(insertRepository, Mockito.times(1))
                .insertIssuedCoupon(Mockito.argThat(arg -> arg.get(0)
                        .getExpirationDate()
                        .equals(LocalDate.now(clock).plusDays(10))));
        Assertions.assertThat(actual).hasSize(1);
        Assertions.assertThat(actual.get(0).getCreatedCouponCodes()).hasSize(1000);
    }

    @Test
    @DisplayName("기간 필드에 값이 없어 자동 발행 쿠폰 발행에 실패한다.")
    void autoIssuanceCouponIssuanceFailCauseByDurationFieldIsNull() {
        // given
        CouponIssueRequestDto requestDto = new CouponIssueRequestDto(
                TriggerTypeCode.SIGN_UP.name(),
                null,
                1000
        );
        RateCoupon coupon = RateCoupon.builder()
                .id(1L)
                .name("테스트용 쿠폰")
                .isUnlimited(false)
                .duration(null)
                .expirationDate(null)
                .createdDatetime(LocalDateTime.of(2023, 2, 2, 0, 0))
                .couponTypeCode(CouponTypeCode.FIXED_RATE)
                .minOrderAmount(1000)
                .maxDiscountAmount(10000)
                .discountRate(10)
                .canBeOverlapped(true)
                .build();
        Trigger trigger = Trigger.builder()
                .triggerTypeCode(TriggerTypeCode.SIGN_UP)
                .coupon(coupon)
                .build();
        CouponGroup couponGroup = CouponGroup.builder()
                .coupon(coupon)
                .triggerTypeCode(TriggerTypeCode.SIGN_UP)
                .groupCode("test-group")
                .build();

        Mockito.when(queryTriggerRepository.findTriggerByTriggerTypeCode(TriggerTypeCode.SIGN_UP))
                .thenReturn(List.of(trigger));
        Mockito.when(queryCouponGroupRepository.findCouponGroupByTriggerTypeAndCouponId(
                trigger.getTriggerTypeCode(),
                1L
        )).thenReturn(Optional.of(couponGroup));

        // when
        // then
        Assertions.assertThatThrownBy(() -> service.issueCoupon(requestDto))
                .isInstanceOf(ClientException.class);
    }


    @Test
    @DisplayName("일반 발행 쿠폰 발행에 성공한다.")
    void NonAutoIssuanceCouponIssuanceSuccess() {
        // given
        CouponIssueRequestDto requestDto = new CouponIssueRequestDto(
                TriggerTypeCode.MEMBER_GRADE_GOLD.name(),
                null,
                1000
        );
        RateCoupon coupon = RateCoupon.builder()
                .id(1L)
                .name("테스트용 쿠폰")
                .isUnlimited(false)
                .duration(null)
                .expirationDate(LocalDate.now(clock))
                .createdDatetime(LocalDateTime.of(2023, 2, 2, 0, 0))
                .couponTypeCode(CouponTypeCode.FIXED_RATE)
                .minOrderAmount(1000)
                .maxDiscountAmount(10000)
                .discountRate(10)
                .canBeOverlapped(true)
                .build();
        Trigger trigger = Trigger.builder()
                .triggerTypeCode(TriggerTypeCode.MEMBER_GRADE_GOLD)
                .coupon(coupon)
                .build();
        CouponGroup couponGroup = CouponGroup.builder()
                .coupon(coupon)
                .triggerTypeCode(TriggerTypeCode.MEMBER_GRADE_GOLD)
                .groupCode("test-group")
                .build();

        Mockito.when(queryTriggerRepository.findTriggerByTriggerTypeCode(TriggerTypeCode.MEMBER_GRADE_GOLD))
                .thenReturn(List.of(trigger));
        Mockito.when(queryCouponGroupRepository.findCouponGroupByTriggerTypeAndCouponId(
                trigger.getTriggerTypeCode(),
                1L
        )).thenReturn(Optional.of(couponGroup));

        // when
        List<CouponIssueResponseDto> actual = service.issueCoupon(requestDto);

        // then
        Mockito.verify(queryTriggerRepository, Mockito.times(1))
                .findTriggerByTriggerTypeCode(TriggerTypeCode.MEMBER_GRADE_GOLD);
        Mockito.verify(queryCouponGroupRepository, Mockito.times(1))
                .findCouponGroupByTriggerTypeAndCouponId(trigger.getTriggerTypeCode(), 1L);
        Mockito.verify(insertRepository, Mockito.times(1))
                .insertIssuedCoupon(Mockito.argThat(arg -> arg.get(0)
                        .getExpirationDate()
                        .equals(LocalDate.now(clock))));
        Assertions.assertThat(actual).hasSize(1);
        Assertions.assertThat(actual.get(0).getCreatedCouponCodes()).hasSize(1000);
    }

    @Test
    @DisplayName("기간 필드에 값이 없어 일반 발행 쿠폰 발행에 실패한다.")
    void nonAutoIssuanceCouponIssuanceFailCauseByDurationFieldIsNull() {
        // given
        CouponIssueRequestDto requestDto = new CouponIssueRequestDto(
                TriggerTypeCode.MEMBER_GRADE_BRONZE.name(),
                null,
                1000
        );
        RateCoupon coupon = RateCoupon.builder()
                .id(1L)
                .name("테스트용 쿠폰")
                .isUnlimited(false)
                .duration(null)
                .expirationDate(null)
                .createdDatetime(null)
                .couponTypeCode(CouponTypeCode.FIXED_RATE)
                .minOrderAmount(1000)
                .maxDiscountAmount(10000)
                .discountRate(10)
                .canBeOverlapped(true)
                .build();
        Trigger trigger = Trigger.builder()
                .triggerTypeCode(TriggerTypeCode.MEMBER_GRADE_BRONZE)
                .coupon(coupon)
                .build();
        CouponGroup couponGroup = CouponGroup.builder()
                .coupon(coupon)
                .triggerTypeCode(TriggerTypeCode.MEMBER_GRADE_BRONZE)
                .groupCode("test-group")
                .build();

        Mockito.when(queryTriggerRepository.findTriggerByTriggerTypeCode(TriggerTypeCode.MEMBER_GRADE_BRONZE))
                .thenReturn(List.of(trigger));
        Mockito.when(queryCouponGroupRepository.findCouponGroupByTriggerTypeAndCouponId(
                trigger.getTriggerTypeCode(),
                1L
        )).thenReturn(Optional.of(couponGroup));

        // when
        // then
        Assertions.assertThatThrownBy(() -> service.issueCoupon(requestDto))
                .isInstanceOf(ClientException.class);
    }

}