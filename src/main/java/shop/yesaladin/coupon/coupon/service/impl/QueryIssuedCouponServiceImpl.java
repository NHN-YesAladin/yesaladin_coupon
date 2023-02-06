package shop.yesaladin.coupon.coupon.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.CouponGroup;
import shop.yesaladin.coupon.coupon.domain.model.IssuedCoupon;
import shop.yesaladin.coupon.coupon.domain.repository.QueryCouponGroupRepository;
import shop.yesaladin.coupon.coupon.domain.repository.QueryIssuedCouponRepository;
import shop.yesaladin.coupon.coupon.dto.CouponIssueRequestDto;
import shop.yesaladin.coupon.coupon.dto.CouponIssueResponseDto;
import shop.yesaladin.coupon.coupon.service.inter.CommandIssuedCouponService;
import shop.yesaladin.coupon.coupon.service.inter.QueryIssuedCouponService;

/**
 * 발행쿠폰 관련 조회 서비스의 구현체입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class QueryIssuedCouponServiceImpl implements QueryIssuedCouponService {

    private final QueryCouponGroupRepository queryCouponGroupRepository;
    private final QueryIssuedCouponRepository queryIssuedCouponRepository;
    private final CommandIssuedCouponService commandIssuedCouponService;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<CouponIssueResponseDto> getCouponIssueResponseDtoList(CouponIssueRequestDto issueRequestDto) {
        TriggerTypeCode triggerTypeCode = TriggerTypeCode.valueOf(issueRequestDto.getTriggerTypeCode());

        // triggerTypeCode 가 SIGN_UP(회원가입)인 경우, 즉시 발행하여 넘겨준다.
        if (triggerTypeCode.equals(TriggerTypeCode.SIGN_UP)) {
            return commandIssuedCouponService.issueCoupon(issueRequestDto);
        }

        CouponGroup couponGroup = getCouponGroup(triggerTypeCode, issueRequestDto.getCouponId());
        Optional<IssuedCoupon> issuedCouponOptional = getIssuedCouponByGroupCodeId(couponGroup);

        if (issuedCouponOptional.isEmpty() && couponGroup.getCoupon().isUnlimited()) {
            commandIssuedCouponService.issueCoupon(issueRequestDto);
            issuedCouponOptional = getIssuedCouponByGroupCodeId(couponGroup);
        }

        IssuedCoupon issuedCoupon = issuedCouponOptional.orElseThrow(() -> new ClientException(
                // FIXME VALID_ISSUED_COUPON_NOT_FOUND 에러코드로 수정
                ErrorCode.COUPON_NOT_FOUND,
                ErrorCode.COUPON_NOT_FOUND.getDisplayName()
        ));

        return List.of(CouponIssueResponseDto.builder()
                .createdCouponCodes(List.of(issuedCoupon.getCouponCode()))
                .couponGroupCode(couponGroup.getGroupCode()).build());
    }

    // 트리거 타입과 쿠폰 아이디로 쿠폰그룹을 조회합니다.
    private CouponGroup getCouponGroup(TriggerTypeCode triggerTypeCode, Long couponId) {
        Optional<CouponGroup> couponGroupOptional;

        if (Objects.isNull(couponId)) {
            couponGroupOptional = queryCouponGroupRepository.findCouponGroupByTriggerType(
                    triggerTypeCode);
        } else {
            couponGroupOptional = queryCouponGroupRepository.findCouponGroupByTriggerTypeAndCouponId(
                    triggerTypeCode,
                    couponId
            );
        }

        return couponGroupOptional.orElseThrow(() -> new ClientException(
                // FIXME COUPON_GROUP_NOT_FOUND 에러코드로 수정
                ErrorCode.COUPON_NOT_FOUND,
                ErrorCode.COUPON_NOT_FOUND.getDisplayName()
        ));
    }

    private Optional<IssuedCoupon> getIssuedCouponByGroupCodeId(CouponGroup couponGroup) {
        return queryIssuedCouponRepository.findIssuedCouponByGroupCodeId(
                couponGroup.getId());
    }
}
