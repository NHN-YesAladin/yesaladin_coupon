package shop.yesaladin.coupon.coupon.service.impl;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.domain.repository.QueryCouponGroupRepository;
import shop.yesaladin.coupon.coupon.dto.CouponGroupAndLimitDto;

class QueryCouponGroupServiceImplTest {

    private QueryCouponGroupRepository queryCouponGroupRepository;
    private QueryCouponGroupServiceImpl queryCouponGroupService;

    @BeforeEach
    void setup() {
        queryCouponGroupRepository = Mockito.mock(QueryCouponGroupRepository.class);
        queryCouponGroupService = new QueryCouponGroupServiceImpl(queryCouponGroupRepository);
    }

    @Test
    @DisplayName("쿠폰 트리거 코드와 쿠폰 아이디로 쿠폰 그룹, 무제한 여부를 가져온다.")
    void getCouponGroupAndLimitTest() {
        // given
        List<CouponGroupAndLimitDto> expectedResult = List.of(new CouponGroupAndLimitDto(
                "a",
                true
        ));
        Mockito.when(queryCouponGroupRepository.findCouponGroupAndLimitMeta(
                TriggerTypeCode.SIGN_UP,
                1L
        )).thenReturn(expectedResult);

        // when
        List<CouponGroupAndLimitDto> actual = queryCouponGroupService.getCouponGroupAndLimit(
                TriggerTypeCode.SIGN_UP,
                1L
        );

        // then
        Assertions.assertThat(actual).isEqualTo(expectedResult);
    }

}