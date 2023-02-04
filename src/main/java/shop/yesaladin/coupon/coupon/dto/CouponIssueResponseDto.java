package shop.yesaladin.coupon.coupon.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.yesaladin.coupon.dto.CouponGiveDto;

/**
 * 쿠폰 발행 후 발행된 쿠폰 코드들을 저장하는 DTO 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public class CouponIssueResponseDto {

    private final List<String> createdCouponCodes;
    private final String couponGroupCode;

    public static CouponGiveDto toCouponGiveDto(CouponIssueResponseDto responseDto) {
        return CouponGiveDto.builder()
                .couponCodes(responseDto.getCreatedCouponCodes())
                .couponGroupCode(responseDto.getCouponGroupCode())
                .build();
    }
}
