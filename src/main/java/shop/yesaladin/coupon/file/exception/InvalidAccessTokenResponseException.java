package shop.yesaladin.coupon.file.exception;

/**
 * 오브젝트 스토리지 인증 토큰 발급 요청으로부터 적절한 응답을 받지 못했을 때 발생하는 예외 클래스입니다.
 *
 * @author 서민지
 * @since 1.0
 */
public class InvalidAccessTokenResponseException extends RuntimeException {

    public InvalidAccessTokenResponseException(int statusCodeValue) {
        super("Invalid AccessTokenResponse status code: " + statusCodeValue);
    }
}
