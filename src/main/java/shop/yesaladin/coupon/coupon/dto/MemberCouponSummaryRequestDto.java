package shop.yesaladin.coupon.coupon.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원이 가진 쿠폰코드 리스트를 담은 dto 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public class MemberCouponSummaryRequestDto {

    @NotEmpty
    List<String> couponCodeList;
}
