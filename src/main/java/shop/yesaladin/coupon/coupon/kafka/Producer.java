package shop.yesaladin.coupon.coupon.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class Producer {

    private final KafkaTemplate<String, Object> template;

    public void send(String topic, Object payload) {
        log.info("sending payload='{}' to topic='{}'", payload.toString(), topic);
        template.send(topic, payload);
    }
}
