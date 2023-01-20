package shop.yesaladin.coupon.file.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.InputStream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import shop.yesaladin.coupon.file.service.inter.ObjectStorageService;
import shop.yesaladin.coupon.file.service.inter.StorageAuthService;

@RequiredArgsConstructor
@Service
public class ObjectStorageServiceImpl implements ObjectStorageService {


    @Value("${coupon.storage.url}")
    private final String storageUrl;
    @Value("${coupon.storage.account}")
    private final String storageAccount;

    @Value("${coupon.storage.container-name}")
    private final String containerName;
    private final StorageAuthService storageAuthService;

    @Override
    public String getUrl(String containerName, @NonNull String objectName) {
        containerName = this.containerName;     // FIXME
        return this.storageUrl + "/" + containerName + "/" + objectName;
    }

    // 발급받은 인증 토큰을 사용하여 파일을 업로드합니다.
    @Override
    public void uploadObject(String containerName, String objectName, InputStream inputStream)
            throws JsonProcessingException {
        String tokenId = storageAuthService.requestToken();
        String url = getUrl(containerName, objectName);

        // InputStream 을 요청 본문에 추가할 수 있도록 RequestCallback 오버라이드
        final RequestCallback requestCallback = request -> {
            request.getHeaders().add("X-Auth-Token", tokenId);
            IOUtils.copy(inputStream, request.getBody());
        };

        // 오버라이드한 RequestCallback 을 사용할 수 있도록 설정
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setBufferRequestBody(false);
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        HttpMessageConverterExtractor<String> responseExtractor
                = new HttpMessageConverterExtractor<>(
                String.class,
                restTemplate.getMessageConverters()
        );

        // API 호출
        restTemplate.execute(url, HttpMethod.PUT, requestCallback, responseExtractor);
    }
}