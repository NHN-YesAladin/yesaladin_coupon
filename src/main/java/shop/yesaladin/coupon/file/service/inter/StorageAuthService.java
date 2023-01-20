package shop.yesaladin.coupon.file.service.inter;

import com.fasterxml.jackson.core.JsonProcessingException;
import shop.yesaladin.coupon.file.dto.AccessTokenResponse;

public interface StorageAuthService {

    String requestToken() throws JsonProcessingException;
}
