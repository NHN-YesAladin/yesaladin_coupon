package shop.yesaladin.coupon.file.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import shop.yesaladin.coupon.file.dto.AccessTokenResponse;
import shop.yesaladin.coupon.file.service.inter.StorageAuthService;

class StorageStorageAuthServiceImplTest {

    private String authUrl = "https://api-identity.infrastructure.cloud.toast.com/v2.0/tokens";
    private String tenantId = "fcb81f74e379456b8ca0e091d351a7af";
    private String username = "mmmmm_s2@naver.com";
    private String password = "yesaladin";
    private RestTemplate restTemplate = new RestTemplate();

    private StorageStorageAuthServiceImpl storageAuthService;

    @BeforeEach
    void setUp() {
        storageAuthService = new StorageStorageAuthServiceImpl(
                authUrl,
                tenantId,
                username,
                password,
                restTemplate
        );
    }

    @Test
    void test() {

        String accessTokenResponse = storageAuthService.requestToken();
        System.out.println("###" + accessTokenResponse);

    }

}