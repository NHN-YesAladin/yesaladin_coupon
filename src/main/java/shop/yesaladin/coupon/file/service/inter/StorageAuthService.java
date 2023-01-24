package shop.yesaladin.coupon.file.service.inter;

/**
 * 오브젝트 스토리지 접근에 필요한 인증 서비스 인터페이스 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
public interface StorageAuthService {

    /**
     * 오브젝트 스토리지에 인증 토큰 발급을 요청합니다.
     *
     * @return 발급된 인증 토큰의 id
     */
    String requestToken();
}
