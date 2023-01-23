package shop.yesaladin.coupon.file.dto;

import lombok.Data;

/**
 * 오브젝트 스토리지 접근에 필요한 인증 토큰 요청시 사용되는 클래스 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Data
public class AccessTokenRequest {

    private Auth auth = new Auth();

    @Data
    public class Auth {

        private String tenantId;
        private PasswordCredentials passwordCredentials = new PasswordCredentials();
    }

    @Data
    public class PasswordCredentials {

        private String username;
        private String password;
    }
}