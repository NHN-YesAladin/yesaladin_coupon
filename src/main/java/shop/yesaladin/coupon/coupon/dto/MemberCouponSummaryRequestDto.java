package shop.yesaladin.coupon.coupon.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberCouponSummaryRequestDto {

    @NotEmpty
    List<String> couponCodeList;
}
