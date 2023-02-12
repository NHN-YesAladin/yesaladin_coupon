package shop.yesaladin.coupon.coupon.kafka;

import java.util.List;
import java.util.UUID;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import shop.yesaladin.coupon.config.KafkaTopicProperties;
import shop.yesaladin.coupon.dto.CouponGiveDto;
import shop.yesaladin.coupon.message.CouponGiveRequestResponseMessage;

@Disabled
@SpringBootTest
@ActiveProfiles("local-test")
@EmbeddedKafka(brokerProperties = {
        "listeners=PLAINTEXT://localhost:9093", "port=9093"})
class CouponProducerTest {

    @Autowired
    private KafkaTopicProperties kafkaTopicProperties;
    @Autowired
    private CouponProducer couponProducer;
    private Consumer<String, Object> consumer;
    @Autowired
    private ConsumerFactory<String, Object> consumerFactory;

    @BeforeEach
    void setUp() {
        consumer = consumerFactory.createConsumer("test-group", "1");
    }

    @Test
    @DisplayName("쿠폰 지급요청에 대한 응답 성공")
    void test() {
        // given
        String requestId = "test requestId";
        String couponCode = UUID.randomUUID().toString();
        String couponGroupCode = UUID.randomUUID().toString();
        String giveRequestResponseTopic = kafkaTopicProperties.getGiveRequestResponse();

        CouponGiveDto couponGiveDto = CouponGiveDto.builder()
                .couponCodes(List.of(couponCode))
                .couponGroupCode(couponGroupCode)
                .build();

        CouponGiveRequestResponseMessage message = CouponGiveRequestResponseMessage.builder()
                .coupons(List.of(couponGiveDto))
                .requestId(requestId)
                .success(true)
                .build();

        consumer.subscribe(List.of(giveRequestResponseTopic));

        // when
        couponProducer.responseGiveRequest(message);
        ConsumerRecord<String, Object> consumerRecord = KafkaTestUtils.getSingleRecord(
                consumer,
                giveRequestResponseTopic
        );
        CouponGiveRequestResponseMessage result = (CouponGiveRequestResponseMessage) consumerRecord.value();

        // then
        Assertions.assertThat(result.getRequestId()).isEqualTo(requestId);
        Assertions.assertThat(result.isSuccess()).isTrue();
        Assertions.assertThat(result.getCoupons()).hasSize(1);
        Assertions.assertThat(result.getCoupons().get(0).getCouponGroupCode())
                .isEqualTo(couponGroupCode);
    }
}