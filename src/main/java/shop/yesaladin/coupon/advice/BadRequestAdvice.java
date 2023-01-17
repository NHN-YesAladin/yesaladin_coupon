package shop.yesaladin.coupon.advice;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.yesaladin.coupon.dto.ErrorResponseDto;

/**
 * 400 Bad Request 응답을 내려주기 위한 RestControllerAdvice 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
@RestControllerAdvice
public class BadRequestAdvice {

    /**
     * MethodArgumentNotValidException 이 발생하면 FiledError들의 메시지를 꺼내 응답합니다.
     *
     * @param ex 발생한 예외
     * @return 에러 메시지를 담은 DTO
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponseDto handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errorMesasageList = ex.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        return new ErrorResponseDto(errorMesasageList);
    }
}
