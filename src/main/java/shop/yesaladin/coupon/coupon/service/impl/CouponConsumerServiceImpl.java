package shop.yesaladin.coupon.coupon.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.coupon.coupon.domain.model.CouponGivenStateCode;
import shop.yesaladin.coupon.coupon.domain.model.CouponUsageStateCode;
import shop.yesaladin.coupon.coupon.dto.CouponIssueRequestDto;
import shop.yesaladin.coupon.coupon.dto.CouponIssueResponseDto;
import shop.yesaladin.coupon.coupon.kafka.CouponProducer;
import shop.yesaladin.coupon.coupon.service.inter.CommandIssuedCouponService;
import shop.yesaladin.coupon.coupon.service.inter.CouponConsumerService;
import shop.yesaladin.coupon.coupon.service.inter.QueryIssuedCouponService;
import shop.yesaladin.coupon.dto.CouponGiveDto;
import shop.yesaladin.coupon.message.CouponCodesAndResultMessage;
import shop.yesaladin.coupon.message.CouponCodesMessage;
import shop.yesaladin.coupon.message.CouponGiveRequestMessage;
import shop.yesaladin.coupon.message.CouponGiveRequestResponseMessage;

/**
 * 쿠폰 관련 요청 메시지를 처리하기 위한 CouponConsumerService 의 구현체입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class CouponConsumerServiceImpl implements CouponConsumerService {

    private final CouponProducer couponProducer;
    private final CommandIssuedCouponService commandIssuedCouponService;
    private final QueryIssuedCouponService queryIssuedCouponService;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public void consumeCouponGiveRequestMessage(CouponGiveRequestMessage message) {
        List<CouponIssueResponseDto> responseDtoList;
        try {
            responseDtoList = queryIssuedCouponService.getCouponIssueResponseDtoList(
                    CouponIssueRequestDto.fromCouponGiveRequestMessage(message));
        } catch (Exception e) {
            // 쿠폰 발행 실패 응답
            CouponGiveRequestResponseMessage giveRequestResponseMessage = CouponGiveRequestResponseMessage.builder()
                    .requestId(message.getRequestId())
                    .success(false)
                    .errorMessage(e.getMessage())
                    .build();
            couponProducer.responseGiveRequest(giveRequestResponseMessage);
            return;
        }

        assert responseDtoList != null;
        List<CouponGiveDto> coupons = responseDtoList.stream()
                .map(CouponIssueResponseDto::toCouponGiveDto)
                .collect(Collectors.toList());

        // 동일한 requestId 를 포함하는 지급 요청 응답 메시지를 보낸다.
        CouponGiveRequestResponseMessage giveRequestResponseMessage = CouponGiveRequestResponseMessage.builder()
                .requestId(message.getRequestId())
                .coupons(coupons)
                .success(true)
                .build();

        couponProducer.responseGiveRequest(giveRequestResponseMessage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void consumeCouponGivenMessage(CouponCodesAndResultMessage message) {
        CouponGivenStateCode givenStateCode = CouponGivenStateCode.NOT_GIVEN;

        if (message.isSuccess()) {
            givenStateCode = CouponGivenStateCode.GIVEN;
        }
        commandIssuedCouponService.updateCouponGivenStateAndDateTime(
                message.getCouponCodes(),
                givenStateCode
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void consumeCouponGiveRequestCancelMessage(CouponCodesMessage message) {
        commandIssuedCouponService.updateCouponGivenStateAndDateTime(
                message.getCouponCodes(),
                CouponGivenStateCode.NOT_GIVEN
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void consumeCouponUsedMessage(CouponCodesAndResultMessage message) {
        CouponUsageStateCode usageStateCode = CouponUsageStateCode.NOT_USED;

        if (message.isSuccess()) {
            usageStateCode = CouponUsageStateCode.USED;
        }
        commandIssuedCouponService.updateCouponUsageStateAndDateTime(
                message.getCouponCodes(),
                usageStateCode
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void consumeCouponUseRequestCancelMessage(CouponCodesMessage message) {
        commandIssuedCouponService.updateCouponUsageStateAndDateTime(
                message.getCouponCodes(),
                CouponUsageStateCode.NOT_USED
        );
    }
}