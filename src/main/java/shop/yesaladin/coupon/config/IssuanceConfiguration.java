package shop.yesaladin.coupon.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 쿠폰 발행시 필요한 설정들을 외부화하기 위해 사용하는 Configuration 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Getter
@Configuration
public class IssuanceConfiguration {

    @Value("${coupon.issue.unlimited-coupon-issue-size}")
    private int unlimitedCouponIssueSize;

    @Value("${coupon.issue.unlimited-flag-value}")
    private int unlimitedFlag;
}
