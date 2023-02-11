package shop.yesaladin.coupon.coupon.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.yesaladin.coupon.code.CouponBoundCode;
import shop.yesaladin.coupon.coupon.domain.model.CouponBound;

/**
 * 쿠폰 범위 조회 요청의 응답 dto 입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponBoundResponseDto {

    private final long couponId;
    private final CouponBoundCode boundCode;
    private final String bound;

    /**
     * 쿠폰 범위 엔티티로부터 응답 DTO 객체를 생성합니다.
     * @param couponBound 쿠폰 범위 엔티티
     * @return 생성된 응답 DTO
     */
    public static CouponBoundResponseDto fromEntity(CouponBound couponBound) {
        String bound;
        switch (couponBound.getCouponBoundCode()) {
            case CATEGORY:
                bound = couponBound.getCategoryId().toString();
                break;
            case PRODUCT:
                bound = couponBound.getIsbn();
                break;
            default:
                bound = null;
                break;
        }
        return new CouponBoundResponseDto(
                couponBound.getCouponId(),
                couponBound.getCouponBoundCode(),
                bound
        );
    }

    /**
     * 포인트 쿠폰을 위한 비어있는 응답 DTO 객체를 생성합니다.
     * @return 빈 응답 DTO
     */
    public static CouponBoundResponseDto emptyBound(long couponId) {
        return new CouponBoundResponseDto(couponId, null, null);
    }
}
