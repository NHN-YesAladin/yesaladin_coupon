package shop.yesaladin.coupon.service.inter;

import shop.yesaladin.coupon.dto.CouponIssuanceRequestDto;
import shop.yesaladin.coupon.dto.CouponIssuanceResponseDto;

/**
 * 쿠폰 발행과 관련된 CUD 작업을 하는 서비스 인터페이스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
public interface CommandCouponIssuanceService {

    /**
     * 쿠폰 테이블에 있는 데이터를 바탕으로 쿠폰을 발행하는 메서드입니다.
     *
     * @param requestDto 발행 요청 정보를 담은 dto
     * @return 발행된 쿠폰의 코드 리스트를 담은 dto
     */
    CouponIssuanceResponseDto issueCoupon(CouponIssuanceRequestDto requestDto);
}
