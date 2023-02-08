package shop.yesaladin.coupon.coupon.service.impl;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.config.IssuanceConfiguration;
import shop.yesaladin.coupon.coupon.domain.model.CouponGivenStateCode;
import shop.yesaladin.coupon.coupon.domain.model.CouponGroup;
import shop.yesaladin.coupon.coupon.domain.model.CouponUsageStateCode;
import shop.yesaladin.coupon.coupon.domain.model.Trigger;
import shop.yesaladin.coupon.coupon.domain.repository.CommandIssuedCouponRepository;
import shop.yesaladin.coupon.coupon.domain.repository.InsertIssuedCouponRepository;
import shop.yesaladin.coupon.coupon.domain.repository.QueryCouponGroupRepository;
import shop.yesaladin.coupon.coupon.domain.repository.QueryTriggerRepository;
import shop.yesaladin.coupon.coupon.dto.CouponIssueRequestDto;
import shop.yesaladin.coupon.coupon.dto.CouponIssueResponseDto;
import shop.yesaladin.coupon.coupon.dto.IssuedCouponInsertDto;
import shop.yesaladin.coupon.coupon.service.inter.CommandIssuedCouponService;

/**
 * CouponIssuanceCommandService 인터페이스의 구현체입니다.
 *
 * @author 김홍대, 서민지
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class CommandIssuedCouponServiceImpl implements CommandIssuedCouponService {

    private final IssuanceConfiguration issuanceConfig;
    private final CommandIssuedCouponRepository commandIssuedCouponRepository;
    private final QueryTriggerRepository queryTriggerRepository;
    private final QueryCouponGroupRepository queryCouponGroupRepository;
    private final InsertIssuedCouponRepository issuanceInsertRepository;
    private final Clock clock;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<CouponIssueResponseDto> issueCoupon(CouponIssueRequestDto requestDto) {
        if (requestDto.requestWithTriggerCodeAndCouponId()) {
            return issueCouponByTriggerCodeAndCouponId(requestDto);
        }
        return issueCouponByTriggerTypeCode(requestDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public long updateCouponGivenStateAndDateTime(
            List<String> couponCodeList, CouponGivenStateCode givenStateCode
    ) {
        return commandIssuedCouponRepository.updateCouponGivenStateAndDateTime(couponCodeList,
                givenStateCode
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public long updateCouponUsageStateAndDateTime(
            List<String> couponCodeList, CouponUsageStateCode usageStateCode
    ) {
        return commandIssuedCouponRepository.updateCouponUsageStateAndDateTime(couponCodeList,
                usageStateCode
        );
    }

    private List<CouponIssueResponseDto> issueCouponByTriggerCodeAndCouponId(CouponIssueRequestDto requestDto) {
        CouponGroup couponGroup = tryGetCouponGroupByTriggerTypeAndCouponId(TriggerTypeCode.valueOf(
                requestDto.getTriggerTypeCode()), requestDto.getCouponId());

        List<IssuedCouponInsertDto> issuanceDataList = createIssuanceDataList(couponGroup,
                requestDto.getQuantity()
        );

        issuanceInsertRepository.insertIssuedCoupon(issuanceDataList);

        return List.of(createResponse(issuanceDataList, couponGroup.getGroupCode()));
    }


    private List<CouponIssueResponseDto> issueCouponByTriggerTypeCode(CouponIssueRequestDto requestDto) {
        List<CouponGroup> couponGroupList = getCouponGroupByRequestDto(requestDto);

        return couponGroupList.stream().map(couponGroup -> {
            List<IssuedCouponInsertDto> issuanceDataList = createIssuanceDataList(couponGroup,
                    requestDto.getQuantity()
            );
            issuanceInsertRepository.insertIssuedCoupon(issuanceDataList);
            return List.of(createResponse(issuanceDataList, couponGroup.getGroupCode()));
        }).findAny().orElseThrow(IllegalStateException::new);
    }

    private List<IssuedCouponInsertDto> createIssuanceDataList(
            CouponGroup couponGroup, Integer requestedQuantity
    ) {
        int issueQuantity = getCouponQuantityWillBeIssued(couponGroup, requestedQuantity);

        List<IssuedCouponInsertDto> issuanceDataList = new ArrayList<>();
        for (int count = 0; count < issueQuantity; count++) {
            IssuedCouponInsertDto insertDto = new IssuedCouponInsertDto(couponGroup.getCoupon()
                    .getId(),
                    UUID.randomUUID().toString(),
                    calculateExpirationDate(couponGroup)
            );
            issuanceDataList.add(insertDto);
        }
        return issuanceDataList;
    }

    private CouponGroup tryGetCouponGroupByTriggerTypeAndCouponId(
            TriggerTypeCode triggerTypeCode, long couponId
    ) {

        return queryCouponGroupRepository.findCouponGroupByTriggerTypeAndCouponId(triggerTypeCode,
                couponId
        ).orElseThrow(() -> new ClientException(ErrorCode.NOT_FOUND,
                "Coupon group not exists. Trigger type : " + triggerTypeCode + " Coupon : "
                        + couponId
        ));
    }

    private List<CouponGroup> getCouponGroupByRequestDto(
            CouponIssueRequestDto requestDto
    ) {
        TriggerTypeCode triggerTypeCode = TriggerTypeCode.valueOf(requestDto.getTriggerTypeCode());

        List<Trigger> triggerList = queryTriggerRepository.findTriggerByTriggerTypeCode(
                triggerTypeCode);

        if (triggerList.isEmpty()) {
            throw new ClientException(ErrorCode.TRIGGER_COUPON_NOT_FOUND,
                    "Trigger not exists for tirgger type " + triggerTypeCode
            );
        }

        return triggerList.stream()
                .map(trigger -> tryGetCouponGroupByTriggerTypeAndCouponId(trigger.getTriggerTypeCode(),
                        trigger.getCoupon().getId()
                ))
                .collect(Collectors.toList());
    }

    private int getCouponQuantityWillBeIssued(CouponGroup couponGroup, Integer requestedQuantity) {
        if (couponGroup.getCoupon().isUnlimited() && !isAutoIssuanceCoupon(couponGroup)) {
            return issuanceConfig.getUnlimitedCouponIssueSize();
        }
        return requestedQuantity;
    }

    private LocalDate calculateExpirationDate(CouponGroup couponGroup) {
        if (isAutoIssuanceCoupon(couponGroup)) {
            Integer duration = Optional.ofNullable(couponGroup.getCoupon().getDuration())
                    .orElseThrow(() -> new ClientException(ErrorCode.INVALID_COUPON_DATA,
                            "Invalid coupon data. coupon : " + couponGroup.getCoupon().getId()
                    ));
            return LocalDate.now(clock).plusDays(duration);
        }

        return Optional.ofNullable(couponGroup.getCoupon().getExpirationDate())
                .orElseThrow(() -> new ClientException(ErrorCode.INVALID_COUPON_DATA,
                        "Invalid coupon data. coupon : " + couponGroup.getCoupon().getId()
                ));
    }

    private CouponIssueResponseDto createResponse(
            List<IssuedCouponInsertDto> issuanceDataList, String couponGroupCode
    ) {
        LocalDate expirationDate = issuanceDataList.get(0).getExpirationDate();
        List<String> createdCouponCodes = issuanceDataList.stream()
                .map(IssuedCouponInsertDto::getCouponTypeCode)
                .collect(Collectors.toList());
        return new CouponIssueResponseDto(createdCouponCodes, couponGroupCode, expirationDate);
    }

    private boolean isAutoIssuanceCoupon(CouponGroup couponGroup) {
        Set<TriggerTypeCode> autoIssuanceCouponTriggerList = Set.of(TriggerTypeCode.BIRTHDAY,
                TriggerTypeCode.SIGN_UP
        );

        return autoIssuanceCouponTriggerList.contains(couponGroup.getTriggerTypeCode());
    }
}
