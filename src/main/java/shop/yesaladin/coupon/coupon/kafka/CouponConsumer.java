package shop.yesaladin.coupon.coupon.kafka;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import shop.yesaladin.coupon.coupon.service.inter.CouponConsumerService;
import shop.yesaladin.coupon.message.CouponCodesAndResultMessage;
import shop.yesaladin.coupon.message.CouponCodesMessage;
import shop.yesaladin.coupon.message.CouponGiveRequestMessage;
import shop.yesaladin.coupon.message.CouponUseRequestMessage;

/**
 * Shop 서버에서 들어오는 쿠폰 관련 메시지를 처리하는 consumer 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@RequiredArgsConstructor
@Component
public class CouponConsumer {

    private final CouponConsumerService couponConsumerService;
    @Getter
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    /**
     * [지급 요청] (무제한)쿠폰 지급요청 메시지를 처리합니다.
     *
     * @param records 쿠폰(무제한) 지급요청 토픽으로부터 읽어온 메시지 리스트
     */
    @KafkaListener(id = "yesaladin_coupon_give_request", topics = "${coupon.topic.give-request}")
    public void giveRequestListener(List<CouponGiveRequestMessage> records) {
        for (CouponGiveRequestMessage message : records) {
            couponConsumerService.consumeCouponGiveRequestMessage(message);
            countDownLatch.countDown(); // for test
        }
        resetCountDownLatch();
    }

    /**
     * [지급 요청] (수량 제한)쿠폰 지급요청 메시지를 처리합니다.
     *
     * @param records 쿠폰(제한) 지급요청 토픽으로부터 읽어온 메시지 리스트
     */
    @KafkaListener(id = "yesaladin_coupon_give_request_limit", topics = "${coupon.topic.give-request-limit}")
    public void giveRequestLimitListener(List<CouponGiveRequestMessage> records) {
        for (CouponGiveRequestMessage message : records) {
            couponConsumerService.consumeCouponGiveRequestMessage(message);
        }
    }

    /**
     * [사용 결과] 쿠폰 지급 결과 메시지의 처리 성공 여부에 따라 발행쿠폰의 지급 상태와 지급 일시를 업데이트합니다.
     *
     * @param records 쿠폰 지급 결과 토픽으로부터 읽어온 메시지 리스트
     */
    @KafkaListener(id = "yesaladin_coupon_given", topics = "${coupon.topic.given}")
    public void givenListener(List<CouponCodesAndResultMessage> records) {
        for (CouponCodesAndResultMessage message : records) {
            couponConsumerService.consumeCouponGivenMessage(message);
        }
    }

    /**
     * [지급 취소] 쿠폰 지급 취소 메시지 내 쿠폰 코드에 해당하는 발행쿠폰의 지급 상태(미지급)와 지급 일시를 업데이트합니다.
     *
     * @param records 쿠폰 지급 취소 토픽으로부터 읽어온 메시지 리스트
     */
    @KafkaListener(id = "yesaladin_coupon_give_request_cancel", topics = "${coupon.topic.give-request-cancel}")
    public void giveRequestCancelListener(List<CouponCodesMessage> records) {
        for (CouponCodesMessage message : records) {
            couponConsumerService.consumeCouponGiveRequestCancelMessage(message);
        }
    }

    /**
     * [사용 요청] 쿠폰 사용 요청 메시지 내 쿠폰 코드에 해당하는 발행쿠폰의 유효성 검사를 통해 사용 상태(사용 대기)를 업데이트합니다.
     *
     * @param records 쿠폰 사용 요청 토픽으로부터 읽어온 메시지 리스트
     */
    @KafkaListener(id = "yesaladin_coupon_use_request", topics = "${coupon.topic.use-request}")
    public void useRequestListener(List<CouponUseRequestMessage> records) {
        for (CouponUseRequestMessage message : records) {
            couponConsumerService.consumeCouponUseRequestMessage(message);
        }
    }

    /**
     * [사용 결과] 쿠폰 사용 메시지의 성공 여부에 따라 발행쿠폰의 사용 상태(사용완료/미사용)와 사용 일시를 업데이트합니다.
     *
     * @param records 쿠폰 사용 결과 토픽으로부터 읽어온 메시지 리스트
     */
    @KafkaListener(id = "yesaladin_coupon_used", topics = "${coupon.topic.used}")
    public void usedListener(List<CouponCodesAndResultMessage> records) {
        for (CouponCodesAndResultMessage message : records) {
            couponConsumerService.consumeCouponUsedMessage(message);
        }
    }

    /**
     * [사용 취소] 쿠폰 사용 취소 메시지의 쿠폰 코드에 해당하는 발행쿠폰의 사용 상태(미사용)와 사용 일시를 업데이트합니다.
     *
     * @param records 쿠폰 사용 취소 토픽으로부터 읽어온 메시지 리스트
     */
    @KafkaListener(id = "yesaladin_coupon_use_request_cancel", topics = "${coupon.topic.use-request-cancel}")
    public void useRequestCancelListener(List<CouponCodesMessage> records) {
        for (CouponCodesMessage message : records) {
            couponConsumerService.consumeCouponUseRequestCancelMessage(message);
        }
    }

    private void resetCountDownLatch() {
        this.countDownLatch = new CountDownLatch(1);
    }
}
