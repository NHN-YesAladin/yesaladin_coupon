package shop.yesaladin.coupon.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class IssuanceConfiguration {

    @Value("${coupon.issue.unlimited-coupon-issue-size}")
    private int unlimitedCouponIssueSize;

    @Value("${coupon.issue.unlimited-flag-value}")
    private int unlimitedFlag;
}
