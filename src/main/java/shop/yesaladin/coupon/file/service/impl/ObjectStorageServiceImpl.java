package shop.yesaladin.coupon.file.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import shop.yesaladin.coupon.config.StorageConfiguration;
import shop.yesaladin.coupon.file.service.inter.ObjectStorageService;
import shop.yesaladin.coupon.file.service.inter.StorageAuthService;

@RequiredArgsConstructor
@Service
public class ObjectStorageServiceImpl implements ObjectStorageService {

    private final StorageConfiguration storageConfiguration;
    private final StorageAuthService storageAuthService;

    @Override
    public String getUrl(String containerName, @NonNull String objectName) {
        return storageConfiguration.getStorageUrl() + "/" + containerName + "/" + objectName;
    }

    // 발급받은 인증 토큰을 사용하여 파일을 업로드합니다.
    @Override
    public String uploadObject(String containerName, String objectName, MultipartFile multipartFile) {
        String tokenId = storageAuthService.requestToken();
        String url = getUrl(containerName, objectName);

        // InputStream 을 요청 본문에 추가할 수 있도록 RequestCallback 오버라이드
        final RequestCallback requestCallback = request -> {
            request.getHeaders().add("X-Auth-Token", tokenId);
            IOUtils.copy(multipartFile.getInputStream(), request.getBody());
        };

        // 오버라이드한 RequestCallback 을 사용할 수 있도록 설정
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setBufferRequestBody(false);
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        HttpMessageConverterExtractor<String> responseExtractor = new HttpMessageConverterExtractor<>(
                String.class,
                restTemplate.getMessageConverters()
        );

        // API 호출
        restTemplate.execute(url, HttpMethod.PUT, requestCallback, responseExtractor);

        return url;
    }
}