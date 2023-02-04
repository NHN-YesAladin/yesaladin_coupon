package shop.yesaladin.coupon.coupon.consumer;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import shop.yesaladin.coupon.coupon.service.inter.CommandCouponService;
import shop.yesaladin.coupon.message.CouponCodesAndResultMessage;
import shop.yesaladin.coupon.message.CouponGiveRequestMessage;

@RequiredArgsConstructor
@Service
public class Consumer {

    private final CommandCouponService commandCouponService;

    /**
     * 쿠폰 지급 요청 메시지 처리
     * @param records
     */
    @KafkaListener(id = "yesaladin_coupon_give_request", topics = "${coupon.topic.give-request}")
    public void giveRequestListener(List<CouponGiveRequestMessage> records) {
        // 지급 요청 메시지에 따라 쿠폰을 발행한 후
        // 동일한 requestId 를 포함하는 지급 요청 응답 메시지를 보낸다.

    }

    @KafkaListener(id = "yesaladin_coupon_give_request_limit", topics = "${coupon.topic.give-request-limit}")
    public void giveRequestLimitListener(List<Object> records) {

    }

    /**
     * 쿠폰 지급 메시지 처리
     * 성공 여부에 따라 발행된 쿠폰의 지급 상태를 미지급/지급완료 상태로 업데이트합니다.
     * @param records
     */
    @KafkaListener(id = "yesaladin_coupon_given", topics = "${coupon.topic.given}")
    public void givenListener(List<CouponCodesAndResultMessage> records) {
        for (CouponCodesAndResultMessage message : records) {
            commandCouponService.updateCouponGivenState(message);
        }
    }

    @KafkaListener(id = "yesaladin_coupon_give_request_cancel", topics = "${coupon.topic.give-request-cancel}")
    public void giveRequestCancelListener(List<Object> records) {

    }
    @KafkaListener(id = "yesaladin_coupon_use_request", topics = "${coupon.topic.use-request}")
    public void useRequestListener(List<Object> records) {

    }

    @KafkaListener(id = "yesaladin_coupon_used", topics = "${coupon.topic.used}")
    public void usedListener(List<Object> records) {

    }
    @KafkaListener(id = "yesaladin_coupon_use_request_cancel", topics = "${coupon.topic.use-request-cancel}")
    public void useRequestCancelListener(List<Object> records) {

    }
}
