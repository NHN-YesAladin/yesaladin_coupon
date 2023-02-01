package shop.yesaladin.coupon.file.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * 오브젝트 스토리지의 인증 토큰 발급 응답으로부터 토큰 아이디를 저장하는 클래스입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Getter
public class AccessTokenResponse {

    private String id;

    /**
     * 응답 중 "access" 항목 하위에 있는 "token" 항목의 "id"를 필드 id 에 저장합니다.
     *
     * @param access 응답에 있는 "access" 항목
     */
    @JsonProperty("access")
    private void unpackNested(@NotNull Map<String, Object> access) {
        Map<String, String> token = (Map<String, String>) access.get("token");
        this.id = token.get("id");
    }
}
