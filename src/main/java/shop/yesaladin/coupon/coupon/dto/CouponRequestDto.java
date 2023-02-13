package shop.yesaladin.coupon.coupon.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.multipart.MultipartFile;
import shop.yesaladin.coupon.code.CouponTypeCode;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;

/**
 * 쿠폰 생성 요청 시 사용하는 dto 추상 클래스 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@ToString
@Getter
@AllArgsConstructor
public abstract class CouponRequestDto {

    private TriggerTypeCode triggerTypeCode;

    @NotBlank(message = "coupon name must be at least 2 characters long")
    @Length(max = 50, message = "coupon name cannot be more than 50 characters")
    private String name;

    private Boolean isUnlimited;

    @PositiveOrZero(message = "invalid coupon quantity")
    private Integer quantity;

    @Range(min = 1, max = 31)
    private Integer couponOpenDate;

    @DateTimeFormat(pattern = "kk:mm")
    private LocalTime couponOpenTime;

    private MultipartFile imageFile;
    @Setter
    private String imageFileUri;

    @PositiveOrZero(message = "invalid duration of use")
    private Integer duration;

    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate expirationDate;

    private CouponTypeCode couponTypeCode;

    public abstract Coupon toEntity();
}
