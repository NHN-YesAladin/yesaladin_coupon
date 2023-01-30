package shop.yesaladin.coupon.coupon.service.inter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.coupon.coupon.dto.CouponSummaryDto;

public interface QueryCouponService {

    Page<CouponSummaryDto> getTriggeredCouponList(Pageable pageable);
}
