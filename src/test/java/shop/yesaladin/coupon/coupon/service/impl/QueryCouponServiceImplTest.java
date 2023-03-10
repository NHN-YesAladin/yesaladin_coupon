package shop.yesaladin.coupon.coupon.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.coupon.code.CouponTypeCode;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.PointCoupon;
import shop.yesaladin.coupon.coupon.domain.model.Trigger;
import shop.yesaladin.coupon.coupon.domain.repository.QueryCouponRepository;
import shop.yesaladin.coupon.coupon.domain.repository.QueryIssuedCouponRepository;
import shop.yesaladin.coupon.coupon.domain.repository.QueryTriggerRepository;
import shop.yesaladin.coupon.coupon.dto.CouponSummaryDto;

class QueryCouponServiceImplTest {

    private QueryTriggerRepository queryTriggerRepository;
    private QueryCouponServiceImpl queryCouponService;
    private QueryIssuedCouponRepository queryIssuedCouponRepository;
    private QueryCouponRepository queryCouponRepository;

    @BeforeEach
    void setUp() {
        queryTriggerRepository = Mockito.mock(QueryTriggerRepository.class);
        queryIssuedCouponRepository = Mockito.mock(QueryIssuedCouponRepository.class);
        queryCouponRepository = Mockito.mock(QueryCouponRepository.class);
        queryCouponService = new QueryCouponServiceImpl(
                queryTriggerRepository,
                queryIssuedCouponRepository,
                queryCouponRepository
        );
    }

    @Test
    @DisplayName("triggered 쿠폰 조회 성공")
    void getTriggeredCouponListTest() {
        // given
        String name = "test point coupon";
        long couponId = 1L;

        Coupon coupon = PointCoupon.builder()
                .id(couponId)
                .name(name)
                .isUnlimited(false)
                .chargePointAmount(1000)
                .expirationDate(LocalDate.of(2023, 1, 4))
                .couponTypeCode(CouponTypeCode.POINT)
                .triggerList(Collections.emptyList())
                .build();

        Trigger trigger = Trigger.builder()
                .triggerTypeCode(TriggerTypeCode.BIRTHDAY)
                .coupon(coupon)
                .build();

        CouponSummaryDto couponSummaryDto = CouponSummaryDto.builder()
                .id(couponId)
                .name(name)
                .triggerTypeCode(trigger.getTriggerTypeCode())
                .couponTypeCode(coupon.getCouponTypeCode())
                .isUnlimited(false)
                .chargePointAmount(1000)
                .build();

        List<CouponSummaryDto> couponSummaryDtos = new ArrayList<>();
        couponSummaryDtos.add(couponSummaryDto);
        PageImpl<CouponSummaryDto> couponSummaryDtoPage = new PageImpl<>(couponSummaryDtos);
        Mockito.when(queryTriggerRepository.findAll(Mockito.any()))
                .thenReturn(couponSummaryDtoPage);

        // when
        Page<CouponSummaryDto> result = queryCouponService.getTriggeredCouponList(
                PageRequest.of(0, 5));

        // then
        Assertions.assertThat(result.getContent()).hasSize(1);
        Assertions.assertThat(result.getContent().get(0).getName()).isEqualTo(name);
        Assertions.assertThat(result.getContent().get(0).getChargePointAmount()).isEqualTo(1000);
    }

    @Test
    @DisplayName("트리거 타입 코드로 쿠폰 조회에 성공한다.")
    @SuppressWarnings("unchecked")
    void getCouponListByTriggerTypeCodeTest() {
        // given
        Page<CouponSummaryDto> expectedReturnValue = Mockito.mock(Page.class);
        Pageable expectedPageable = PageRequest.of(0, 10);
        Mockito.when(queryCouponRepository.findCouponByTriggerCode(
                true,
                TriggerTypeCode.SIGN_UP,
                expectedPageable
        )).thenReturn(expectedReturnValue);

        // when
        Page<CouponSummaryDto> actual = queryCouponService.getCouponListByTriggerTypeCode(
                true,
                TriggerTypeCode.SIGN_UP,
                expectedPageable
        );

        // then
        Assertions.assertThat(actual).isEqualTo(expectedReturnValue);
        Mockito.verify(queryCouponRepository, Mockito.times(1))
                .findCouponByTriggerCode(true, TriggerTypeCode.SIGN_UP, expectedPageable);
    }

}