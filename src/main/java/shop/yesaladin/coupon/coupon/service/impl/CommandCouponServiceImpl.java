package shop.yesaladin.coupon.coupon.service.impl;


import static shop.yesaladin.coupon.code.TriggerTypeCode.BIRTHDAY;
import static shop.yesaladin.coupon.code.TriggerTypeCode.COUPON_OF_THE_MONTH;
import static shop.yesaladin.coupon.code.TriggerTypeCode.SIGN_UP;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shop.yesaladin.coupon.code.CouponBoundCode;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.config.StorageConfiguration;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.CouponBound;
import shop.yesaladin.coupon.coupon.domain.model.CouponGroup;
import shop.yesaladin.coupon.coupon.domain.model.CouponOfTheMonthPolicy;
import shop.yesaladin.coupon.coupon.domain.model.Trigger;
import shop.yesaladin.coupon.coupon.domain.repository.CommandCouponBoundRepository;
import shop.yesaladin.coupon.coupon.domain.repository.CommandCouponGroupRepository;
import shop.yesaladin.coupon.coupon.domain.repository.CommandCouponOfTheMonthPolicyRepository;
import shop.yesaladin.coupon.coupon.domain.repository.CommandCouponRepository;
import shop.yesaladin.coupon.coupon.domain.repository.CommandTriggerRepository;
import shop.yesaladin.coupon.coupon.dto.AmountCouponRequestDto;
import shop.yesaladin.coupon.coupon.dto.CouponIssueRequestDto;
import shop.yesaladin.coupon.coupon.dto.CouponRequestDto;
import shop.yesaladin.coupon.coupon.dto.CouponResponseDto;
import shop.yesaladin.coupon.coupon.dto.PointCouponRequestDto;
import shop.yesaladin.coupon.coupon.dto.RateCouponRequestDto;
import shop.yesaladin.coupon.coupon.service.inter.CommandCouponService;
import shop.yesaladin.coupon.coupon.service.inter.CommandIssuedCouponService;
import shop.yesaladin.coupon.file.service.inter.ObjectStorageService;
import shop.yesaladin.coupon.scheduler.CouponOfTheCouponScheduler;

