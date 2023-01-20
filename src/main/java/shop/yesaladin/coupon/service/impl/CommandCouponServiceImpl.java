package shop.yesaladin.coupon.service.impl;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shop.yesaladin.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.domain.model.CouponBound;
import shop.yesaladin.coupon.domain.model.CouponBoundCode;
import shop.yesaladin.coupon.domain.model.Trigger;
import shop.yesaladin.coupon.domain.repository.CommandCouponBoundRepository;
import shop.yesaladin.coupon.domain.repository.CommandCouponRepository;
import shop.yesaladin.coupon.domain.repository.CommandTriggerRepository;
import shop.yesaladin.coupon.dto.AmountCouponRequestDto;
import shop.yesaladin.coupon.dto.CouponIssueRequestDto;
import shop.yesaladin.coupon.dto.CouponRequestDto;
import shop.yesaladin.coupon.dto.CouponResponseDto;
import shop.yesaladin.coupon.dto.PointCouponRequestDto;
import shop.yesaladin.coupon.dto.RateCouponRequestDto;
import shop.yesaladin.coupon.service.inter.CommandCouponService;
import shop.yesaladin.coupon.service.inter.CommandIssueCouponService;
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

    @Override
    @Transactional
    public CouponResponseDto createPointCoupon(PointCouponRequestDto pointCouponRequestDto) {
        Coupon coupon = issueCouponAfterCreate(pointCouponRequestDto);

        return new CouponResponseDto(coupon.getName(), coupon.getCouponTypeCode());
    }

    private boolean hasImageFile(CouponRequestDto couponRequestDto) {
        if (!couponRequestDto.getImageFile().isEmpty()) {
            return true;
        }
        return false;
    }

    private String upload(MultipartFile file) {
        // 인증토큰 발급 받기
        // 파일 업로드 하기
        // url 받기
    }

    @Override
    @Transactional
    public CouponResponseDto createAmountCoupon(AmountCouponRequestDto amountCouponRequestDto) {


        Coupon coupon = issueCouponAfterCreate(amountCouponRequestDto);
        createCouponBound(
                amountCouponRequestDto.getISBN(),
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
        createCouponBound(
                rateCouponRequestDto.getISBN(),
                rateCouponRequestDto.getCategoryId(),
                rateCouponRequestDto.getCouponBoundCode(),
                coupon
        );

        return new CouponResponseDto(coupon.getName(), coupon.getCouponTypeCode());
    }

    private Coupon issueCouponAfterCreate(CouponRequestDto couponRequestDto) {
        Coupon coupon = couponRepository.save(couponRequestDto.toEntity());
        createTrigger(couponRequestDto.getTriggerTypeCode(), coupon);
        issueCouponService.issueCoupon(new CouponIssueRequestDto(
                coupon.getId(),
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
        triggerRepository.save(Trigger.builder()
                .triggerTypeCode(couponRequestDto)
                .coupon(coupon)
                .build());
    }
}
