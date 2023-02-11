package shop.yesaladin.coupon.coupon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.coupon.code.CouponTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.CouponBound;
import shop.yesaladin.coupon.coupon.domain.repository.QueryCouponBoundRepository;
import shop.yesaladin.coupon.coupon.domain.repository.QueryCouponRepository;
import shop.yesaladin.coupon.coupon.dto.CouponBoundResponseDto;
import shop.yesaladin.coupon.coupon.service.inter.QueryCouponBoundService;

@RequiredArgsConstructor
@Service
public class QueryCouponBoundServiceImpl implements QueryCouponBoundService {

    private final QueryCouponBoundRepository queryCouponBoundRepository;
    private final QueryCouponRepository queryCouponRepository;

    @Override
    @Transactional(readOnly = true)
    public CouponBoundResponseDto getCouponBoundByCouponId(long couponId) {
        Coupon coupon = tryGetCouponByCouponId(couponId);

        if (coupon.getCouponTypeCode().equals(CouponTypeCode.POINT)) {
            return CouponBoundResponseDto.emptyBound(couponId);
        }

        CouponBound couponBound = tryGetCouponBoundByCouponId(couponId);
        return CouponBoundResponseDto.fromEntity(couponBound);
    }

    private Coupon tryGetCouponByCouponId(long couponId) {
        return queryCouponRepository.findCouponById(couponId)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.COUPON_NOT_FOUND,
                        "Coupon not exists. Coupon id : " + couponId
                ));
    }

    private CouponBound tryGetCouponBoundByCouponId(long couponId) {
        return queryCouponBoundRepository.findCouponBoundByCouponId(couponId)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.NOT_FOUND,
                        "Coupon bound data not exist. Coupon id : " + couponId
                ));
    }
}
