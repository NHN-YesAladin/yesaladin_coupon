package shop.yesaladin.coupon.coupon.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.repository.QueryCouponRepository;
import shop.yesaladin.coupon.coupon.dto.CouponIssueRequestDto;
import shop.yesaladin.coupon.coupon.dto.CouponIssueResponseDto;
import shop.yesaladin.coupon.coupon.service.inter.CommandIssuedCouponService;
import shop.yesaladin.coupon.coupon.service.inter.QueryIssuedCouponService;

@RequiredArgsConstructor
@Service
public class QueryIssuedCouponServiceImpl implements QueryIssuedCouponService {

    private final QueryCouponRepository queryCouponRepository;
    private final CommandIssuedCouponService issuedCouponService;

    @Override
    @Transactional(readOnly = true)
    public List<CouponIssueResponseDto> getIssuedCoupon(CouponIssueRequestDto issueRequestDto) {
        TriggerTypeCode trigger = TriggerTypeCode.valueOf(issueRequestDto.getTriggerTypeCode());

        // triggerTypeCode 가 SIGN_UP(회원가입)인 경우, 즉시 발행하여 넘겨준다.
        if (trigger.equals(TriggerTypeCode.SIGN_UP)) {
            return issuedCouponService.issueCoupon(issueRequestDto);
        }

        // triggerTypeCode 가 COUPON_OF_THE_MONTH(이달쿠)인 경우
        if (trigger.equals(TriggerTypeCode.COUPON_OF_THE_MONTH)) {
            // 이달의쿠폰정책 테이블에서 couponId 를 조회하여 해당 쿠폰
        }

        // triggerTypeCode 와 couponId 를 가지는 요청인 경우(일반 다운로드)

        // triggerTypeCode 만으로 발행쿠폰을 지급하는 경우
        List<Coupon> couponList = queryCouponRepository.findCouponByTriggerCode(
                trigger);

        // 각 쿠폰에 대해 그룹코드와 쿠폰코드를 조회한다.

        return null;
    }
}
