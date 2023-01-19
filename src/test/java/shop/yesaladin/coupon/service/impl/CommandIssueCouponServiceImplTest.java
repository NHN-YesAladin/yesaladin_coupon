package shop.yesaladin.coupon.service.impl;

import java.lang.reflect.Field;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import shop.yesaladin.coupon.config.IssuanceConfiguration;
import shop.yesaladin.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.domain.model.CouponTypeCode;
import shop.yesaladin.coupon.domain.model.PointCoupon;
import shop.yesaladin.coupon.domain.model.Trigger;
import shop.yesaladin.coupon.domain.model.TriggerTypeCode;
import shop.yesaladin.coupon.domain.repository.InsertIssuedCouponRepository;
import shop.yesaladin.coupon.domain.repository.QueryCouponRepository;
import shop.yesaladin.coupon.dto.CouponIssueRequestDto;
import shop.yesaladin.coupon.dto.CouponIssueResponseDto;
import shop.yesaladin.coupon.dto.IssuedCouponInsertDto;
import shop.yesaladin.coupon.exception.CouponNotFoundException;
import shop.yesaladin.coupon.exception.InvalidCouponDataException;

class CommandIssueCouponServiceImplTest {

    private IssuanceConfiguration issuanceConfig;
    private QueryCouponRepository queryCouponRepository;
    private InsertIssuedCouponRepository insertRepository;
    private CommandIssueCouponServiceImpl service;
    private final Clock clock = Clock.fixed(
            Instant.parse("2023-01-01T00:00:00.00Z"),
            ZoneId.of("UTC")
    );

    @BeforeEach
    void setUp() {
        issuanceConfig = Mockito.mock(IssuanceConfiguration.class);
        queryCouponRepository = Mockito.mock(QueryCouponRepository.class);
        insertRepository = Mockito.mock(InsertIssuedCouponRepository.class);
        service = new CommandIssueCouponServiceImpl(
                issuanceConfig,
                queryCouponRepository,
                insertRepository,
                clock
        );
    }

    @Test
    @DisplayName("수량 제한 쿠폰 발행에 성공한다.")
    void issueLimitedCouponSuccessTest() throws NoSuchFieldException, IllegalAccessException {
        // given
        long couponId = 1L;
        Coupon coupon = PointCoupon.builder()
                .id(couponId)
                .name("test coupon")
                .isUnlimited(false)
                .chargePointAmount(1000)
                .expirationDate(LocalDate.of(2023, 1, 4))
                .couponTypeCode(CouponTypeCode.POINT)
                .triggerList(Collections.emptyList())
                .build();

        CouponIssueRequestDto requestDto = ReflectionUtils.newInstance(CouponIssueRequestDto.class);
        Field couponIdField = requestDto.getClass().getDeclaredField("couponId");
        Field quantityField = requestDto.getClass().getDeclaredField("quantity");
        couponIdField.setAccessible(true);
        quantityField.setAccessible(true);
        couponIdField.set(requestDto, couponId);
        quantityField.set(requestDto, 500);

        Mockito.when(queryCouponRepository.findCouponById(couponId))
                .thenReturn(Optional.of(coupon));

        // when
        CouponIssueResponseDto actual = service.issueCoupon(requestDto);

        // then
        ArgumentCaptor<List<IssuedCouponInsertDto>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        Assertions.assertThat(actual.getCreatedCouponCodes()).hasSize(500);
        Mockito.verify(insertRepository, Mockito.times(1))
                .insertIssuedCoupon(argumentCaptor.capture());
        List<IssuedCouponInsertDto> actualArgs = argumentCaptor.getValue();
        Assertions.assertThat(actualArgs).hasSize(500);
    }

