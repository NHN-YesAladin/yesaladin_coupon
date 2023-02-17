package shop.yesaladin.coupon.coupon.service.impl;


import static shop.yesaladin.coupon.code.TriggerTypeCode.BIRTHDAY;
import static shop.yesaladin.coupon.code.TriggerTypeCode.COUPON_OF_THE_MONTH;
import static shop.yesaladin.coupon.code.TriggerTypeCode.SIGN_UP;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import shop.yesaladin.coupon.coupon.domain.model.Trigger;
import shop.yesaladin.coupon.coupon.domain.repository.CommandCouponBoundRepository;
import shop.yesaladin.coupon.coupon.domain.repository.CommandCouponGroupRepository;
import shop.yesaladin.coupon.coupon.domain.repository.CommandCouponRepository;
import shop.yesaladin.coupon.coupon.domain.repository.CommandTriggerRepository;
import shop.yesaladin.coupon.coupon.dto.AmountCouponRequestDto;
import shop.yesaladin.coupon.coupon.dto.CouponIssueRequestDto;
import shop.yesaladin.coupon.coupon.dto.CouponRequestDto;
import shop.yesaladin.coupon.coupon.dto.CouponResponseDto;
import shop.yesaladin.coupon.coupon.dto.MonthlyCouponPolicyDto;
import shop.yesaladin.coupon.coupon.dto.PointCouponRequestDto;
import shop.yesaladin.coupon.coupon.dto.RateCouponRequestDto;
import shop.yesaladin.coupon.coupon.service.inter.CommandCouponService;
import shop.yesaladin.coupon.coupon.service.inter.CommandIssuedCouponService;
import shop.yesaladin.coupon.coupon.service.inter.CouponOfTheMonthService;
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

    private static final String MONTHLY_POLICY_KEY = "monthlyCouponPolicy";
    private static final String MONTHLY_COUPON_ID_KEY = "monthlyCouponId";
    private static final String MONTHLY_COUPON_OPEN_DATE_TIME_KEY = "monthlyCouponOpenDateTime";

    private final CommandCouponRepository couponRepository;
    private final CommandCouponBoundRepository couponBoundRepository;
    private final CommandTriggerRepository triggerRepository;
    private final CommandCouponGroupRepository couponGroupRepository;
    private final CommandIssuedCouponService issueCouponService;
    private final ObjectStorageService objectStorageService;
    private final StorageConfiguration storageConfiguration;
    private final CouponOfTheMonthService couponOfTheMonthService;
    private final CouponOfTheCouponScheduler couponOfTheCouponScheduler;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CouponResponseDto createPointCoupon(PointCouponRequestDto pointCouponRequestDto) {
        if (hasImageFile(pointCouponRequestDto)) {
            pointCouponRequestDto.setImageFileUri(getImageUriByUpload(pointCouponRequestDto.getImageFile()));
        }
        Coupon coupon = createAndIssue(pointCouponRequestDto);
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
            amountCouponRequestDto.setImageFileUri(getImageUriByUpload(amountCouponRequestDto.getImageFile()));
        }
        Coupon coupon = createAndIssue(amountCouponRequestDto);
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
            rateCouponRequestDto.setImageFileUri(getImageUriByUpload(rateCouponRequestDto.getImageFile()));
        }
        Coupon coupon = createAndIssue(rateCouponRequestDto);
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
        return Objects.nonNull(couponRequestDto.getImageFile());
    }

    private String getImageUriByUpload(MultipartFile file) {
        return objectStorageService.uploadObject(storageConfiguration.getContainerName(), file);
    }

    private Coupon createAndIssue(CouponRequestDto couponRequestDto) {
        Coupon coupon = couponRepository.save(couponRequestDto.toEntity());
        TriggerTypeCode triggerTypeCode = couponRequestDto.getTriggerTypeCode();

        log.info("=== [{}] Coupon has been created.===", triggerTypeCode);

        createTrigger(triggerTypeCode, coupon);
        createCouponGroup(triggerTypeCode, coupon);

        issueCoupon(couponRequestDto, coupon.getId());

        return coupon;
    }

    private void issueCoupon(CouponRequestDto couponRequestDto, long couponId) {
        TriggerTypeCode triggerTypeCode = couponRequestDto.getTriggerTypeCode();

        // 생일, 회원가입, 이달의쿠폰 타입인 경우 쿠폰 요청 및 특정 발행 시점에 맞춰 발행하기 때문에 생성시에는 발행하지 않음
        if (!notToBeAutoIssued(triggerTypeCode)) {
            CouponIssueRequestDto requestDto = new CouponIssueRequestDto(
                    triggerTypeCode.toString(),
                    couponId,
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
    }

    /**
     * 이달의 쿠폰 생성시 이달의 쿠폰 정책을 생성하고 이벤트 오픈 1시간 전에 쿠폰 발행을 예약합니다.
     *
     * @param dto    이달의 쿠폰 생성 정보를 담은 dto
     * @param coupon 생성된 이달의 쿠폰
     */
    private void checkIsCouponOfTheMonth(CouponRequestDto dto, Coupon coupon) {
        if (isCouponOfTheMonth(dto.getTriggerTypeCode())) {
            couponOfTheMonthService.createPolicy(new MonthlyCouponPolicyDto(
                    coupon,
                    dto.getCouponOpenTime(),
                    dto.getCouponOpenDate(),
                    dto.getQuantity()
            ));
            updateCouponOfTheMonthScheduler(dto.getCouponOpenDate(), dto.getCouponOpenTime());
            storeMonthlyCouponEventInfo(dto, coupon);
        }
    }

    /**
     * redis 에 신규로 등록된 이달의 쿠폰 이벤트 정보(쿠폰 아이디, 오픈 날짜 및 시간)를 저장합니다.
     *
     * @param couponRequestDto 이달의 쿠폰 생성 정보를 담은 dto
     * @param coupon           생성된 이달의 쿠폰
     */
    private void storeMonthlyCouponEventInfo(CouponRequestDto couponRequestDto, Coupon coupon) {
        LocalDate openDate = LocalDate.now().withDayOfMonth(couponRequestDto.getCouponOpenDate());
        LocalDateTime openDateTime = LocalDateTime.of(
                openDate,
                couponRequestDto.getCouponOpenTime()
        );
        redisTemplate.opsForHash()
                .put(MONTHLY_POLICY_KEY, MONTHLY_COUPON_ID_KEY, coupon.getId().toString());
        redisTemplate.opsForHash()
                .put(
                        MONTHLY_POLICY_KEY,
                        MONTHLY_COUPON_OPEN_DATE_TIME_KEY,
                        openDateTime.toString()
                );
    }

    private boolean isCouponOfTheMonth(TriggerTypeCode triggerTypeCode) {
        return triggerTypeCode.equals(COUPON_OF_THE_MONTH);
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

    private boolean notToBeAutoIssued(TriggerTypeCode triggerTypeCode) {
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
        couponOfTheCouponScheduler.changeIssueTime(
                openDate,
                openTime.getHour(),
                openTime.getMinute()
        );
        couponOfTheCouponScheduler.startScheduler();
        log.info("==== [COUPON SCHEDULER] scheduler is updated. {} {} ====", openDate, openTime);
    }
}
