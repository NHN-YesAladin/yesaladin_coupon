package shop.yesaladin.coupon.coupon.kafka;

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
class CouponProducerTest {

    @Autowired
    private KafkaTopicConfig kafkaTopicConfig;

    @Autowired
    private CouponProducer couponProducer;

    @Autowired
    private CouponConsumer couponConsumer;

    @Test
    void test() {

//        producer.send(kafkaTopicConfig..);
    }
}