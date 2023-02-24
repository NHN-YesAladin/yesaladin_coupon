package shop.yesaladin.coupon.config;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate 설정 클래스입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Slf4j
@Configuration
class RestTemplateConfig {

    /**
     * connectionTimeout, readTimeout 이 각각 5초, 통신 실패 시 3번 재시도하는 RestTemplate 입니다.
     *
     * @return 타임아웃 설정과 RetryTemplate 을 적용한 RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(5))
                .setBufferRequestBody(false)
                .build();
    }

}