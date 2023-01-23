package shop.yesaladin.coupon.file.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import shop.yesaladin.coupon.config.StorageConfiguration;
import shop.yesaladin.coupon.file.dto.AccessTokenRequest;
import shop.yesaladin.coupon.file.dto.AccessTokenResponse;
import shop.yesaladin.coupon.file.exception.InvalidAccessTokenResponseException;
import shop.yesaladin.coupon.file.service.inter.StorageAuthService;

@RequiredArgsConstructor
@Service
public class StorageAuthServiceImpl implements StorageAuthService {

    private final StorageConfiguration storageConfiguration;
    private final RestTemplate restTemplate = new RestTemplate();

    private AccessTokenRequest createAccessTokenRequest() {
        AccessTokenRequest request = new AccessTokenRequest();
        request.getAuth().setTenantId(storageConfiguration.getTenantId());
        request.getAuth().getPasswordCredentials().setUsername(storageConfiguration.getUsername());
        request.getAuth().getPasswordCredentials().setPassword(storageConfiguration.getPassword());
        return request;
    }

    @Override
    public String requestToken() {
        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<AccessTokenRequest> httpEntity
                = new HttpEntity<>(createAccessTokenRequest(), headers);

        // 토큰 요청
        ResponseEntity<String> response
                = this.restTemplate.exchange(
                storageConfiguration.getAuthUrl(),
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        JsonMapper jsonMapper = new JsonMapper();
        AccessTokenResponse accessTokenResponse = null;
        try {
            accessTokenResponse = jsonMapper.readValue(
                    response.getBody(),
                    AccessTokenResponse.class
            );
        } catch (JsonProcessingException e) {
            throw new InvalidAccessTokenResponseException(response.getStatusCodeValue());
        }

        return accessTokenResponse.getId();
    }
}