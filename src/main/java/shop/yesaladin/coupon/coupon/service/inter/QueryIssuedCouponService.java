package shop.yesaladin.coupon.coupon.service.inter;

import java.time.LocalDateTime;
import java.util.List;
import shop.yesaladin.coupon.coupon.domain.model.IssuedCoupon;
import shop.yesaladin.coupon.coupon.dto.CouponIssueRequestDto;
import shop.yesaladin.coupon.coupon.dto.CouponIssueResponseDto;

/**
 * 발행쿠폰 관련 조회 서비스 인터페이스입니다.
 *
 * @author 서민지
 * @since 1.0
 */
public interface QueryIssuedCouponService {

    /**
     * 쿠폰 지급 요청을 처리합니다. 쿠폰 요청에 대해 유효한 발행쿠폰이 존재하는 경우 쿠폰코드와 그룹코드를 반환합니다. 수량 제한 쿠폰의 경우 발행쿠폰이 존재하지 않을 경우
     * ClientException 이 발생합니다.
     *
     * @param issueRequestDto 쿠폰 지급 요청 dto
     * @return 요청에 대한 쿠폰 발행 정보 리스트
     */
    List<CouponIssueResponseDto> getCouponIssueResponseDtoList(CouponIssueRequestDto issueRequestDto);

    /**
     * 쿠폰코드를 통해 사용이 불가능한 발행쿠폰을 조회합니다. 조회하는 모든 발행쿠폰의 지급 상태(지급완료), 사용 상태(미사용), 만료일(검사 시간 이후)이 유효하면 empty
     * List 를 반환합니다.
     *
     * @param couponCodeList  조회할 쿠폰 코드
     * @param requestDateTime 쿠폰 사용 요청 시간
     * @return 사용이 불가능한 발행쿠폰 리스트
     */
    List<IssuedCoupon> checkUnavailableIssuedCoupon(List<String> couponCodeList, LocalDateTime requestDateTime);
}
