package shop.yesaladin.coupon.domain.repository;

import java.util.List;
import shop.yesaladin.coupon.dto.IssuedCouponInsertDto;

/**
 * 쿠폰을 발급하는 Repository 인터페이스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
public interface InsertIssuedCouponRepository {

    /**
     * 쿠폰발급 테이블에 발급 정보를 저장하는 메서드입니다.
     *
     * @param insertDataList 쿠폰 발급에 대한 정보가 담긴 리스트
     * @return 삽입된 데이터의 수
     */
    int insertIssuedCoupon(List<IssuedCouponInsertDto> insertDataList);
}
