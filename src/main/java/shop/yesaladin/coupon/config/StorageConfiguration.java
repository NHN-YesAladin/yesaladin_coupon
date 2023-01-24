package shop.yesaladin.coupon.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 쿠폰 이미지를 관리하기 위해 필요한 오브젝트 스토리지 설정을 외부화하기 위해 사용하는 Configuration 클래스입니다.
 *
 * @author 서민지
 * @since 1.0
 */
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
