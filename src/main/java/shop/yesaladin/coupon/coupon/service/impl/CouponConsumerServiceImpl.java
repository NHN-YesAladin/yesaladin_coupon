package shop.yesaladin.coupon.coupon.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.common.exception.ServerException;
import shop.yesaladin.coupon.coupon.domain.model.CouponGivenStateCode;
import shop.yesaladin.coupon.coupon.domain.model.CouponUsageStateCode;
import shop.yesaladin.coupon.coupon.domain.model.IssuedCoupon;
import shop.yesaladin.coupon.coupon.dto.CouponIssueRequestDto;
import shop.yesaladin.coupon.coupon.dto.CouponIssueResponseDto;
import shop.yesaladin.coupon.coupon.kafka.CouponProducer;
import shop.yesaladin.coupon.coupon.service.inter.CommandIssuedCouponService;
import shop.yesaladin.coupon.coupon.service.inter.CouponConsumerService;
import shop.yesaladin.coupon.coupon.service.inter.QueryIssuedCouponService;
import shop.yesaladin.coupon.dto.CouponGiveDto;
import shop.yesaladin.coupon.message.CouponCodesMessage;
import shop.yesaladin.coupon.message.CouponGiveRequestMessage;
import shop.yesaladin.coupon.message.CouponGiveRequestResponseMessage;
import shop.yesaladin.coupon.message.CouponUseRequestMessage;
import shop.yesaladin.coupon.message.CouponUseRequestResponseMessage;

/**
 * 쿠폰 관련 요청 메시지를 처리하기 위한 CouponConsumerService 의 구현체입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CouponConsumerServiceImpl implements CouponConsumerService {

    private final CouponProducer couponProducer;
    private final CommandIssuedCouponService commandIssuedCouponService;
    private final QueryIssuedCouponService queryIssuedCouponService;

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    @Transactional
    public CouponGiveRequestResponseMessage consumeCouponGiveRequestMessage(CouponGiveRequestMessage message) {
        List<CouponIssueResponseDto> responseDtoList;
        try {
            responseDtoList = queryIssuedCouponService.getCouponIssueResponseDtoList(
                    CouponIssueRequestDto.fromCouponGiveRequestMessage(message));

            for (int i = 0; i < responseDtoList.size(); i++) {
                log.info(
                        "==== [COUPON] coupon group code to give member by trigger type {} : {} ====",
                        message.getTriggerTypeCode(),
                        responseDtoList.get(i).getCouponGroupCode()
                );
            }

        } catch (ClientException e) {
            log.error("=== [COUPON] ===", e);
            // 쿠폰 발행 실패 응답
            CouponGiveRequestResponseMessage giveRequestResponseMessage = CouponGiveRequestResponseMessage.builder()
                    .requestId(message.getRequestId())
                    .success(false)
                    .errorMessage(e.getMessage())
                    .build();
            couponProducer.responseGiveRequest(giveRequestResponseMessage);
            return giveRequestResponseMessage;
        } catch (Exception e) {
            log.error("=== [COUPON] ===", e);
            return CouponGiveRequestResponseMessage.builder()
                    .requestId(message.getRequestId())
                    .success(false)
                    .errorMessage(String.valueOf(new ServerException(
                            ErrorCode.BAD_REQUEST,
                            "Exception occurred at consume coupon give request message."
                    )))
                    .build();
        }

        List<CouponGiveDto> coupons = responseDtoList.stream()
                .map(CouponIssueResponseDto::toCouponGiveDto)
                .collect(Collectors.toList());
        for (int i = coupons.size() - 1; i >= 0; i--) {
            log.info(
                    "==== [COUPON] coupon code to give member {} (couponGroupCode: {})",
                    coupons.get(i).getCouponCodes().get(0), coupons.get(i).getCouponGroupCode()
            );
        }

        // 지급할 발행쿠폰의 지급상태코드를 '지급 대기'로 업데이트
        List<String> couponCodesToGive = new ArrayList<>();
        coupons.forEach(couponGiveDto -> couponCodesToGive.addAll(couponGiveDto.getCouponCodes()));

        commandIssuedCouponService.updateCouponGivenStateAndDateTime(
                couponCodesToGive,
                CouponGivenStateCode.PENDING_GIVE
        );

        // 동일한 requestId 를 포함하는 지급 요청 응답 메시지를 보낸다.

        return CouponGiveRequestResponseMessage.builder()
                .requestId(message.getRequestId())
                .coupons(coupons)
                .success(true)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void consumeCouponGivenMessage(CouponCodesAndResultMessage message) {
        CouponGivenStateCode givenStateCode = CouponGivenStateCode.NOT_GIVEN;

        givenStateCode = CouponGivenStateCode.GIVEN;
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
    public void consumeCouponUseRequestMessage(CouponUseRequestMessage message) {
        List<IssuedCoupon> unusableCouponList = queryIssuedCouponService.checkUnavailableIssuedCoupon(
                message.getCouponCodes(),
                message.getRequestDateTime()
        );

        CouponUseRequestResponseMessage responseMessage;
        if (unusableCouponList.isEmpty()) {
            commandIssuedCouponService.updateCouponUsageStateAndDateTime(
                    message.getCouponCodes(),
                    CouponUsageStateCode.PENDING_USE
            );
            responseMessage = CouponUseRequestResponseMessage.builder()
                    .requestId(message.getRequestId())
                    .success(true)
                    .build();
        } else {
            responseMessage = CouponUseRequestResponseMessage.builder()
                    .requestId(message.getRequestId())
                    .success(false)
                    .errorMessage(
                            "Unavailable publishing coupon exists: " + unusableCouponList.stream()
                                    .map(IssuedCoupon::getCouponCode)
                                    .collect(Collectors.joining(", ")))
                    .build();
        }
        couponProducer.responseUseRequest(responseMessage);
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

    //
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


    public static class CouponCodesAndResultMessage {

        private List<String> couponCodes;
        private Boolean success = true;


        public List<String> getCouponCodes() {
            return this.couponCodes;
        }

        public Boolean isSuccess() {
            return this.success;
        }

        CouponCodesAndResultMessage() {
        }

        public CouponCodesAndResultMessage(List<String> couponCodes, Boolean success) {
            this.couponCodes = couponCodes;
            this.success = success;
        }

    }

}
