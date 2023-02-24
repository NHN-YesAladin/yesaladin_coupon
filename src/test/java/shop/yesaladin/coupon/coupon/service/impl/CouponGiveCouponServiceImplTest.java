package shop.yesaladin.coupon.coupon.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.dto.CouponIssueResponseDto;
import shop.yesaladin.coupon.coupon.service.inter.CommandIssuedCouponService;
import shop.yesaladin.coupon.coupon.service.inter.GiveCouponService;
import shop.yesaladin.coupon.coupon.service.inter.QueryIssuedCouponService;
import shop.yesaladin.coupon.message.CouponGiveRequestMessage;
import shop.yesaladin.coupon.message.CouponGiveRequestResponseMessage;

class CouponGiveCouponServiceImplTest {

    private CommandIssuedCouponService commandIssuedCouponService;
    private QueryIssuedCouponService queryIssuedCouponService;
    private GiveCouponService service;
    private List<String> createdCouponCodeList;
    private String couponGroupCode;
    private ArgumentCaptor<Object> argumentCaptor;

    @BeforeEach
    void setUp() {
        commandIssuedCouponService = Mockito.mock(CommandIssuedCouponService.class);
        queryIssuedCouponService = Mockito.mock(QueryIssuedCouponServiceImpl.class);
        service = new GiveCouponServiceImpl(
                commandIssuedCouponService,
                queryIssuedCouponService
        );

        createdCouponCodeList = List.of(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        couponGroupCode = UUID.randomUUID().toString();
        argumentCaptor = ArgumentCaptor.forClass(Object.class);
    }

    @Test
    @DisplayName("쿠폰지급요청 메시지 성공 처리")
    void responseCouponGiveRequestMessageSuccessTest() {
        // given
        String requestId = "test requestId";
        TriggerTypeCode triggerTypeCode = TriggerTypeCode.SIGN_UP;
        CouponGiveRequestMessage couponGiveRequestMessage = CouponGiveRequestMessage.builder()
                .requestId(requestId)
                .triggerTypeCode(triggerTypeCode)
                .build();

        CouponIssueResponseDto couponIssueResponseDto = CouponIssueResponseDto.builder()
                .createdCouponCodes(createdCouponCodeList)
                .couponGroupCode(couponGroupCode)
                .build();

        // when
        when(queryIssuedCouponService.getCouponIssueResponseDtoList(any())).thenReturn(List.of(
                couponIssueResponseDto));
        service.consumeCouponGiveRequestMessage(couponGiveRequestMessage);

        // then
        CouponGiveRequestResponseMessage responseMessage = (CouponGiveRequestResponseMessage) argumentCaptor.getValue();
        assertThat(responseMessage.getRequestId()).isEqualTo(requestId);
        assertThat(responseMessage.getCoupons().get(0).getCouponCodes()).isEqualTo(
                createdCouponCodeList);
        assertThat(responseMessage.isSuccess()).isTrue();
    }

    @Test
    @DisplayName("쿠폰지급요청 메시지 실패 처리")
    void responseCouponGiveRequestMessageFailTest() {
        // given
        String requestId = "test requestId";
        TriggerTypeCode triggerTypeCode = TriggerTypeCode.SIGN_UP;
        CouponGiveRequestMessage couponGiveRequestMessage = CouponGiveRequestMessage.builder()
                .requestId(requestId)
                .triggerTypeCode(triggerTypeCode)
                .build();

        // when
        when(queryIssuedCouponService.getCouponIssueResponseDtoList(any())).thenThrow(new ClientException(
                ErrorCode.ISSUED_COUPON_NOT_FOUND, ErrorCode.COUPON_NOT_FOUND.getDisplayName()));
        service.consumeCouponGiveRequestMessage(couponGiveRequestMessage);

        // then
        CouponGiveRequestResponseMessage responseMessage = (CouponGiveRequestResponseMessage) argumentCaptor.getValue();
        assertThat(responseMessage.getRequestId()).isEqualTo(requestId);
        assertThat(responseMessage.getCoupons()).isNull();
        assertThat(responseMessage.isSuccess()).isFalse();
    }
}