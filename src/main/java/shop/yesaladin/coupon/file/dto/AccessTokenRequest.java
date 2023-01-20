package shop.yesaladin.coupon.file.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class AccessTokenRequest {

    private Auth auth = new Auth();

    @Setter
    @Getter
    public class Auth {

        private String tenantId;
        private PasswordCredentials passwordCredentials = new PasswordCredentials();
    }

    @Setter
    @Getter
    public class PasswordCredentials {

        private String username;
        private String password;
    }
}