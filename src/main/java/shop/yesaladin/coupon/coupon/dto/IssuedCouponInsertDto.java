package shop.yesaladin.coupon.coupon.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 쿠폰 발행 테이블에 데이터 삽입 시 사용하는 DTO 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public class IssuedCouponInsertDto {

    private final long couponGroupId;
    private final String couponTypeCode;
    private final LocalDate expirationDate;
}
