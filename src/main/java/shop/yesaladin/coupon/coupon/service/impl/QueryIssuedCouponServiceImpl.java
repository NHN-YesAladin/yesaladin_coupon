package shop.yesaladin.coupon.coupon.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    @Transactional
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
                ErrorCode.ISSUED_COUPON_NOT_FOUND, "The coupon quantity prepared has been exhausted."));

        return List.of(CouponIssueResponseDto.builder()
                .createdCouponCodes(List.of(issuedCoupon.getCouponCode()))
                .couponGroupCode(couponGroup.getGroupCode())
                .expirationDate(issuedCoupon.getExpirationDate())
                .build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<IssuedCoupon> checkUnavailableIssuedCoupon(
            List<String> couponCodeList, LocalDateTime requestDateTime
    ) {
        return queryIssuedCouponRepository.checkUnavailableIssuedCoupon(
                couponCodeList,
                requestDateTime
        );
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
                ErrorCode.COUPON_GROUP_NOT_FOUND,
                "Coupon group not found. couponId: " + couponId + ", triggerTypeCode: "
                        + triggerTypeCode
        ));
    }

    private Optional<IssuedCoupon> getIssuedCouponByGroupCodeId(CouponGroup couponGroup) {
        log.info(
                "=== [COUPON] get issued coupon by groupCode ({}, id: {}). ===",
                couponGroup.getGroupCode(),
                couponGroup.getId()
        );
        return queryIssuedCouponRepository.findIssuedCouponByGroupCodeId(couponGroup.getId());
    }
}
