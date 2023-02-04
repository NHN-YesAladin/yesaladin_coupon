package shop.yesaladin.coupon.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties("coupon.topic")
@Configuration
public class KafkaTopicConfig {

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
