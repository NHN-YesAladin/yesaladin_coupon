package shop.yesaladin.coupon.coupon.persistence;

import java.time.LocalDate;
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
import shop.yesaladin.coupon.coupon.domain.model.AmountCoupon;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.CouponTypeCode;
import shop.yesaladin.coupon.coupon.dto.IssuedCouponInsertDto;
import shop.yesaladin.coupon.coupon.persistence.MyBatisInsertIssuedCouponMapper;

@Transactional
@SpringBootTest
class MyBatisInsertCouponIssuanceMapperTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private MyBatisInsertIssuedCouponMapper mapper;
    private Coupon coupon;

    @BeforeEach
    void setUp() {
        coupon = AmountCoupon.builder()
                .name("test coupon")
                .minOrderAmount(1000)
                .discountAmount(1000)
                .canBeOverlapped(false)
                .duration(3)
                .couponTypeCode(CouponTypeCode.FIXED_PRICE)
                .build();

        em.persist(coupon);
    }

    @Test
    @DisplayName("쿠폰 대량 등록에 성공한다.")
    void couponIssuanceBulkInsertTest() {
        // given
        List<IssuedCouponInsertDto> insertList = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            IssuedCouponInsertDto issuedCouponInsertDto = new IssuedCouponInsertDto(
                    coupon.getId(),
                    UUID.randomUUID().toString().substring(0, 20),
                    LocalDate.now().plusDays(coupon.getDuration())
            );
            insertList.add(issuedCouponInsertDto);
        }

        // when
        int actual = mapper.insertIssuedCoupon(insertList);

        // then
        Assertions.assertThat(actual).isEqualTo(500);
    }
}