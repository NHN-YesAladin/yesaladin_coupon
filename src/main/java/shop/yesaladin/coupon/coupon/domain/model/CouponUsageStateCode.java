package shop.yesaladin.coupon.coupon.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 쿠폰 사용 상태 코드입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public enum CouponUsageStateCode {

    NOT_USED(1, "미사용"), PENDING_USE(2, "사용 대기"), USED(3, "사용 완료");

    private final int code;
    private final String displayName;
}
