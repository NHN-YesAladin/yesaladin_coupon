package shop.yesaladin.coupon.config;

import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate 설정 클래스입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Configuration
class RestTemplateConfig {

    /**
     * connectionTimeout, readTimeout 이 각각 5초, 통신 실패 시 3번 재시도하는 RestTemplate 입니다.
     *
     * @return 타임아웃 설정과 RetryTemplate 을 적용한 RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(5))
                .additionalInterceptors(clientHttpRequestInterceptor())
                .setBufferRequestBody(false)
                .build();
    }

    /**
     * RestTemplate 의 요청이 실패했을 경우 재시도를 실행하도록 설정합니다.
     *
     * @return RestTemplate 에 적용할 RetryTemplate 이 적용된 인터셉터
     */
    @Bean
    public ClientHttpRequestInterceptor clientHttpRequestInterceptor() {
        return (request, body, execution) -> {
            RetryTemplate retryTemplate = new RetryTemplate();
            retryTemplate.setRetryPolicy(new SimpleRetryPolicy(3));
            try {
                return retryTemplate.execute(context -> execution.execute(request, body));
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        };
    }

}