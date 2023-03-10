package shop.yesaladin.coupon.coupon.kafka;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.config.KafkaTopicProperties;
import shop.yesaladin.coupon.coupon.service.inter.CouponConsumerService;
import shop.yesaladin.coupon.message.CouponGiveRequestMessage;

@Disabled
@SpringBootTest
@ActiveProfiles("local-test")
@EmbeddedKafka(brokerProperties = {
        "listeners=PLAINTEXT://localhost:9092", "port=9092"})
class CouponConsumerTest {

    @Autowired
    private KafkaTopicProperties kafkaTopicProperties;
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private CouponConsumer couponConsumer;
    @MockBean
    private CouponConsumerService couponConsumerService;
    @Autowired
    private EmbeddedKafkaBroker broker;
    private ArgumentCaptor<Object> argumentCaptor;

    @BeforeEach
    void setUp() {
        Map<String, Object> props = KafkaTestUtils.producerProps(broker);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        DefaultKafkaProducerFactory<String, Object> producerFactory = new DefaultKafkaProducerFactory<>(
                props);
        kafkaTemplate = new KafkaTemplate<>(producerFactory);
        argumentCaptor = ArgumentCaptor.forClass(Object.class);
    }

    @Test
    @DisplayName("?????????????????? ????????? ?????? ??????")
    void giveRequestTest() throws InterruptedException {
        // given
        String requestId = "requestId";
        CouponGiveRequestMessage message = CouponGiveRequestMessage.builder()
                .requestId(requestId)
                .triggerTypeCode(TriggerTypeCode.SIGN_UP)
                .build();

        // when
        String topic = kafkaTopicProperties.getGiveRequest();
        kafkaTemplate.send(topic, message);

        // then
        assertThat(topic).isEqualTo("test_yesaladin_coupon_give_request");
        boolean isConsumed = couponConsumer.getCountDownLatch()
                .await(10, TimeUnit.SECONDS);    // latch ??? 0??? ?????? true
        Mockito.verify(couponConsumerService, Mockito.times(1))
                .consumeCouponGiveRequestMessage((CouponGiveRequestMessage) argumentCaptor.capture());
        assertThat(isConsumed).isTrue();
        CouponGiveRequestMessage arg = (CouponGiveRequestMessage) argumentCaptor.getValue();
        assertThat(arg.getRequestId()).isEqualTo(requestId);
        assertThat(arg.getTriggerTypeCode()).isEqualTo(TriggerTypeCode.SIGN_UP);
    }
}