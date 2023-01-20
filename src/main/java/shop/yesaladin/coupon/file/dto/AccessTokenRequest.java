package shop.yesaladin.coupon.file.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
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