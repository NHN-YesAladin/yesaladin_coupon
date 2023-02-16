package shop.yesaladin.coupon.coupon.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.coupon.coupon.domain.model.CouponOfTheMonthPolicy;
import shop.yesaladin.coupon.coupon.domain.repository.CommandCouponOfTheMonthPolicyRepository;
import shop.yesaladin.coupon.coupon.domain.repository.QueryCouponOfTheMonthPolicyRepository;
import shop.yesaladin.coupon.coupon.dto.MonthlyCouponPolicyDto;
import shop.yesaladin.coupon.coupon.service.inter.CouponOfTheMonthService;

/**
 * 이달의 쿠폰과 관련된 서비스 구현체입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CouponOfTheMonthServiceImpl implements CouponOfTheMonthService {

    private final CommandCouponOfTheMonthPolicyRepository commandCouponOfTheMonthPolicyRepository;
    private final QueryCouponOfTheMonthPolicyRepository queryCouponOfTheMonthPolicyRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public CouponOfTheMonthPolicy getLatestPolicy() {
        return queryCouponOfTheMonthPolicyRepository.findLatestCouponOfTheMonthPolicy().orElseThrow(
                () -> new ClientException(
                        ErrorCode.COUPON_POLICY_NOT_FOUND,
                        "이달의 쿠폰 정책이 존재하지 않습니다."
                )
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void createPolicy(MonthlyCouponPolicyDto dto) {
        CouponOfTheMonthPolicy policy = CouponOfTheMonthPolicy.builder()
                .coupon(dto.getCoupon())
                .openTime(dto.getOpenTime())
                .openDate(dto.getOpenDate())
                .quantity(dto.getCouponQuantity())
                .createdDateTime(null)
                .build();

        commandCouponOfTheMonthPolicyRepository.save(policy);
    }
}
