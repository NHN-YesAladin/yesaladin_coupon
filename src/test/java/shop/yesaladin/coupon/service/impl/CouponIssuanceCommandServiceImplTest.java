package shop.yesaladin.coupon.service.impl;

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
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import shop.yesaladin.coupon.config.IssuanceConfiguration;
import shop.yesaladin.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.domain.model.CouponCode;
import shop.yesaladin.coupon.domain.repository.CouponIssuanceInsertRepository;
import shop.yesaladin.coupon.domain.repository.CouponQueryRepository;
import shop.yesaladin.coupon.dto.CouponIssuanceInsertDto;
import shop.yesaladin.coupon.exception.CouponNotFoundException;
import shop.yesaladin.coupon.exception.InvalidCouponDataException;

class CouponIssuanceCommandServiceImplTest {

    private IssuanceConfiguration issuanceConfig;
    private CouponQueryRepository couponQueryRepository;
    private CouponIssuanceInsertRepository insertRepository;
    private CouponIssuanceCommandServiceImpl service;
    private final Clock clock = Clock.fixed(
            Instant.parse("2023-01-01T00:00:00.00Z"),
            ZoneId.of("UTC")
    );

    @BeforeEach
    void setUp() {
        issuanceConfig = Mockito.mock(IssuanceConfiguration.class);
        couponQueryRepository = Mockito.mock(CouponQueryRepository.class);
        insertRepository = Mockito.mock(CouponIssuanceInsertRepository.class);
        service = new CouponIssuanceCommandServiceImpl(
                issuanceConfig,
                couponQueryRepository,
                insertRepository,
                clock
        );
    }

    @Test
    @DisplayName("수량 제한 쿠폰 발행에 성공한다.")
    void issueLimitedCouponSuccessTest() {
        // given
        long couponId = 1L;
        Coupon coupon = Coupon.builder()
                .id(couponId)
                .name("test coupon")
                .quantity(500)
                .minOrderAmount(1000)
                .maxDiscountAmount(1000)
                .discountAmount(1000)
                .canBeOverlapped(false)
                .openDatetime(LocalDateTime.of(2023, 1, 1, 0, 0))
                .expirationDate(LocalDate.of(2023, 1, 4))
                .couponTypeCode(CouponCode.POINT)
                .issuanceCode(CouponCode.USER_DOWNLOAD)
                .build();
        Mockito.when(couponQueryRepository.findCouponById(couponId))
                .thenReturn(Optional.of(coupon));

        // when
        List<String> actual = service.issueCoupon(couponId);

        // then
        ArgumentCaptor<List<CouponIssuanceInsertDto>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        Assertions.assertThat(actual).hasSize(coupon.getQuantity());
        Mockito.verify(insertRepository, Mockito.times(1))
                .insertCouponIssuance(argumentCaptor.capture());
        List<CouponIssuanceInsertDto> actualArgs = argumentCaptor.getValue();
        Assertions.assertThat(actualArgs).hasSize(500);
    }

    @Test
    @DisplayName("수량 무제한 쿠폰 발헹에 성공한다.")
    void issueUnlimitedCouponSuccessTest() {
        // given
        long couponId = 1L;
        Coupon coupon = Coupon.builder()
                .id(couponId)
                .name("test coupon")
                .quantity(-1)
                .minOrderAmount(1000)
                .maxDiscountAmount(1000)
                .discountAmount(1000)
                .canBeOverlapped(false)
                .openDatetime(LocalDateTime.of(2023, 1, 1, 0, 0))
                .expirationDate(LocalDate.of(2023, 1, 4))
                .couponTypeCode(CouponCode.POINT)
                .issuanceCode(CouponCode.USER_DOWNLOAD)
                .build();
        Mockito.when(couponQueryRepository.findCouponById(couponId))
                .thenReturn(Optional.of(coupon));
        Mockito.when(issuanceConfig.getUnlimitedCouponIssueSize()).thenReturn(100);
        Mockito.when(issuanceConfig.getUnlimitedFlag()).thenReturn(-1);

        // when
        List<String> actual = service.issueCoupon(couponId);

        // then
        ArgumentCaptor<List<CouponIssuanceInsertDto>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        Assertions.assertThat(actual).hasSize(100);
        Mockito.verify(insertRepository, Mockito.times(1))
                .insertCouponIssuance(argumentCaptor.capture());
        List<CouponIssuanceInsertDto> actualArgs = argumentCaptor.getValue();
        Assertions.assertThat(actualArgs).hasSize(100);
    }

    @Test
    @DisplayName("쿠폰 데이터가 없어 쿠폰 발행에 실패한다")
    void issueCouponFailCauseByCouponNotFound() {
        // given
        long couponId = 1L;
        Mockito.when(couponQueryRepository.findCouponById(couponId)).thenReturn(Optional.empty());

        // when
        // then
        Assertions.assertThatThrownBy(() -> service.issueCoupon(couponId))
                .isInstanceOf(CouponNotFoundException.class);
    }

    @Test
    @DisplayName("기간 필드에 값이 없어 자동 지급 쿠폰 발행에 실패한다.")
    void issueAutoIssuanceCouponFailCauseByDurationIsNull() {
        // given
        long couponId = 1L;
        Coupon coupon = Coupon.builder()
                .id(couponId)
                .name("test coupon")
                .quantity(500)
                .minOrderAmount(1000)
                .maxDiscountAmount(1000)
                .discountAmount(1000)
                .canBeOverlapped(false)
                .openDatetime(LocalDateTime.of(2023, 1, 1, 0, 0))
                .couponTypeCode(CouponCode.POINT)
                .issuanceCode(CouponCode.AUTO_ISSUANCE)
                .build();
        Mockito.when(couponQueryRepository.findCouponById(couponId))
                .thenReturn(Optional.of(coupon));

        // when
        // then
        Assertions.assertThatThrownBy(() -> service.issueCoupon(couponId))
                .isInstanceOf(InvalidCouponDataException.class);
    }

    @Test
    @DisplayName("만료일 필드에 값이 없어 유저 다운로드 쿠폰 발행에 실패한다.")
    void issueUserDownloadCouponFailCauseByExpirationDateIsNull() {
        // given
        long couponId = 1L;
        Coupon coupon = Coupon.builder()
                .id(couponId)
                .name("test coupon")
                .quantity(500)
                .minOrderAmount(1000)
                .maxDiscountAmount(1000)
                .discountAmount(1000)
                .canBeOverlapped(false)
                .openDatetime(LocalDateTime.of(2023, 1, 1, 0, 0))
                .couponTypeCode(CouponCode.POINT)
                .issuanceCode(CouponCode.USER_DOWNLOAD)
                .build();
        Mockito.when(couponQueryRepository.findCouponById(couponId))
                .thenReturn(Optional.of(coupon));

        // when
        // then
        Assertions.assertThatThrownBy(() -> service.issueCoupon(couponId))
                .isInstanceOf(InvalidCouponDataException.class);
    }

}