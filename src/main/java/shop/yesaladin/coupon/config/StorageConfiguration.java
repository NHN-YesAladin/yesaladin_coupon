package shop.yesaladin.coupon.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class StorageConfiguration {

    @Value("${coupon.storage-token.auth.url}")
    private String authUrl;
    @Value("${coupon.storage-token.auth.tenant-id}")
    private String tenantId;
    @Value("${coupon.storage-token.auth.username}")
    private String username;
    @Value("${coupon.storage-token.auth.password}")
    private String password;
    @Value("${coupon.storage.url}")
    private String storageUrl;
    @Value("${coupon.storage.container-name}")
    private String containerName;
}
