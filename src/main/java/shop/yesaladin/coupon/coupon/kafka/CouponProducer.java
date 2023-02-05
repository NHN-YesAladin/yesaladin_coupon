package shop.yesaladin.coupon.coupon.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import shop.yesaladin.coupon.config.KafkaTopicConfig;

@Slf4j
@RequiredArgsConstructor
@Component
public class CouponProducer {

    private final KafkaTopicConfig kafkaTopicConfig;
    private final KafkaTemplate<String, Object> template;

    public void responseGiveRequest(Object message) {
        String topic = kafkaTopicConfig.getGiveRequestResponse();
        log.info("sending payload='{}' to topic='{}'", message.toString(), topic);
        template.send(topic, message);
    }

    public void responseUseRequest(Object message) {
        String topic = kafkaTopicConfig.getUseRequestResponse();
        log.info("sending payload='{}' to topic='{}'", message.toString(), topic);
        template.send(topic, message);
    }
}
