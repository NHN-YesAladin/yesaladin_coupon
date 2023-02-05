package shop.yesaladin.coupon.coupon.persistence;

import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QueryDslQueryIssuedCouponRepositoryTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private QueryDslQueryIssuedCouponRepository queryIssuedCouponRepository;

    @Test
    void test() {
        queryIssuedCouponRepository.getMemberCouponSummary(List.of(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()
        ));
    }
}