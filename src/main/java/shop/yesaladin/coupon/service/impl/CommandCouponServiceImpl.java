package shop.yesaladin.coupon.service.impl;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.domain.model.CouponBound;
import shop.yesaladin.coupon.domain.model.CouponTypeCode;
import shop.yesaladin.coupon.domain.repository.CommandCouponBoundRepository;
import shop.yesaladin.coupon.domain.repository.CommandCouponRepository;
import shop.yesaladin.coupon.domain.repository.CommandTriggerRepository;
import shop.yesaladin.coupon.dto.CouponRequestDto;
import shop.yesaladin.coupon.dto.CouponResponseDto;
import shop.yesaladin.coupon.service.inter.CommandCouponService;

@RequiredArgsConstructor
@Service
public class CommandCouponServiceImpl implements CommandCouponService {
    private final CommandCouponRepository couponRepository;
    private final CommandCouponBoundRepository couponBoundRepository;
    private final CommandTriggerRepository triggerRepository;

    @Override
    @Transactional
    public CouponResponseDto createCoupon(CouponRequestDto couponRequestDto) {
        CouponTypeCode couponTypeCode = couponRequestDto.getCouponTypeCode();

        Coupon coupon = couponRepository.save(couponRequestDto.toEntity(couponTypeCode));

        // 쿠폰 생성에 따라 쿠폰 적용 범위 테이블에 레코드 생성
        // 쿠폰 타입이 포인트 충전 쿠폰인 경우 쿠폰 적용 범위를 생성하지 않음
        if (couponTypeCode != CouponTypeCode.POINT) {
            createCouponBound(couponRequestDto, coupon);
        }

        // TODO 쿠폰 이미지 유무에 따라 파일 처리

        // [자동 발행]
        // 쿠폰 발급 대상자가 생일자인 경우
        // 쿠폰 발급 대상자가 회원가입 대상자인 경우
        CouponIssueTargetCode issueTargetCode = couponRequestDto.getIssueTargetCode();
        int issueTargetCodeId = issueTargetCode.getCode();

        if (issueTargetCodeId == 100 || issueTargetCodeId == 101) {
            // TODO 이벤트 담당 쿠폰이 1개만 존재해야 하는 경우 처리

            // 이벤트 발행 쿠폰 테이블에 레코드 생성
            triggerRepository.save(CouponEvent.builder()
                    .couponEventCode(issueTargetCode.getCouponEventCode())
                    .coupon(coupon)
                    .build()
            );
        }

        return new CouponResponseDto(coupon.getName(),
                coupon.getDiscountRate(),
                coupon.getDiscountAmount(),
                coupon.getChargePointAmount(),
                coupon.getCouponTypeCode()
        );
    }

    // transactional?
    private void createCouponBound(CouponRequestDto couponRequestDto, Coupon coupon) {
        CouponBound couponBound = CouponBound.builder()
                .couponId(coupon.getId())
                .coupon(coupon)
                .ISBN(couponRequestDto.getISBN())
                .categoryId(couponRequestDto.getCategoryId())
                .couponBoundCode(couponRequestDto.getCouponBoundCode())
                .build();

        couponBoundRepository.save(couponBound);
    }
}
