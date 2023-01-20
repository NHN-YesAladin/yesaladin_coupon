package shop.yesaladin.coupon.file.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Service
public class StorageAuthServiceImpl implements StorageAuthService {

    @Value("${coupon.storage-token.auth.url}")
    private final String authUrl;
    @Value("${coupon.storage-token.auth.tenant-id}")
    private final String tenantId;
    @Value("${coupon.storage-token.auth.username}")
    private final String username;
    @Value("${coupon.storage-token.auth.password}")
    private final String password;

    private AccessTokenRequest createAccessTokenRequest() {
        AccessTokenRequest request = new AccessTokenRequest();
        request.getAuth().setTenantId(tenantId);
        request.getAuth().getPasswordCredentials().setUsername(username);
        request.getAuth().getPasswordCredentials().setPassword(password);
        return request;
    }

    @Override
    public String requestToken() throws JsonProcessingException {
        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<AccessTokenRequest> httpEntity
                = new HttpEntity<>(createAccessTokenRequest(), headers);

        // 토큰 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response
                = restTemplate.exchange(
                this.authUrl,
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        JsonMapper jsonMapper = new JsonMapper();
        AccessTokenResponse accessTokenResponse = jsonMapper.readValue(
                response.getBody(),
                AccessTokenResponse.class
        );

        return accessTokenResponse.getId();
    }
}