package shop.yesaladin.coupon.file.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shop.yesaladin.coupon.file.service.inter.StorageAuthService;

class ObjectStorageServiceImplTest {


    private String authUrl = "https://api-identity.infrastructure.cloud.toast.com/v2.0/tokens";
    private String tenantId = "fcb81f74e379456b8ca0e091d351a7af";
    private String username = "mmmmm_s2@naver.com";
    private String password = "yesaladin";
    private String storageUrl = "https://api-storage.cloud.toast.com/v1/AUTH_fcb81f74e379456b8ca0e091d351a7af";
    private String containerName = "yesaladinTest/coupon/test";
    private StorageAuthService storageAuthService;
    private ObjectStorageServiceImpl objectStorageService;

    @BeforeEach
    void setUp() {
        storageAuthService = new StorageAuthServiceImpl(
                authUrl,
                tenantId,
                username,
                password
        );
        objectStorageService = new ObjectStorageServiceImpl(
                storageUrl,
                containerName,
                storageAuthService
        );
    }

    @Test
    void test() throws FileNotFoundException, JsonProcessingException {
        File objFile = new File("/Users/mj/Downloads/coupon.png");
        InputStream inputStream = new FileInputStream(objFile);
        objectStorageService.uploadObject(null, "testCoupon.png", inputStream);
    }
}