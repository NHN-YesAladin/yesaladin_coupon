package shop.yesaladin.coupon.scheduler;

import java.time.LocalDateTime;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.CouponOfTheMonthPolicy;
import shop.yesaladin.coupon.coupon.dto.CouponIssueRequestDto;
import shop.yesaladin.coupon.coupon.service.inter.CommandIssuedCouponService;
import shop.yesaladin.coupon.coupon.service.inter.CouponOfTheMonthService;

/**
 * 이달의 쿠폰 오픈 시간 1시간 전에 지정된 쿠폰을 발행하는 스케줄러입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class CouponOfTheCouponScheduler {

    private final CommandIssuedCouponService commandIssuedCouponService;
    private final CouponOfTheMonthService couponOfTheMonthService;
    private ThreadPoolTaskScheduler scheduler;
    private String issueCron = "0 0 13 1 * *";

    private Runnable getRunnable() {
        return () -> {
            CouponOfTheMonthPolicy latestPolicy = couponOfTheMonthService.getLatestPolicy();
            log.info(
                    "==== {} will be issued with {} counts. ====",
                    latestPolicy.getCoupon().getName(),
                    latestPolicy.getQuantity()
            );

            commandIssuedCouponService.issueCoupon(CouponIssueRequestDto.builder()
                    .couponId(latestPolicy.getCoupon().getId())
                    .triggerTypeCode(TriggerTypeCode.COUPON_OF_THE_MONTH.toString())
                    .quantity(latestPolicy.getQuantity())
                    .build());
        };
    }

    public void changeIssueTime(int openDate, int openHour, int openMin) {
        // 이달의 쿠폰 이벤트 오픈 5분 전 발행 예약
        LocalDateTime issuedDateTime = LocalDateTime.now()
                .withDayOfMonth(openDate)
                .withHour(openHour)
                .withMinute(openMin).minusMinutes(5);

        this.issueCron = "0 " + issuedDateTime.getMinute() + " " + issuedDateTime.getHour() + " " + issuedDateTime.getDayOfMonth() + " * *";
        log.info("==== [COUPON SCHEDULER] updated issue time: {} ====", issuedDateTime.toLocalTime());
    }

    public void startScheduler() {
        this.scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();
        scheduler.setWaitForTasksToCompleteOnShutdown(false);
        scheduler.schedule(getRunnable(), getTrigger());
    }

    public void stopScheduler() {
        scheduler.shutdown();
    }

    private Trigger getTrigger() {
        return new CronTrigger(this.issueCron);
    }

    @PostConstruct
    public void init() {
        startScheduler();
    }

    @PreDestroy
    public void destroy() {
        stopScheduler();
    }
}
