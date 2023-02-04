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
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
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
import shop.yesaladin.coupon.coupon.dto.TriggerWithCouponGroupCodeDto;
import shop.yesaladin.coupon.coupon.service.inter.CommandIssuedCouponService;

/**
 * CouponIssuanceCommandService 인터페이스의 구현체입니다.
 *
 * @author 김홍대
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

    @Override
    @Transactional
    public List<CouponIssueResponseDto> issueCoupon(CouponIssueRequestDto requestDto) {
        if (requestDto.requestWithTriggerCodeAndCouponId()) {
            return issueCouponByTriggerCodeAndCouponId(requestDto);
        }
        return issueCouponByTriggerTypeCode(requestDto);
    }

    @Override
    @Transactional
    public long updateCouponGivenState(
            List<String> couponCodeList, CouponGivenStateCode givenStateCode
    ) {
        return commandIssuedCouponRepository.updateCouponGivenState(couponCodeList, givenStateCode);
    }

    @Override
    public long updateCouponUsageState(
            List<String> couponCodeList,
            CouponUsageStateCode usageStateCode
    ) {
        return commandIssuedCouponRepository.updateCouponUsageState(couponCodeList, usageStateCode);
    }
    
    private List<CouponIssueResponseDto> issueCouponByTriggerCodeAndCouponId(CouponIssueRequestDto requestDto) {
        Trigger trigger = tryGetTriggerByTriggerTypeCodeAndCouponId(TriggerTypeCode.valueOf(
                requestDto.getTriggerTypeCode()), requestDto.getCouponId());
        CouponGroup couponGroup = tryGetCouponGroupByTrigger(trigger);

        List<IssuedCouponInsertDto> issuanceDataList = createIssuanceDataList(
                trigger,
                requestDto.getQuantity()
        );

        issuanceInsertRepository.insertIssuedCoupon(issuanceDataList);

        return List.of(createResponse(issuanceDataList, couponGroup.getGroupCode()));
    }


    private List<CouponIssueResponseDto> issueCouponByTriggerTypeCode(CouponIssueRequestDto requestDto) {
        List<TriggerWithCouponGroupCodeDto> triggerWithCouponGroupCodeList = getTriggerWithCouponGroupCodeDtoListByRequestDto(
                requestDto);

        return triggerWithCouponGroupCodeList.stream().map(dto -> {
            List<IssuedCouponInsertDto> issuanceDataList = createIssuanceDataList(
                    dto.getTrigger(),
                    requestDto.getQuantity()
            );
            issuanceInsertRepository.insertIssuedCoupon(issuanceDataList);
            return List.of(createResponse(issuanceDataList, dto.getCouponGroupCode()));
        }).findAny().orElseThrow(IllegalStateException::new);
    }

    private List<IssuedCouponInsertDto> createIssuanceDataList(
            Trigger trigger, Integer requestedQuantity
    ) {
        int issueQuantity = getCouponQuantityWillBeIssued(trigger.getCoupon(), requestedQuantity);

        List<IssuedCouponInsertDto> issuanceDataList = new ArrayList<>();
        for (int count = 0; count < issueQuantity; count++) {
            IssuedCouponInsertDto insertDto = new IssuedCouponInsertDto(
                    trigger.getCoupon().getId(),
                    UUID.randomUUID().toString(),
                    calculateExpirationDate(trigger)
            );
            issuanceDataList.add(insertDto);
        }
        return issuanceDataList;
    }


    private CouponGroup tryGetCouponGroupByTrigger(Trigger trigger) {
        return queryCouponGroupRepository.findCouponGroupByTrigger(trigger)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.NOT_FOUND,
                        "Coupon group not exists. Trigger type : " + trigger.getTriggerTypeCode()
                                + " Coupon : " + trigger.getCoupon().getId()
                ));
    }

    private List<TriggerWithCouponGroupCodeDto> getTriggerWithCouponGroupCodeDtoListByRequestDto(
            CouponIssueRequestDto requestDto
    ) {
        TriggerTypeCode triggerTypeCode = TriggerTypeCode.valueOf(requestDto.getTriggerTypeCode());

        List<Trigger> triggerList = queryTriggerRepository.findTriggerByTriggerTypeCode(
                triggerTypeCode);

        if (triggerList.isEmpty()) {
            throw new ClientException(
                    ErrorCode.TRIGGER_COUPON_NOT_FOUND,
                    "Trigger not exists for tirgger type " + triggerTypeCode
            );
        }

        return triggerList.stream().map(trigger -> {
            CouponGroup couponGroup = tryGetCouponGroupByTrigger(trigger);
            return new TriggerWithCouponGroupCodeDto(trigger, couponGroup.getGroupCode());
        }).collect(Collectors.toList());
    }

    private int getCouponQuantityWillBeIssued(Coupon coupon, Integer requestedQuantity) {
        if (coupon.isUnlimited()) {
            return issuanceConfig.getUnlimitedCouponIssueSize();
        }
        return requestedQuantity;
    }

    private LocalDate calculateExpirationDate(Trigger trigger) {
        if (isAutoIssuanceCoupon(trigger)) {
            Integer duration = Optional.ofNullable(trigger.getCoupon().getDuration())
                    .orElseThrow(() -> new ClientException(
                            ErrorCode.INVALID_COUPON_DATA,
                            "Invalid coupon data. coupon : " + trigger.getCoupon().getId()
                    ));
            return LocalDate.now(clock).plusDays(duration);
        }

        return Optional.ofNullable(trigger.getCoupon().getExpirationDate())
                .orElseThrow(() -> new ClientException(
                        ErrorCode.INVALID_COUPON_DATA,
                        "Invalid coupon data. coupon : " + trigger.getCoupon().getId()
                ));
    }

    private CouponIssueResponseDto createResponse(
            List<IssuedCouponInsertDto> issuanceDataList, String couponGroupCode
    ) {
        List<String> createdCouponCodes = issuanceDataList.stream()
                .map(IssuedCouponInsertDto::getCouponTypeCode)
                .collect(Collectors.toList());
        return new CouponIssueResponseDto(createdCouponCodes, couponGroupCode);
    }

    private boolean isAutoIssuanceCoupon(Trigger trigger) {
        Set<TriggerTypeCode> autoIssuanceCouponTriggerList = Set.of(
                TriggerTypeCode.BIRTHDAY,
                TriggerTypeCode.SIGN_UP
        );

        return autoIssuanceCouponTriggerList.contains(trigger.getTriggerTypeCode());
    }

    private Trigger tryGetTriggerByTriggerTypeCodeAndCouponId(
            TriggerTypeCode triggerTypeCode, long couponId
    ) {
        return queryTriggerRepository.findTriggerByTriggerTypeCodeAndCouponId(
                triggerTypeCode,
                couponId
        ).orElseThrow(() -> new ClientException(
                ErrorCode.TRIGGER_COUPON_NOT_FOUND,
                "Trigger not exists. trigger type : " + triggerTypeCode + " couponId : " + couponId
        ));
    }
}
