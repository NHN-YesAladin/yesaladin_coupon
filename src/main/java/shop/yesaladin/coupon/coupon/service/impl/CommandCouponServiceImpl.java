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
 * CommandCouponService ?????????????????? ????????? ?????????.
 *
 * @author ?????????
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

        // ??????, ????????????, ??????????????? ????????? ?????? ?????? ?????? ??? ?????? ?????? ????????? ?????? ???????????? ????????? ??????????????? ???????????? ??????
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
     * ????????? ?????? ????????? ????????? ?????? ????????? ???????????? ????????? ?????? 1?????? ?????? ?????? ????????? ???????????????.
     *
     * @param dto    ????????? ?????? ?????? ????????? ?????? dto
     * @param coupon ????????? ????????? ??????
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
     * redis ??? ????????? ????????? ????????? ?????? ????????? ??????(?????? ?????????, ?????? ?????? ??? ??????)??? ???????????????.
     *
     * @param couponRequestDto ????????? ?????? ?????? ????????? ?????? dto
     * @param coupon           ????????? ????????? ??????
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
     * ????????? ????????? ??????????????? ??? '????????? ?????? ?????? ?????? ????????????'??? ?????????????????????. ????????? ????????? ????????? ?????? ?????? 1?????? ?????? ????????? ???????????????.
     *
     * @param openDate ????????? ????????? ?????????
     * @param openTime ????????? ????????? ?????? ??????
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
