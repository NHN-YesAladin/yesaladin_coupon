package shop.yesaladin.coupon.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 타임머신 테스트를 용이하게 하기 위한 Clock 빈을 생성하는 Configuration 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Configuration
public class ClockConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
