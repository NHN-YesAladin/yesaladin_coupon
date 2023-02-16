package shop.yesaladin.coupon.coupon.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.CouponGivenStateCode;
import shop.yesaladin.coupon.coupon.domain.model.CouponGroup;
import shop.yesaladin.coupon.coupon.domain.model.CouponUsageStateCode;
import shop.yesaladin.coupon.coupon.domain.model.IssuedCoupon;
import shop.yesaladin.coupon.coupon.domain.repository.QueryCouponGroupRepository;
import shop.yesaladin.coupon.coupon.domain.repository.QueryIssuedCouponRepository;
import shop.yesaladin.coupon.coupon.dto.CouponIssueRequestDto;
import shop.yesaladin.coupon.coupon.dto.CouponIssueResponseDto;
import shop.yesaladin.coupon.coupon.dummy.CouponDummy;
import shop.yesaladin.coupon.coupon.service.inter.CommandIssuedCouponService;
import shop.yesaladin.coupon.coupon.service.inter.QueryIssuedCouponService;

class QueryIssuedCouponServiceImplTest {

    private QueryIssuedCouponService service;
    private QueryCouponGroupRepository queryCouponGroupRepository;
    private QueryIssuedCouponRepository queryIssuedCouponRepository;
    private CommandIssuedCouponService commandIssuedCouponService;
    private CouponIssueRequestDto couponIssueRequestDto;
    private List<String> createdCouponCodeList;
    private String couponGroupCode;

    @BeforeEach
    void setUp() {
        queryCouponGroupRepository = Mockito.mock(QueryCouponGroupRepository.class);
        queryIssuedCouponRepository = Mockito.mock(QueryIssuedCouponRepository.class);
        commandIssuedCouponService = Mockito.mock(CommandIssuedCouponService.class);
        service = new QueryIssuedCouponServiceImpl(
                queryCouponGroupRepository,
                queryIssuedCouponRepository,
                commandIssuedCouponService
        );

        couponIssueRequestDto = CouponIssueRequestDto.builder()
                .triggerTypeCode(TriggerTypeCode.SIGN_UP.toString())
                .quantity(1)
                .build();

        createdCouponCodeList = List.of(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        couponGroupCode = UUID.randomUUID().toString();
        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
    }

    @Test
    @DisplayName("회원가입 쿠폰발행요청 응답 성공")
    void getCouponIssueResponseDtoList_signUp_SuccessTest() {
        // given
        CouponIssueResponseDto responseDto = CouponIssueResponseDto.builder()
                .createdCouponCodes(createdCouponCodeList)
                .couponGroupCode(couponGroupCode)
                .build();
        when(commandIssuedCouponService.issueCoupon(any())).thenReturn(List.of(responseDto));

        // when
        List<CouponIssueResponseDto> result = service.getCouponIssueResponseDtoList(
                couponIssueRequestDto);

        // then
        verify(commandIssuedCouponService, times(1)).issueCoupon(any());
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCreatedCouponCodes()).isEqualTo(createdCouponCodeList);
        assertThat(result.get(0).getCouponGroupCode()).isEqualTo(couponGroupCode);
    }

