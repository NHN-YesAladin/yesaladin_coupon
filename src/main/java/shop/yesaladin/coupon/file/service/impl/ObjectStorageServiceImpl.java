package shop.yesaladin.coupon.file.service.impl;

import java.util.Objects;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpMethod;
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
    private final RestTemplate restTemplate;

    @Override
    public String getUrl(String containerName, @NonNull String objectName) {
        return storageConfiguration.getStorageUrl() + "/" + containerName + "/" + objectName;
    }

    @Override
    public String uploadObject(String containerName, MultipartFile file) {
        String tokenId = storageAuthService.requestToken();

        // InputStream 을 요청 본문에 추가할 수 있도록 RequestCallback 오버라이드
        final RequestCallback requestCallback = request -> {
            request.getHeaders().add("X-Auth-Token", tokenId);
            IOUtils.copy(file.getInputStream(), request.getBody());
        };

        // 오버라이드한 RequestCallback 을 사용할 수 있도록 설정
        HttpMessageConverterExtractor<String> responseExtractor = new HttpMessageConverterExtractor<>(
                String.class,
                restTemplate.getMessageConverters()
        );

        // API 호출
        String url = getUrl(
                containerName,
                getRandomFileName(Objects.requireNonNull(file.getOriginalFilename()))
        );
        restTemplate.execute(url, HttpMethod.PUT, requestCallback, responseExtractor);

        return url;
    }

    private String getRandomFileName(String fileName) {
        return UUID.randomUUID() + fileName.substring(fileName.lastIndexOf("."));
    }
}