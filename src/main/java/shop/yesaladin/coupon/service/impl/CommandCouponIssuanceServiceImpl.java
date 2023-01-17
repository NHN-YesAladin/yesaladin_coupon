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
import shop.yesaladin.coupon.domain.model.CouponCode;
import shop.yesaladin.coupon.domain.repository.InsertCouponIssuanceRepository;
import shop.yesaladin.coupon.domain.repository.QueryCouponRepository;
import shop.yesaladin.coupon.dto.CouponIssuanceInsertDto;
import shop.yesaladin.coupon.dto.CouponIssuanceRequestDto;
import shop.yesaladin.coupon.dto.CouponIssuanceResponseDto;
import shop.yesaladin.coupon.exception.CouponNotFoundException;
import shop.yesaladin.coupon.exception.InvalidCouponDataException;
import shop.yesaladin.coupon.service.inter.CommandCouponIssuanceService;

/**
 * CouponIssuanceCommandService 인터페이스의 구현체입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class CommandCouponIssuanceServiceImpl implements CommandCouponIssuanceService {

    private final IssuanceConfiguration issuanceConfig;
    private final QueryCouponRepository queryCouponRepository;
    private final InsertCouponIssuanceRepository issuanceInsertRepository;
    private final Clock clock;

    @Override
    @Transactional
    public CouponIssuanceResponseDto issueCoupon(CouponIssuanceRequestDto requestDto) {
        Coupon coupon = tryGetCouponById(requestDto.getCouponId());
        List<CouponIssuanceInsertDto> issuanceDataList = createIssuanceDataList(
                coupon,
                requestDto.getQuantity()
        );

        issuanceInsertRepository.insertCouponIssuance(issuanceDataList);

        return createResponse(issuanceDataList);
    }

    private List<CouponIssuanceInsertDto> createIssuanceDataList(
            Coupon coupon,
            Integer requestedQuantity
    ) {
        int issueQuantity = getCouponQuantityWillBeIssued(coupon, requestedQuantity);

        List<CouponIssuanceInsertDto> issuanceDataList = new ArrayList<>();
        for (int count = 0; count < issueQuantity; count++) {
            CouponIssuanceInsertDto insertDto = new CouponIssuanceInsertDto(
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
        if (coupon.getIssuanceCode().equals(CouponCode.AUTO_ISSUANCE)) {
            Integer duration = Optional.ofNullable(coupon.getDuration())
                    .orElseThrow(() -> new InvalidCouponDataException(coupon.getId()));
            return LocalDate.now(clock).plusDays(duration);
        }

        return Optional.ofNullable(coupon.getExpirationDate())
                .orElseThrow(() -> new InvalidCouponDataException(coupon.getId()));
    }

    private CouponIssuanceResponseDto createResponse(List<CouponIssuanceInsertDto> issuanceDataList) {
        List<String> createdCouponCodes = issuanceDataList.stream()
                .map(CouponIssuanceInsertDto::getCouponCode)
                .collect(Collectors.toList());
        return new CouponIssuanceResponseDto(createdCouponCodes);
    }
}
