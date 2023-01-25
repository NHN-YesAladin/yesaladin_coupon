package shop.yesaladin.coupon.coupon.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.coupon.coupon.domain.model.AmountCoupon;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.CouponTypeCode;

@DataJpaTest
class JpaCommandCouponRepositoryTest {

    @Autowired
    private JpaCommandCouponRepository commandCouponRepository;
    private Coupon coupon;

    @BeforeEach
    void setUp() {
        coupon = AmountCoupon.builder()
                .name("test coupon")
                .couponTypeCode(CouponTypeCode.FIXED_PRICE)
                .minOrderAmount(10000)
                .discountAmount(1000)
                .build();
    }

    @Test
    void save() {
        Coupon save = commandCouponRepository.save(coupon);

        assertThat(save.getName()).isEqualTo("test coupon");
        assertThat(save.getCouponTypeCode()).isEqualTo(CouponTypeCode.FIXED_PRICE);
        assertThat(((AmountCoupon) save).getDiscountAmount()).isEqualTo(1000);
    }

}