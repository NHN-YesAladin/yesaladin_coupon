package shop.yesaladin.coupon.coupon.service.impl;


import static shop.yesaladin.coupon.code.TriggerTypeCode.BIRTHDAY;
import static shop.yesaladin.coupon.code.TriggerTypeCode.SIGN_UP;

import java.util.Objects;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.config.StorageConfiguration;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.CouponBound;
import shop.yesaladin.coupon.coupon.domain.model.CouponBoundCode;
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
import shop.yesaladin.coupon.coupon.dto.PointCouponRequestDto;
import shop.yesaladin.coupon.coupon.dto.RateCouponRequestDto;
import shop.yesaladin.coupon.coupon.service.inter.CommandCouponService;
import shop.yesaladin.coupon.coupon.service.inter.CommandIssueCouponService;
import shop.yesaladin.coupon.file.service.inter.ObjectStorageService;

/**
 * CommandCouponService 인터페이스의 구현체 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class CommandCouponServiceImpl implements CommandCouponService {

    private final CommandCouponRepository couponRepository;
    private final CommandCouponBoundRepository couponBoundRepository;
    private final CommandTriggerRepository triggerRepository;
    private final CommandCouponGroupRepository couponGroupRepository;
    private final CommandIssueCouponService issueCouponService;
    private final ObjectStorageService objectStorageService;
    private final StorageConfiguration storageConfiguration;

    @Override
    @Transactional
    public CouponResponseDto createPointCoupon(PointCouponRequestDto pointCouponRequestDto) {
        if (hasImageFile(pointCouponRequestDto)) {
            pointCouponRequestDto.setImageFileUri(upload(pointCouponRequestDto.getImageFile()));
        }
        Coupon coupon = issueCouponAfterCreate(pointCouponRequestDto);

        return new CouponResponseDto(coupon.getName(), coupon.getCouponTypeCode());
    }

    @Override
    @Transactional
    public CouponResponseDto createAmountCoupon(AmountCouponRequestDto amountCouponRequestDto) {
        if (hasImageFile(amountCouponRequestDto)) {
            amountCouponRequestDto.setImageFileUri(upload(amountCouponRequestDto.getImageFile()));
        }
        Coupon coupon = issueCouponAfterCreate(amountCouponRequestDto);
        createCouponBound(
                amountCouponRequestDto.getIsbn(),
                amountCouponRequestDto.getCategoryId(),
                amountCouponRequestDto.getCouponBoundCode(),
                coupon
        );

        return new CouponResponseDto(coupon.getName(), coupon.getCouponTypeCode());
    }

    @Override
    @Transactional
    public CouponResponseDto createRateCoupon(RateCouponRequestDto rateCouponRequestDto) {
        if (hasImageFile(rateCouponRequestDto)) {
            rateCouponRequestDto.setImageFileUri(upload(rateCouponRequestDto.getImageFile()));
        }
        Coupon coupon = issueCouponAfterCreate(rateCouponRequestDto);
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

        createTrigger(triggerTypeCode, coupon);
        createCouponGroup(triggerTypeCode, coupon);

        // 생일 쿠폰, 회원가입 쿠폰의 경우 쿠폰 요청에 맞춰 발행하기 때문에 생성시에는 발행하지 않음
        if (BIRTHDAY.equals(triggerTypeCode) || SIGN_UP.equals(triggerTypeCode)) {
            return coupon;
        }

        issueCouponService.issueCoupon(new CouponIssueRequestDto(
                couponRequestDto.getCouponTypeCode().toString(),
                coupon.getId(),
                couponRequestDto.getQuantity()
        ));

        return coupon;
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
}
