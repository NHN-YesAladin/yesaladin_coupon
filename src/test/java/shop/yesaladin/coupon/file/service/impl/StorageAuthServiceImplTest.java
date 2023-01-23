package shop.yesaladin.coupon.file.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shop.yesaladin.coupon.config.StorageConfiguration;

class StorageAuthServiceImplTest {

    private StorageAuthServiceImpl storageAuthService;
    private StorageConfiguration storageConfiguration;

    @BeforeEach
    void setUp() {
        storageAuthService = new StorageAuthServiceImpl(storageConfiguration);
    }

    @Test
    void test() throws JsonProcessingException {

        String accessTokenResponse = storageAuthService.requestToken();
        System.out.println("id: " + accessTokenResponse);

    }

}