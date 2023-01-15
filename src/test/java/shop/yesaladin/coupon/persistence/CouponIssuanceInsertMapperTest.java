package shop.yesaladin.coupon.persistence;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.domain.model.CouponCode;
import shop.yesaladin.coupon.dto.CouponIssuanceInsertDto;

@Transactional
@SpringBootTest
class CouponIssuanceInsertMapperTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private CouponIssuanceInsertMapper mapper;
    private Coupon coupon;

    @BeforeEach
    void setUp() {
        coupon = Coupon.builder()
                .name("test coupon")
                .quantity(500)
                .minOrderAmount(1000)
                .maxDiscountAmount(1000)
                .discountAmount(1000)
                .canBeOverlapped(false)
                .openDatetime(LocalDateTime.of(2023, 1, 1, 0, 0))
                .couponTypeCode(CouponCode.POINT)
                .issuanceCode(CouponCode.AUTO_ISSUANCE)
                .build();

        em.persist(coupon);
    }

    @Test
    @DisplayName("쿠폰 대량 등록에 성공한다.")
    void couponIssuanceBulkInsertTest() {
        // given
        List<CouponIssuanceInsertDto> insertList = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            CouponIssuanceInsertDto couponIssuanceInsertDto = new CouponIssuanceInsertDto(
                    coupon.getId(),
                    UUID.randomUUID().toString().substring(0, 20),
                    coupon.getOpenDatetime().plusDays(7).toLocalDate()
            );
            insertList.add(couponIssuanceInsertDto);
        }

        // when
        int actual = mapper.insertCouponIssuance(insertList);

        // then
        Assertions.assertThat(actual).isEqualTo(500);
    }
}