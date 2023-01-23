package shop.yesaladin.coupon.coupon.service.impl;

import java.io.InputStream;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.CouponBound;
import shop.yesaladin.coupon.coupon.domain.model.CouponBoundCode;
import shop.yesaladin.coupon.coupon.domain.model.Trigger;
import shop.yesaladin.coupon.coupon.domain.repository.CommandCouponBoundRepository;
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
import shop.yesaladin.coupon.trigger.TriggerTypeCode;

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
    private final CommandIssueCouponService issueCouponService;
    private final ObjectStorageService objectStorageService;

    @Override
    @Transactional
    public CouponResponseDto createPointCoupon(PointCouponRequestDto pointCouponRequestDto) {
        if (hasImageFile(pointCouponRequestDto)) {
            String url = upload(pointCouponRequestDto.getImageFile());
            pointCouponRequestDto.setImageFileUri(url);
        }
        Coupon coupon = issueCouponAfterCreate(pointCouponRequestDto);

        return new CouponResponseDto(coupon.getName(), coupon.getCouponTypeCode());
    }

    private boolean hasImageFile(CouponRequestDto couponRequestDto) {
        return !couponRequestDto.getImageFile().isEmpty();
    }

    private String upload(MultipartFile file) {
        UUID randomUUID = UUID.randomUUID();
        String contentType = file.getContentType();
        InputStream inputStream = (InputStream) file;
        String fileName = randomUUID + contentType;

        return objectStorageService.uploadObject(null, fileName, inputStream);
    }

    @Override
    @Transactional
    public CouponResponseDto createAmountCoupon(AmountCouponRequestDto amountCouponRequestDto) {

        Coupon coupon = issueCouponAfterCreate(amountCouponRequestDto);
        createCouponBound(amountCouponRequestDto.getISBN(),
                amountCouponRequestDto.getCategoryId(),
                amountCouponRequestDto.getCouponBoundCode(),
                coupon
        );

        return new CouponResponseDto(coupon.getName(), coupon.getCouponTypeCode());
    }

    @Override
    @Transactional
    public CouponResponseDto createRateCoupon(RateCouponRequestDto rateCouponRequestDto) {
        Coupon coupon = issueCouponAfterCreate(rateCouponRequestDto);
        createCouponBound(rateCouponRequestDto.getISBN(),
                rateCouponRequestDto.getCategoryId(),
                rateCouponRequestDto.getCouponBoundCode(),
                coupon
        );

        return new CouponResponseDto(coupon.getName(), coupon.getCouponTypeCode());
    }

    private Coupon issueCouponAfterCreate(CouponRequestDto couponRequestDto) {
        Coupon coupon = couponRepository.save(couponRequestDto.toEntity());
        createTrigger(couponRequestDto.getTriggerTypeCode(), coupon);
        issueCouponService.issueCoupon(new CouponIssueRequestDto(coupon.getId(),
                couponRequestDto.getQuantity()
        ));
        return coupon;
    }

    private void createCouponBound(
            String ISBN, Long categoryId, CouponBoundCode couponBoundCode, Coupon coupon
    ) {
        CouponBound couponBound = CouponBound.builder()
                .coupon(coupon)
                .ISBN(ISBN)
                .categoryId(categoryId)
                .couponBoundCode(couponBoundCode)
                .build();

        couponBoundRepository.save(couponBound);
    }

    private void createTrigger(TriggerTypeCode couponRequestDto, Coupon coupon) {
        Trigger trigger = triggerRepository.save(Trigger.builder()
                .triggerTypeCode(couponRequestDto)
                .coupon(coupon)
                .build());
        coupon.addTrigger(trigger);
    }
}
