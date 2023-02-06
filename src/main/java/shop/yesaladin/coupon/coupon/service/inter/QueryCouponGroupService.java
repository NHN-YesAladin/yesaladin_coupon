package shop.yesaladin.coupon.coupon.service.inter;

import java.util.List;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.dto.CouponGroupAndLimitDto;

/**
 * 쿠폰 그룹에 관련된 정보를 조회하는 서비스 인터페이스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
public interface QueryCouponGroupService {

    /**
     * 트리거 타입 코드와 쿠폰 아이디로 활성화된 쿠폰 지급에 필요한 정보를 가져옵니다. 쿠폰 아이디가 null일 경우 트리거 타입 코드로만 조회합니다.
     *
     * @param triggerTypeCode 조회할 트리거 타입
     * @param couponId        조회할 쿠폰 아이디
     * @return 조회된 dto 리스트
     */
    List<CouponGroupAndLimitDto> getCouponGroupAndLimit(
            TriggerTypeCode triggerTypeCode,
            Long couponId
    );
}
