package shop.yesaladin.coupon.file.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import shop.yesaladin.coupon.file.dto.AccessTokenRequest;
import shop.yesaladin.coupon.file.dto.AccessTokenResponse;
import shop.yesaladin.coupon.file.service.inter.StorageAuthService;

@Slf4j
@RequiredArgsConstructor
@Service
public class StorageStorageAuthServiceImpl implements StorageAuthService {

    @Value("${coupon.storage-token.auth.url}")
    private final String authUrl;
    @Value("${coupon.storage-token.auth.tenant-id}")
    private final String tenantId;
    @Value("${coupon.storage-token.auth.username}")
    private final String username;
    @Value("${coupon.storage-token.auth.password}")
    private final String password;
    private final RestTemplate restTemplate;

    private AccessTokenRequest createAccessTokenRequest() {
        AccessTokenRequest request = new AccessTokenRequest();
        request.getAuth().setTenantId(tenantId);
        request.getAuth().getPasswordCredentials().setUsername(username);
        request.getAuth().getPasswordCredentials().setPassword(password);
        return request;
    }

    @Override
    public String requestToken() {
        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        log.info(headers.toString());

        HttpEntity<AccessTokenRequest> httpEntity
                = new HttpEntity<>(createAccessTokenRequest(), headers);

        log.info(httpEntity.toString());
        log.info(this.authUrl);

        // 토큰 요청
        ResponseEntity<String> response
                = this.restTemplate.exchange(
                this.authUrl,
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        return response.getBody();
    }
}