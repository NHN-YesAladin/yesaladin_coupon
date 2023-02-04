package shop.yesaladin.coupon.coupon.kafka;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import shop.yesaladin.coupon.config.KafkaTopicConfig;
import shop.yesaladin.coupon.coupon.domain.model.CouponGivenStateCode;
import shop.yesaladin.coupon.coupon.domain.model.CouponUsageStateCode;
import shop.yesaladin.coupon.coupon.dto.CouponIssueRequestDto;
import shop.yesaladin.coupon.coupon.dto.CouponIssueResponseDto;
import shop.yesaladin.coupon.coupon.service.inter.CommandIssuedCouponService;
import shop.yesaladin.coupon.dto.CouponGiveDto;
import shop.yesaladin.coupon.message.CouponCodesAndResultMessage;
import shop.yesaladin.coupon.message.CouponCodesMessage;
import shop.yesaladin.coupon.message.CouponGiveRequestMessage;
import shop.yesaladin.coupon.message.CouponGiveRequestResponseMessage;
import shop.yesaladin.coupon.message.CouponUseRequestMessage;

@RequiredArgsConstructor
@Component
public class Consumer {

    private final KafkaTopicConfig kafkaTopicConfig;
    private final Producer producer;
    private final CommandIssuedCouponService commandIssuedCouponService;

    /**
     * 쿠폰(무제한) 지급요청 메시지를 처리합니다.
     *
     * @param records 쿠폰(무제한) 지급요청 토픽으로부터 읽어온 메시지 리스트
     */
    @KafkaListener(id = "yesaladin_coupon_give_request", topics = "${coupon.topic.give-request}")
    public void giveRequestListener(List<CouponGiveRequestMessage> records) {
        for (CouponGiveRequestMessage message : records) {
            // triggerTypeCode, couponId 에 따라 발행된 쿠폰코드와 그룹 코드를 응답 메시지로 전달한다.
            // 발행된 쿠폰이 없는 경우 해당 쿠폰을 발행한 후 응답 메시지를 전달한다.
            // 지급 요청 메시지에 따라 쿠폰을 발행한 후
            List<CouponIssueResponseDto> responseDtoList = commandIssuedCouponService.issueCoupon(
                    CouponIssueRequestDto.fromCouponGiveRequestMessage(message));

            List<CouponGiveDto> coupons = responseDtoList.stream()
                    .map(CouponIssueResponseDto::toCouponGiveDto)
                    .collect(Collectors.toList());

            // 동일한 requestId 를 포함하는 지급 요청 응답 메시지를 보낸다.
            CouponGiveRequestResponseMessage giveRequestResponseMessage = CouponGiveRequestResponseMessage.builder()
                    .requestId(message.getRequestId())
                    .coupons(coupons)
                    .success(true)
                    .build();

            // TODO 쿠폰 발행 실패 응답

            producer.send(kafkaTopicConfig.getGiveRequestResponse(), giveRequestResponseMessage);
        }
    }

    /**
     * 쿠폰(제한) 지급요청 메시지를 처리합니다.[][
     *
     * @param records 쿠폰(제한) 지급요청 토픽으로부터 읽어온 메시지 리스트
     */
    @KafkaListener(id = "yesaladin_coupon_give_request_limit", topics = "${coupon.topic.give-request-limit}")
    public void giveRequestLimitListener(List<CouponGiveRequestMessage> records) {
        //
    }

    /**
     * 쿠폰 지급 메시지 처리 성공 여부에 따라 발행된 쿠폰의 지급 상태를 미지급/지급완료 상태로 업데이트합니다.
     *m
     * @param records
     */
    @KafkaListener(id = "yesaladin_coupon_given", topics = "${coupon.topic.given}")
    public void givenListener(List<CouponCodesAndResultMessage> records) {
        for (CouponCodesAndResultMessage message : records) {
            CouponGivenStateCode givenStateCode = CouponGivenStateCode.NOT_GIVEN;

            if (message.isSuccess()) {
                givenStateCode = CouponGivenStateCode.GIVEN;
            }
            commandIssuedCouponService.updateCouponGivenState(
                    message.getCouponCodes(),
                    givenStateCode
            );
        }
    }

    /**
     * 쿠폰 지급 취소 메시지 처리 메시지 내 쿠폰 코드에 해당하는 발행 쿠폰의 지급 상태를 미지급으로 업데이트합니다.
     *
     * @param records
     */
    @KafkaListener(id = "yesaladin_coupon_give_request_cancel", topics = "${coupon.topic.give-request-cancel}")
    public void giveRequestCancelListener(List<CouponCodesMessage> records) {
        for (CouponCodesMessage message : records) {
            commandIssuedCouponService.updateCouponGivenState(
                    message.getCouponCodes(),
                    CouponGivenStateCode.NOT_GIVEN
            );
        }
    }

    /**
     * 쿠폰 사용 요청 메시지 처리 메시지 내 쿠폰 코드에 해당하는 발행 쿠폰의 사용 상태를 사용 대기 상태로 업데이트합니다.
     *
     * @param records
     */
    @KafkaListener(id = "yesaladin_coupon_use_request", topics = "${coupon.topic.use-request}")
    public void useRequestListener(List<CouponUseRequestMessage> records) {
        // 쿠폰 코드에 해당하는 발행 쿠폰의 사용 상태가 모두 미지급이고, 지급 상태가 모두 지급 완료 상태이고 메시지 발행 일시 기준 만료일이 지나지 않았으면
        // 쿠폰 코드에 해당하는 발행 쿠폰의 사용 상태를 사용 대기 상태로 변경합니다.
        // 요청 메시지의 requestId 와 성공여부를 포함하는 응답 메시지를 보냅니다.
        producer.send(kafkaTopicConfig.getUseRequestResponse(), null);
    }

    /**
     * 쿠폰 사용 완료 메시지 처리 메시지 내 성공여부에따라 쿠폰 코드에 해당하는 발행 쿠폰의 사용 상태를 사용완료 상태로 업데이트합니다.
     *
     * @param records
     */
    @KafkaListener(id = "yesaladin_coupon_used", topics = "${coupon.topic.used}")
    public void usedListener(List<CouponCodesAndResultMessage> records) {
        for (CouponCodesAndResultMessage message : records) {
            CouponUsageStateCode usageStateCode = CouponUsageStateCode.NOT_USED;

            if (message.isSuccess()) {
                usageStateCode = CouponUsageStateCode.USED;
            }
            commandIssuedCouponService.updateCouponUsageState(
                    message.getCouponCodes(),
                    usageStateCode
            );
        }
    }

    /**
     * 쿠폰 사용 취소 메시지 처리 메시지 내 쿠폰 코드에 해당하는 발행 쿠폰의 사용 상태를 미사용 상태로 업데이트합니다.
     *
     * @param records
     */
    @KafkaListener(id = "yesaladin_coupon_use_request_cancel", topics = "${coupon.topic.use-request-cancel}")
    public void useRequestCancelListener(List<CouponCodesMessage> records) {
        for (CouponCodesMessage message : records) {
            commandIssuedCouponService.updateCouponUsageState(
                    message.getCouponCodes(),
                    CouponUsageStateCode.NOT_USED
            );
        }
    }
}
