package shop.yesaladin.coupon.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * kafka 토픽에 대한 값을 설정하는 클래스입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Getter
@Setter
@ConfigurationProperties("coupon.topic")
@Configuration
public class KafkaTopicProperties {

    private String giveRequest;
    private String giveRequestLimit;
    private String given;
    private String giveRequestCancel;
    private String useRequest;
    private String used;
    private String useRequestCancel;
    private String giveRequestResponse;
    private String useRequestResponse;
}
