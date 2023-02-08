package shop.yesaladin.coupon.coupon.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import shop.yesaladin.coupon.code.CouponTypeCode;
import shop.yesaladin.coupon.config.JpaAuditingConfiguration;
import shop.yesaladin.coupon.coupon.domain.model.AmountCoupon;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;

@Import(JpaAuditingConfiguration.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
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
        assertThat(save.getCreatedDatetime()).isNotNull();
        assertThat(save.getCouponTypeCode()).isEqualTo(CouponTypeCode.FIXED_PRICE);
        assertThat(((AmountCoupon) save).getDiscountAmount()).isEqualTo(1000);
    }

}