    @Test
    @DisplayName("수량 무제한 쿠폰 발헹에 성공한다.")
    void issueUnlimitedCouponSuccessTest() throws NoSuchFieldException, IllegalAccessException {
        // given
        long couponId = 1L;
        Coupon coupon = PointCoupon.builder()
                .id(couponId)
                .name("test coupon")
                .isUnlimited(true)
                .chargePointAmount(1000)
                .expirationDate(LocalDate.of(2023, 1, 4))
                .couponTypeCode(CouponTypeCode.POINT)
                .triggerList(Collections.emptyList())
                .build();

        CouponIssueRequestDto requestDto = ReflectionUtils.newInstance(CouponIssueRequestDto.class);
        Field couponIdField = requestDto.getClass().getDeclaredField("couponId");
        couponIdField.setAccessible(true);
        couponIdField.set(requestDto, couponId);

        Mockito.when(queryCouponRepository.findCouponById(couponId))
                .thenReturn(Optional.of(coupon));
        Mockito.when(issuanceConfig.getUnlimitedCouponIssueSize()).thenReturn(100);
        Mockito.when(issuanceConfig.getUnlimitedFlag()).thenReturn(-1);

        // when
        CouponIssueResponseDto actual = service.issueCoupon(requestDto);

        // then
        ArgumentCaptor<List<IssuedCouponInsertDto>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        Assertions.assertThat(actual.getCreatedCouponCodes()).hasSize(100);
        Mockito.verify(insertRepository, Mockito.times(1))
                .insertIssuedCoupon(argumentCaptor.capture());
        List<IssuedCouponInsertDto> actualArgs = argumentCaptor.getValue();
        Assertions.assertThat(actualArgs).hasSize(100);
    }

    @Test
    @DisplayName("쿠폰 데이터가 없어 쿠폰 발행에 실패한다")
    void issueCouponFailCauseByCouponNotFound()
            throws NoSuchFieldException, IllegalAccessException {
        // given
        long couponId = 1L;
        CouponIssueRequestDto requestDto = ReflectionUtils.newInstance(CouponIssueRequestDto.class);
        Field couponIdField = requestDto.getClass().getDeclaredField("couponId");
        couponIdField.setAccessible(true);
        couponIdField.set(requestDto, couponId);

        Mockito.when(queryCouponRepository.findCouponById(couponId)).thenReturn(Optional.empty());

        // when
        // then
        Assertions.assertThatThrownBy(() -> service.issueCoupon(requestDto))
                .isInstanceOf(CouponNotFoundException.class);
    }

    @Test
    @DisplayName("기간 필드에 값이 없어 자동 지급 쿠폰 발행에 실패한다.")
    void issueAutoIssuanceCouponFailCauseByDurationIsNull()
            throws NoSuchFieldException, IllegalAccessException {
        // given
        long couponId = 1L;
        Coupon coupon = PointCoupon.builder()
                .id(couponId)
                .name("test coupon")
                .chargePointAmount(1000)
                .expirationDate(LocalDate.of(2023, 1, 4))
                .isUnlimited(false)
                .couponTypeCode(CouponTypeCode.POINT)
                .triggerList(List.of(Trigger.builder()
                        .triggerTypeCode(TriggerTypeCode.BIRTHDAY)
                        .build()))
                .build();

        CouponIssueRequestDto requestDto = ReflectionUtils.newInstance(CouponIssueRequestDto.class);
        Field couponIdField = requestDto.getClass().getDeclaredField("couponId");
        Field quantityField = requestDto.getClass().getDeclaredField("quantity");
        couponIdField.setAccessible(true);
        quantityField.setAccessible(true);
        couponIdField.set(requestDto, couponId);
        quantityField.set(requestDto, 500);

        Mockito.when(queryCouponRepository.findCouponById(couponId))
                .thenReturn(Optional.of(coupon));

        // when
        // then
        Assertions.assertThatThrownBy(() -> service.issueCoupon(requestDto))
                .isInstanceOf(InvalidCouponDataException.class);
    }

    @Test
    @DisplayName("만료일 필드에 값이 없어 유저 다운로드 쿠폰 발행에 실패한다.")
    void issueUserDownloadCouponFailCauseByExpirationDateIsNull()
            throws NoSuchFieldException, IllegalAccessException {
        // given
        long couponId = 1L;
        Coupon coupon = PointCoupon.builder()
                .id(couponId)
                .name("test coupon")
                .chargePointAmount(1000)
                .isUnlimited(false)
                .duration(10)
                .couponTypeCode(CouponTypeCode.POINT)
                .triggerList(Collections.emptyList())
                .build();

        CouponIssueRequestDto requestDto = ReflectionUtils.newInstance(CouponIssueRequestDto.class);
        Field couponIdField = requestDto.getClass().getDeclaredField("couponId");
        Field quantityField = requestDto.getClass().getDeclaredField("quantity");
        couponIdField.setAccessible(true);
        quantityField.setAccessible(true);
        couponIdField.set(requestDto, couponId);
        quantityField.set(requestDto, 500);

        Mockito.when(queryCouponRepository.findCouponById(couponId))
                .thenReturn(Optional.of(coupon));

        // when
        // then
        Assertions.assertThatThrownBy(() -> service.issueCoupon(requestDto))
                .isInstanceOf(InvalidCouponDataException.class);
    }

}