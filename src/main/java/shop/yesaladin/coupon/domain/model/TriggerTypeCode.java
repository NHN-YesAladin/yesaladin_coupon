package shop.yesaladin.coupon.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 쿠폰 발행 트리거 코드 입니다.
 *
 * @author 서민지, 김홍대
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public enum TriggerTypeCode {

    SIGN_UP(1),
    BIRTHDAY(2),
    COUPON_OF_THE_MONTH(3),
    MEMBER_GRADE_WHITE(101),
    MEMBER_GRADE_BRONZE(102),
    MEMBER_GRADE_SILVER(103),
    MEMBER_GRADE_GOLD(104),
    MEMBER_GRADE_PLATINUM(105);

    private final int code;
}