package shop.yesaladin.coupon.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 예외가 발생했을 때 ControllerAdvice에서 응답하는 DTO
 *
 * @author 김홍대
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public class ErrorResponseDto {

    private final List<String> errorMessageList;
}
