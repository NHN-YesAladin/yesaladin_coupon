package shop.yesaladin.coupon.coupon.kafka;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.coupon.config.KafkaTopicConfig;

@Transactional
@SpringBootTest
@EmbeddedKafka(partitions = 3, brokerProperties = {
        "listeners=PLAINTEXT://localhost:9092", "port=9092"})
class ProducerTest {

    @Autowired
    private KafkaTopicConfig kafkaTopicConfig;

    @Autowired
    private Producer producer;

    @Autowired
    private Consumer consumer;

    @Test
    void test() {

//        producer.send(kafkaTopicConfig..);
    }
}