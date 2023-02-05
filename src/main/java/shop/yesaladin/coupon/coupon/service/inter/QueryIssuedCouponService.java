package shop.yesaladin.coupon.coupon.service.inter;

import java.util.List;
import shop.yesaladin.coupon.coupon.dto.CouponIssueRequestDto;
import shop.yesaladin.coupon.coupon.dto.CouponIssueResponseDto;

public interface QueryIssuedCouponService {

    List<CouponIssueResponseDto> getCouponIssueResponseDtoList(CouponIssueRequestDto issueRequestDto);
}
