package shop.yesaladin.coupon.service.impl;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.coupon.config.IssuanceConfiguration;
import shop.yesaladin.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.domain.model.CouponCode;
import shop.yesaladin.coupon.domain.repository.CouponIssuanceInsertRepository;
import shop.yesaladin.coupon.domain.repository.CouponQueryRepository;
import shop.yesaladin.coupon.dto.CouponIssuanceInsertDto;
import shop.yesaladin.coupon.exception.CouponNotFoundException;
import shop.yesaladin.coupon.exception.InvalidCouponDataException;
import shop.yesaladin.coupon.service.inter.CouponIssuanceCommandService;

/**
 * CouponIssuanceCommandService 인터페이스의 구현체입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class CouponIssuanceCommandServiceImpl implements CouponIssuanceCommandService {

    private final IssuanceConfiguration issuanceConfig;
    private final CouponQueryRepository couponQueryRepository;
    private final CouponIssuanceInsertRepository issuanceInsertRepository;
    private final Clock clock;

    @Override
    @Transactional
    public List<String> issueCoupon(long couponId) {
        Coupon coupon = tryGetCouponById(couponId);
        List<CouponIssuanceInsertDto> issuanceDataList = createIssuanceDataList(coupon);
        issuanceInsertRepository.insertCouponIssuance(issuanceDataList);

        return issuanceDataList.stream()
                .map(CouponIssuanceInsertDto::getCouponCode)
                .collect(Collectors.toList());
    }

    private List<CouponIssuanceInsertDto> createIssuanceDataList(Coupon coupon) {
        int issueQuantity = getCouponQuantityWillBeIssued(coupon);

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
        return couponQueryRepository.findCouponById(couponId)
                .orElseThrow(() -> new CouponNotFoundException(couponId));
    }

    private int getCouponQuantityWillBeIssued(Coupon coupon) {
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
}
