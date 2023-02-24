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
import shop.yesaladin.coupon.coupon.dto.CouponIssueRequestDto;
import shop.yesaladin.coupon.coupon.dto.CouponIssueResponseDto;
import shop.yesaladin.coupon.coupon.service.inter.CommandIssuedCouponService;
import shop.yesaladin.coupon.coupon.service.inter.GiveCouponService;
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
@Slf4j
@RequiredArgsConstructor
@Service
public class GiveCouponServiceImpl implements GiveCouponService {

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

            for (CouponIssueResponseDto responseDto : responseDtoList) {
                log.info(
                        "==== [COUPON] coupon group code to give member by trigger type {} : {} ====",
                        message.getTriggerTypeCode(),
                        responseDto.getCouponGroupCode()
                );
            }
        } catch (ClientException e) {
            log.error("=== [COUPON] ===", e);
            // 쿠폰 발행 실패 응답
            return CouponGiveRequestResponseMessage.builder()
                    .requestId(message.getRequestId())
                    .success(false)
                    .errorMessage(e.getMessage())
                    .build();
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
                    coupons.get(i).getCouponCodes().get(0),
                    coupons.get(i).getCouponGroupCode()
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
}
