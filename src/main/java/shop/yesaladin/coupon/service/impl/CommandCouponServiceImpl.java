package shop.yesaladin.coupon.service.impl;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.domain.model.CouponBound;
import shop.yesaladin.coupon.domain.model.CouponBoundCode;
import shop.yesaladin.coupon.domain.model.Trigger;
import shop.yesaladin.coupon.domain.model.TriggerTypeCode;
import shop.yesaladin.coupon.domain.repository.CommandCouponBoundRepository;
import shop.yesaladin.coupon.domain.repository.CommandCouponRepository;
import shop.yesaladin.coupon.domain.repository.CommandTriggerRepository;
import shop.yesaladin.coupon.dto.AmountCouponRequestDto;
import shop.yesaladin.coupon.dto.CouponResponseDto;
import shop.yesaladin.coupon.dto.PointCouponRequestDto;
import shop.yesaladin.coupon.dto.RateCouponRequestDto;
import shop.yesaladin.coupon.service.inter.CommandCouponService;

@RequiredArgsConstructor
@Service
public class CommandCouponServiceImpl implements CommandCouponService {

    private final CommandCouponRepository couponRepository;
    private final CommandCouponBoundRepository couponBoundRepository;
    private final CommandTriggerRepository triggerRepository;

    // TODO 쿠폰 이미지 유무에 따라 파일 처리

    /**
     * 포인트 쿠폰을 생성합니다.
     *
     * @param couponRequestDto
     * @return
     */
    @Override
    @Transactional
    public CouponResponseDto createPointCoupon(PointCouponRequestDto couponRequestDto) {
        Coupon coupon = couponRepository.save(couponRequestDto.toEntity());
        createTrigger(couponRequestDto.getTriggerTypeCode(), coupon);

        return new CouponResponseDto(coupon.getName(), coupon.getCouponTypeCode());
    }


    /**
     * 정액할인 쿠폰을 생성합니다.
     *
     * @param couponRequestDto
     * @return
     */
    @Override
    @Transactional
    public CouponResponseDto createAmountCoupon(AmountCouponRequestDto couponRequestDto) {
        Coupon coupon = couponRepository.save(couponRequestDto.toEntity());
        createCouponBound(
                couponRequestDto.getISBN(),
                couponRequestDto.getCategoryId(),
                couponRequestDto.getCouponBoundCode(),
                coupon
        );
        createTrigger(couponRequestDto.getTriggerTypeCode(), coupon);

        return new CouponResponseDto(coupon.getName(), coupon.getCouponTypeCode());
    }


    /**
     * 정율할인 쿠폰을 생성합니다.
     *
     * @param couponRequestDto
     * @return
     */
    @Override
    @Transactional
    public CouponResponseDto createRateCoupon(RateCouponRequestDto couponRequestDto) {
        Coupon coupon = couponRepository.save(couponRequestDto.toEntity());
        createCouponBound(
                couponRequestDto.getISBN(),
                couponRequestDto.getCategoryId(),
                couponRequestDto.getCouponBoundCode(),
                coupon
        );
        createTrigger(couponRequestDto.getTriggerTypeCode(), coupon);

        return new CouponResponseDto(coupon.getName(), coupon.getCouponTypeCode());
    }

    private void createCouponBound(String ISBN, Long categoryId, CouponBoundCode couponBoundCode, Coupon coupon) {
        CouponBound couponBound = CouponBound.builder()
                .couponId(coupon.getId())
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
