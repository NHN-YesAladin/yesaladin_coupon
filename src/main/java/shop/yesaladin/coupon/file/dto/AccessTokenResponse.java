package shop.yesaladin.coupon.file.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AccessTokenResponse {

    @JsonProperty("access.token.id")
    private String tokenId;
    @JsonProperty("access.token.tenant.id")
    private String tenantId;
    @JsonProperty("access.token.expires")
    private String expires;
    @JsonProperty("access.user.id")
    private String userId;

}
