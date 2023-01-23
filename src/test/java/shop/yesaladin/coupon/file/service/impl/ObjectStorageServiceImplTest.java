package shop.yesaladin.coupon.file.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shop.yesaladin.coupon.config.StorageConfiguration;
import shop.yesaladin.coupon.file.service.inter.StorageAuthService;

class ObjectStorageServiceImplTest {

    private StorageAuthService storageAuthService;
    private ObjectStorageServiceImpl objectStorageService;
    private StorageConfiguration storageConfiguration;

    @BeforeEach
    void setUp() {
        storageAuthService = new StorageAuthServiceImpl(storageConfiguration);
        objectStorageService = new ObjectStorageServiceImpl(storageConfiguration,
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