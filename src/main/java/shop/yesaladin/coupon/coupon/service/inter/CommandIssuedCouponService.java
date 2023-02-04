package shop.yesaladin.coupon.coupon.service.inter;

import java.util.List;
import shop.yesaladin.coupon.coupon.domain.model.CouponGivenStateCode;
import shop.yesaladin.coupon.coupon.dto.CouponIssueRequestDto;
import shop.yesaladin.coupon.coupon.dto.CouponIssueResponseDto;

/**
 * 쿠폰 발행과 관련된 CUD 작업을 하는 서비스 인터페이스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
public interface CommandIssuedCouponService {

    /**
     * 쿠폰 테이블에 있는 데이터를 바탕으로 쿠폰을 발행하는 메서드입니다.
     *
     * @param requestDto 발행 요청 정보를 담은 dto
     * @return 발행된 쿠폰의 코드 리스트를 담은 dto 리스트
     */
    List<CouponIssueResponseDto> issueCoupon(CouponIssueRequestDto requestDto);

    // TODO javadoc
    long updateCouponGivenState(List<String> couponCodeList, CouponGivenStateCode givenStateCode);
}