/**
 * CommandCouponService 인터페이스의 구현체 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CommandCouponServiceImpl implements CommandCouponService {

    private final CommandCouponOfTheMonthPolicyRepository couponOfTheMonthPolicyRepository;
    private final CommandCouponRepository couponRepository;
    private final CommandCouponBoundRepository couponBoundRepository;
    private final CommandTriggerRepository triggerRepository;
    private final CommandCouponGroupRepository couponGroupRepository;
    private final CommandIssuedCouponService issueCouponService;
    private final ObjectStorageService objectStorageService;
    private final StorageConfiguration storageConfiguration;
    private final CouponOfTheCouponScheduler couponOfTheCouponScheduler;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CouponResponseDto createPointCoupon(PointCouponRequestDto pointCouponRequestDto) {
        if (hasImageFile(pointCouponRequestDto)) {
            pointCouponRequestDto.setImageFileUri(upload(pointCouponRequestDto.getImageFile()));
        }
        Coupon coupon = issueCouponAfterCreate(pointCouponRequestDto);
        checkIsCouponOfTheMonth(pointCouponRequestDto, coupon);

        return new CouponResponseDto(coupon.getName(), coupon.getCouponTypeCode());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CouponResponseDto createAmountCoupon(AmountCouponRequestDto amountCouponRequestDto) {
        if (hasImageFile(amountCouponRequestDto)) {
            amountCouponRequestDto.setImageFileUri(upload(amountCouponRequestDto.getImageFile()));
        }
        Coupon coupon = issueCouponAfterCreate(amountCouponRequestDto);
        checkIsCouponOfTheMonth(amountCouponRequestDto, coupon);
        createCouponBound(
                amountCouponRequestDto.getIsbn(),
                amountCouponRequestDto.getCategoryId(),
                amountCouponRequestDto.getCouponBoundCode(),
                coupon
        );

        return new CouponResponseDto(coupon.getName(), coupon.getCouponTypeCode());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CouponResponseDto createRateCoupon(RateCouponRequestDto rateCouponRequestDto) {
        if (hasImageFile(rateCouponRequestDto)) {
            rateCouponRequestDto.setImageFileUri(upload(rateCouponRequestDto.getImageFile()));
        }
        Coupon coupon = issueCouponAfterCreate(rateCouponRequestDto);
        checkIsCouponOfTheMonth(rateCouponRequestDto, coupon);
        createCouponBound(
                rateCouponRequestDto.getIsbn(),
                rateCouponRequestDto.getCategoryId(),
                rateCouponRequestDto.getCouponBoundCode(),
                coupon
        );

        return new CouponResponseDto(coupon.getName(), coupon.getCouponTypeCode());
    }

    private boolean hasImageFile(CouponRequestDto couponRequestDto) {
        return !Objects.isNull(couponRequestDto.getImageFile());
    }

    private String upload(MultipartFile file) {
        return objectStorageService.uploadObject(storageConfiguration.getContainerName(), file);
    }

    private Coupon issueCouponAfterCreate(CouponRequestDto couponRequestDto) {
        Coupon coupon = couponRepository.save(couponRequestDto.toEntity());
        TriggerTypeCode triggerTypeCode = couponRequestDto.getTriggerTypeCode();

        log.info("=== [{}] Coupon has been created.===", triggerTypeCode);

        createTrigger(triggerTypeCode, coupon);
        createCouponGroup(triggerTypeCode, coupon);

        // 생일, 회원가입, 이달의쿠폰 타입인 경우 쿠폰 요청 및 특정 발행 시점에 맞춰 발행하기 때문에 생성시에는 발행하지 않음
        if (!notToBeIssued(triggerTypeCode)) {
            CouponIssueRequestDto requestDto = new CouponIssueRequestDto(
                    couponRequestDto.getTriggerTypeCode().toString(),
                    coupon.getId(),
                    couponRequestDto.getQuantity()
            );
            issueCouponService.issueCoupon(requestDto);

            log.info(
                    "=== [{}] {} coupons with #{} has been issued. ===",
                    requestDto.getTriggerTypeCode(),
                    requestDto.getCouponId(),
                    requestDto.getQuantity()
            );
        }

        return coupon;
    }

    private void checkIsCouponOfTheMonth(CouponRequestDto couponRequestDto, Coupon coupon) {
        if (isCouponOfTheMonth(couponRequestDto)) {
            createCouponOfTheMonthPolicy(couponRequestDto, coupon);
            updateCouponOfTheMonthScheduler(
                    couponRequestDto.getCouponOpenDate(),
                    couponRequestDto.getCouponOpenTime()
            );
            redisTemplate.opsForValue().set("monthlyCouponId", coupon.getId().toString());
        }
    }

    private boolean isCouponOfTheMonth(CouponRequestDto dto) {
        return dto.getTriggerTypeCode().equals(COUPON_OF_THE_MONTH);
    }

    private void createCouponOfTheMonthPolicy(CouponRequestDto dto, Coupon coupon) {
        log.info("==== [COUPON] coupon create request dto {} ====", dto);
        CouponOfTheMonthPolicy policy = CouponOfTheMonthPolicy.builder()
                .coupon(coupon)
                .openTime(dto.getCouponOpenTime())
                .openDate(dto.getCouponOpenDate())
                .quantity(dto.getQuantity())
                .createdDateTime(null)
                .build();

        couponOfTheMonthPolicyRepository.save(policy);
    }

    private void createCouponBound(
            String isbn, Long categoryId, CouponBoundCode couponBoundCode, Coupon coupon
    ) {
        CouponBound couponBound = CouponBound.builder()
                .coupon(coupon)
                .isbn(isbn)
                .categoryId(categoryId)
                .couponBoundCode(couponBoundCode)
                .build();

        couponBoundRepository.save(couponBound);
    }

    private void createTrigger(TriggerTypeCode triggerTypeCode, Coupon coupon) {
        Trigger trigger = triggerRepository.save(Trigger.builder()
                .triggerTypeCode(triggerTypeCode)
                .coupon(coupon)
                .build());
        coupon.addTrigger(trigger);
    }

    private void createCouponGroup(TriggerTypeCode triggerTypeCode, Coupon coupon) {
        couponGroupRepository.save(CouponGroup.builder()
                .triggerTypeCode(triggerTypeCode)
                .coupon(coupon)
                .groupCode(UUID.randomUUID().toString())
                .build());
    }

    private boolean notToBeIssued(TriggerTypeCode triggerTypeCode) {
        return List.of(BIRTHDAY, SIGN_UP, COUPON_OF_THE_MONTH).contains(triggerTypeCode);
    }

    /**
     * 이달의 쿠폰을 생성하였을 시 '이달의 쿠폰 자동 발행 스케줄러'를 업데이트합니다. 등록된 이달의 쿠폰은 오픈 시간 1시간 전에 발행이 시작됩니다.
     *
     * @param openDate 등록된 쿠폰의 오픈일
     * @param openTime 등록된 쿠폰의 오픈 시간
     */
    private void updateCouponOfTheMonthScheduler(int openDate, LocalTime openTime) {
        couponOfTheCouponScheduler.stopScheduler();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO exception 정의하기
            throw new RuntimeException(e);
        }
        couponOfTheCouponScheduler.changeIssueTime(
                openDate,
                openTime.getHour() - 1,
                openTime.getMinute()
        );
        couponOfTheCouponScheduler.startScheduler();
        log.info("==== [COUPON SCHEDULER] scheduler is updated. {} {} ====", openDate, openTime);
    }
}
