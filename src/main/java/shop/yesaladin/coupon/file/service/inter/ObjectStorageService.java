package shop.yesaladin.coupon.file.service.inter;

import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

/**
 * 오브젝트 스토리지에 파일을 업로드/다운로드하는 서비스 인터페이스 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
public interface ObjectStorageService {

    /**
     * 오브젝트 스토리지의 Url 을 구성합니다.
     *
     * @param container  파일이 저장된 컨테이너 이름
     * @param objectName 파일의 이름
     * @return 파일의 Url
     */
    String getUrl(String container, @NonNull String objectName);

    /**
     * 발급받은 인증 토큰을 사용하여 파일을 업로드합니다.
     *
     * @param containerName 파일을 저장할 컨테이너 이름
     * @param multipartFile 저장할 파일
     * @return 저장된 파일의 Url
     */
    String uploadObject(String containerName, MultipartFile multipartFile);
}
