package shop.yesaladin.coupon.coupon.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.config.KafkaTopicConfig;
import shop.yesaladin.coupon.message.CouponGiveRequestMessage;

@Transactional
@SpringBootTest
@EmbeddedKafka(partitions = 3, brokerProperties = {
        "listeners=PLAINTEXT://localhost:9092", "port=9092"})
class ConsumerTest {

    @Autowired
    private KafkaTopicConfig kafkaTopicConfig;
    @Autowired
    private Producer producer;
    @Autowired
    private Consumer consumer;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void giveRequestTest() {
        // given
        String requestId = "requestId";
        CouponGiveRequestMessage message = CouponGiveRequestMessage.builder()
                .requestId(requestId)
                .triggerTypeCode(TriggerTypeCode.SIGN_UP)
                .build();

        // when
        producer.send(kafkaTopicConfig.getGiveRequest(), message);
//        consumer.
    }

}