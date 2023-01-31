package shop.yesaladin.coupon.coupon.domain.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import shop.yesaladin.coupon.coupon.dto.CouponSummaryDto;

@SpringBootTest
class QueryTriggerRepositoryTest {

    @Autowired
    QueryTriggerRepository triggerRepository;

    @Test
    void test() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<CouponSummaryDto> all = triggerRepository.findAll(pageRequest);
    }
}