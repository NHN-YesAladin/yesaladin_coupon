package shop.yesaladin.coupon.file.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Getter;

@Getter
public class AccessTokenResponse {

    private String id;

    @SuppressWarnings("unchecked")
    @JsonProperty("access")
    private void unpackNested(Map<String, Object> access) {
        Map<String, String> token = (Map<String, String>) access.get("token");
        this.id = token.get("id");
    }
}
