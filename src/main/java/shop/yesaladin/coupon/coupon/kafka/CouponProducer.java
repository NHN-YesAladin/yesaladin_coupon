package shop.yesaladin.coupon.coupon.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import shop.yesaladin.coupon.config.KafkaTopicConfig;
import shop.yesaladin.coupon.message.CouponGiveRequestResponseMessage;
import shop.yesaladin.coupon.message.CouponUseRequestResponseMessage;

/**
 * 쿠폰 관련 요청에 대한 응답 메시지를 생성하는 producer 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class CouponProducer {

    private final KafkaTopicConfig kafkaTopicConfig;
    private final KafkaTemplate<String, Object> template;

    /**
     * 쿠폰 지급 요청에 대한 응답 메시지를 생성합니다.
     *
     * @param message 쿠폰 지급 요청에 대한 응답 메시지
     */
    public void responseGiveRequest(CouponGiveRequestResponseMessage message) {
        String topic = kafkaTopicConfig.getGiveRequestResponse();
        log.info("sending payload='{}' to topic='{}'", message.toString(), topic);
        template.send(topic, message);
    }

    /**
     * 쿠폰 사용 요청에 대한 응답 메시지를 생성합니다.
     *
     * @param message 쿠폰 사용 요청에 대한 응답 메시지
     */
    public void responseUseRequest(CouponUseRequestResponseMessage message) {
        String topic = kafkaTopicConfig.getUseRequestResponse();
        log.info("sending payload='{}' to topic='{}'", message.toString(), topic);
        template.send(topic, message);
    }
}
