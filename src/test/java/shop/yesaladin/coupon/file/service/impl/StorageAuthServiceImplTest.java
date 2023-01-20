package shop.yesaladin.coupon.file.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StorageAuthServiceImplTest {

    private String authUrl = "https://api-identity.infrastructure.cloud.toast.com/v2.0/tokens";
    private String tenantId = "fcb81f74e379456b8ca0e091d351a7af";
    private String username = "mmmmm_s2@naver.com";
    private String password = "yesaladin";

    private StorageAuthServiceImpl storageAuthService;

    @BeforeEach
    void setUp() {
        storageAuthService = new StorageAuthServiceImpl(
                authUrl,
                tenantId,
                username,
                password
        );
    }

    @Test
    void test() throws JsonProcessingException {

        String accessTokenResponse = storageAuthService.requestToken();
        System.out.println("id: " + accessTokenResponse);

    }

}