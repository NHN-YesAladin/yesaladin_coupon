package shop.yesaladin.coupon.service.inter;

import java.util.List;

/**
 * 쿠폰 발행과 관련된 CUD 작업을 하는 서비스 인터페이스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
public interface CouponIssuanceCommandService {

    /**
     * 쿠폰 테이블에 있는 데이터를 바탕으로 쿠폰을 발행하는 메서드입니다.
     *
     * @param couponId 발행할 쿠폰의 ID
     * @return 발행된 쿠폰의 코드 리스트
     */
    List<String> issueCoupon(long couponId);
}
