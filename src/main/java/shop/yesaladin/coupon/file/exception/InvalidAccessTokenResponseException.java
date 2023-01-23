package shop.yesaladin.coupon.file.exception;

public class InvalidAccessTokenResponseException extends RuntimeException {

    public InvalidAccessTokenResponseException(int statusCodeValue) {
        super("Invalid AccessTokenResponse status code: " + statusCodeValue);
    }
}
