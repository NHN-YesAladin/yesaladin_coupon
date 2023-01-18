package shop.yesaladin.coupon.service.impl;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.coupon.config.IssuanceConfiguration;
import shop.yesaladin.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.domain.model.TriggerTypeCode;
import shop.yesaladin.coupon.domain.repository.InsertIssuedCouponRepository;
import shop.yesaladin.coupon.domain.repository.QueryCouponRepository;
import shop.yesaladin.coupon.dto.CouponIssueRequestDto;
import shop.yesaladin.coupon.dto.CouponIssueResponseDto;
import shop.yesaladin.coupon.dto.IssuedCouponInsertDto;
import shop.yesaladin.coupon.exception.CouponNotFoundException;
import shop.yesaladin.coupon.exception.InvalidCouponDataException;
import shop.yesaladin.coupon.service.inter.CommandIssueCouponService;

/**
 * CouponIssuanceCommandService 인터페이스의 구현체입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class CommandIssueCouponServiceImpl implements CommandIssueCouponService {

    private final IssuanceConfiguration issuanceConfig;
    private final QueryCouponRepository queryCouponRepository;
    private final InsertIssuedCouponRepository issuanceInsertRepository;
    private final Clock clock;

    @Override
    @Transactional
    public CouponIssueResponseDto issueCoupon(CouponIssueRequestDto requestDto) {
        Coupon coupon = tryGetCouponById(requestDto.getCouponId());
        List<IssuedCouponInsertDto> issuanceDataList = createIssuanceDataList(
                coupon,
                requestDto.getQuantity()
        );

        issuanceInsertRepository.insertIssuedCoupon(issuanceDataList);

        return createResponse(issuanceDataList);
    }

    private List<IssuedCouponInsertDto> createIssuanceDataList(
            Coupon coupon, Integer requestedQuantity
    ) {
        int issueQuantity = getCouponQuantityWillBeIssued(coupon, requestedQuantity);

        List<IssuedCouponInsertDto> issuanceDataList = new ArrayList<>();
        for (int count = 0; count < issueQuantity; count++) {
            IssuedCouponInsertDto insertDto = new IssuedCouponInsertDto(
                    coupon.getId(),
                    UUID.randomUUID().toString(),
                    calculateExpirationDate(coupon)
            );
            issuanceDataList.add(insertDto);
        }
        return issuanceDataList;
    }

    private Coupon tryGetCouponById(long couponId) {
        return queryCouponRepository.findCouponById(couponId)
                .orElseThrow(() -> new CouponNotFoundException(couponId));
    }

    private int getCouponQuantityWillBeIssued(Coupon coupon, Integer requestedQuantity) {
        if (Objects.nonNull(requestedQuantity)) {
            return requestedQuantity;
        }
        int issueQuantity = coupon.getQuantity();
        if (issueQuantity == issuanceConfig.getUnlimitedFlag()) {
            return issuanceConfig.getUnlimitedCouponIssueSize();
        }
        return issueQuantity;
    }

    private LocalDate calculateExpirationDate(Coupon coupon) {
        if (isAutoIssuanceCoupon(coupon)) {
            Integer duration = Optional.ofNullable(coupon.getDuration())
                    .orElseThrow(() -> new InvalidCouponDataException(coupon.getId()));
            return LocalDate.now(clock).plusDays(duration);
        }

        return Optional.ofNullable(coupon.getExpirationDate())
                .orElseThrow(() -> new InvalidCouponDataException(coupon.getId()));
    }

    private CouponIssueResponseDto createResponse(List<IssuedCouponInsertDto> issuanceDataList) {
        List<String> createdCouponCodes = issuanceDataList.stream()
                .map(IssuedCouponInsertDto::getCouponCode)
                .collect(Collectors.toList());
        return new CouponIssueResponseDto(createdCouponCodes);
    }

    private boolean isAutoIssuanceCoupon(Coupon coupon) {
        List<TriggerTypeCode> autoIssuanceCouponTriggerList = List.of(
                TriggerTypeCode.BIRTHDAY,
                TriggerTypeCode.SIGN_UP
        );

        return coupon.getTriggerList()
                .stream()
                .anyMatch(trigger -> autoIssuanceCouponTriggerList.contains(trigger.getTriggerTypeCode()));
    }
}
