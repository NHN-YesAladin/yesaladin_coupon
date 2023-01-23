package shop.yesaladin.coupon.file.service.inter;

import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

public interface ObjectStorageService {

    String getUrl(String container, @NonNull String objectName);

    // 발급받은 인증 토큰을 사용하여 파일을 업로드합니다.
    String uploadObject(String containerName, String objectName, MultipartFile multipartFile);
}