    @Test
    @DisplayName("일반 쿠폰발행요청 응답 성공")
    void getCouponIssueResponseDtoList_SuccessTest() {
        // given
        TriggerTypeCode triggerTypeCode = TriggerTypeCode.MEMBER_GRADE_GOLD;
        long couponId = 6L;
        Coupon coupon = CouponDummy.dummyRateCouponWithId(couponId);

        CouponIssueRequestDto couponIssueRequestDto2 = CouponIssueRequestDto.builder()
                .triggerTypeCode(triggerTypeCode.toString())
                .quantity(1)
                .build();

        Optional<CouponGroup> couponGroupOptional = Optional.of(CouponGroup.builder()
                .id(1L)
                .triggerTypeCode(triggerTypeCode)
                .coupon(coupon)
                .groupCode(couponGroupCode)
                .build());

        String issuedCouponCode = UUID.randomUUID().toString();

        Optional<IssuedCoupon> issuedCouponOptional = Optional.ofNullable(IssuedCoupon.builder()
                .id(100L)
                .couponCode(issuedCouponCode)
                .createdDatetime(
                        LocalDateTime.now())
                .expirationDate(LocalDate.of(2023, 9, 10))
                .couponGroup(couponGroupOptional.get())
                .couponGivenStateCode(CouponGivenStateCode.NOT_GIVEN)
                .couponUsageStateCode(CouponUsageStateCode.NOT_USED)
                .build());

        // when
        when(queryCouponGroupRepository.findCouponGroupByTriggerType(any())).thenReturn(
                couponGroupOptional);
        when(queryIssuedCouponRepository.findIssuedCouponByGroupCodeId(anyLong())).thenReturn(
                issuedCouponOptional);
        List<CouponIssueResponseDto> result = service.getCouponIssueResponseDtoList(
                couponIssueRequestDto2);

        // then
        verify(commandIssuedCouponService, times(0)).issueCoupon(any());
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCreatedCouponCodes()).contains(issuedCouponCode);
        assertThat(result.get(0).getCouponGroupCode()).isEqualTo(couponGroupCode);
    }

    @Test
    @DisplayName("무제한 쿠폰에 대해 발행된 쿠폰이 존재하지 않는 경우 발행하여 정상 응답")
    void getCouponIssueResponseDtoList_unlimited_SuccessTest() {
        TriggerTypeCode triggerTypeCode = TriggerTypeCode.MEMBER_GRADE_GOLD;
        Coupon coupon = CouponDummy.dummyRateCoupon();

        CouponIssueRequestDto couponIssueRequestDto2 = CouponIssueRequestDto.builder()
                .triggerTypeCode(triggerTypeCode.toString())
                .quantity(1)
                .build();

        Optional<CouponGroup> couponGroupOptional = Optional.of(CouponGroup.builder()
                .id(1L)
                .triggerTypeCode(triggerTypeCode)
                .coupon(coupon)
                .groupCode(couponGroupCode)
                .build());

        String issuedCouponCode = UUID.randomUUID().toString();

        Optional<IssuedCoupon> issuedCouponOptional = Optional.ofNullable(IssuedCoupon.builder()
                .id(100L)
                .couponCode(issuedCouponCode)
                .createdDatetime(
                        LocalDateTime.now())
                .expirationDate(LocalDate.of(2023, 9, 10))
                .couponGroup(couponGroupOptional.get())
                .couponGivenStateCode(CouponGivenStateCode.NOT_GIVEN)
                .couponUsageStateCode(CouponUsageStateCode.NOT_USED)
                .build());

        // when
        when(queryCouponGroupRepository.findCouponGroupByTriggerType(any())).thenReturn(
                couponGroupOptional);
        when(queryIssuedCouponRepository.findIssuedCouponByGroupCodeId(anyLong())).thenReturn(
                Optional.ofNullable(null)).thenReturn(issuedCouponOptional);
        List<CouponIssueResponseDto> result = service.getCouponIssueResponseDtoList(
                couponIssueRequestDto2);

        // then
        verify(commandIssuedCouponService, times(1)).issueCoupon(any());
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCreatedCouponCodes()).contains(issuedCouponCode);
        assertThat(result.get(0).getCouponGroupCode()).isEqualTo(couponGroupCode);
    }

    @Test
    @DisplayName("수량 제한 쿠폰에 대해 발행된 쿠폰이 존재하지 않는 경우 실패 응답")
    void getCouponIssueResponseDtoList_limited_FailTest() {
        TriggerTypeCode triggerTypeCode = TriggerTypeCode.MEMBER_GRADE_GOLD;
        Coupon coupon = CouponDummy.dummyRateCouponWithUnlimited(false);

        CouponIssueRequestDto couponIssueRequestDto2 = CouponIssueRequestDto.builder()
                .triggerTypeCode(triggerTypeCode.toString())
                .quantity(1)
                .build();

        Optional<CouponGroup> couponGroupOptional = Optional.of(CouponGroup.builder()
                .id(1L)
                .triggerTypeCode(triggerTypeCode)
                .coupon(coupon)
                .groupCode(couponGroupCode)
                .build());

        // when
        when(queryCouponGroupRepository.findCouponGroupByTriggerType(any())).thenReturn(
                couponGroupOptional);
        when(queryIssuedCouponRepository.findIssuedCouponByGroupCodeId(anyLong())).thenReturn(
                Optional.ofNullable(null));

        // then
        verify(commandIssuedCouponService, times(0)).issueCoupon(any());
        ClientException result = Assertions.assertThrows(
                ClientException.class,
                () -> service.getCouponIssueResponseDtoList(couponIssueRequestDto2)
        );
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.ISSUED_COUPON_NOT_FOUND);
    }
}