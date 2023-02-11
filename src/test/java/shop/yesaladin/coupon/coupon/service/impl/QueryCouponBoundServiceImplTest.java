package shop.yesaladin.coupon.coupon.service.impl;

import java.util.Optional;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.coupon.code.CouponBoundCode;
import shop.yesaladin.coupon.code.CouponTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.Coupon;
import shop.yesaladin.coupon.coupon.domain.model.CouponBound;
import shop.yesaladin.coupon.coupon.domain.repository.QueryCouponBoundRepository;
import shop.yesaladin.coupon.coupon.domain.repository.QueryCouponRepository;
import shop.yesaladin.coupon.coupon.dto.CouponBoundResponseDto;
import shop.yesaladin.coupon.coupon.dummy.CouponDummy;

class QueryCouponBoundServiceImplTest {

    private QueryCouponBoundServiceImpl service;
    private QueryCouponBoundRepository queryCouponBoundRepository;
    private QueryCouponRepository queryCouponRepository;

    @BeforeEach
    void setup() {
        queryCouponBoundRepository = Mockito.mock(QueryCouponBoundRepository.class);
        queryCouponRepository = Mockito.mock(QueryCouponRepository.class);
        service = new QueryCouponBoundServiceImpl(
                queryCouponBoundRepository,
                queryCouponRepository
        );
    }

    @DisplayName("쿠폰 ID로 쿠폰 적용범위 조회에 성공한다.")
    @ParameterizedTest
    @MethodSource("getCouponBoundByCouponIdSuccessTestArgs")
    void getCouponBoundByCouponIdSuccessTest(
            CouponTypeCode couponTypeCode,
            CouponBoundCode couponBoundCode,
            String isbn,
            Long categoryId,
            String expectedBound,
            int expectedBoundQueryTimes
    ) {
        // given
        long couponId = 1L;
        Coupon coupon = CouponDummy.dummyCouponWithCouponType(couponTypeCode);
        CouponBound expectedCouponBound = CouponBound.builder()
                .couponId(couponId)
                .couponBoundCode(couponBoundCode)
                .isbn(isbn)
                .categoryId(categoryId)
                .build();
        Mockito.when(queryCouponRepository.findCouponById(couponId))
                .thenReturn(Optional.of(coupon));
        Mockito.when(queryCouponBoundRepository.findCouponBoundByCouponId(couponId))
                .thenReturn(Optional.of(expectedCouponBound));

        // when
        CouponBoundResponseDto actual = service.getCouponBoundByCouponId(couponId);

        // then

        Mockito.verify(queryCouponBoundRepository, Mockito.times(expectedBoundQueryTimes))
                .findCouponBoundByCouponId(couponId);
        Assertions.assertThat(actual.getCouponId()).isEqualTo(expectedCouponBound.getCouponId());
        Assertions.assertThat(actual.getBoundCode())
                .isEqualTo(expectedCouponBound.getCouponBoundCode());
        Assertions.assertThat(actual.getBound()).isEqualTo(expectedBound);
    }

    @Test
    @DisplayName("쿠폰ID로 조회된 적용 범위 데이터가 없을 경우 예외가 발생한다.")
    void getCouponBoundByCouponIdFailCauseByDataNotExistsTest() {
        // given
        Mockito.when(queryCouponRepository.findCouponById(1L))
                .thenReturn(Optional.of(CouponDummy.dummyRateCoupon()));
        Mockito.when(queryCouponBoundRepository.findCouponBoundByCouponId(1L))
                .thenReturn(Optional.empty());

        // when
        // then
        Assertions.assertThatThrownBy(() -> service.getCouponBoundByCouponId(1L))
                .isInstanceOf(ClientException.class)
                .hasMessageContaining("1");
    }

    @Test
    @DisplayName("쿠폰 ID로 조회된 쿠폰이 없을 경우 예외가 발생한다.")
    void getCouponBoundByCouponIdFailCauseByCouponNotExistsTest() {
        // given
        Mockito.when(queryCouponRepository.findCouponById(1L))
                .thenReturn(Optional.empty());

        // when
        // then
        Assertions.assertThatThrownBy(() -> service.getCouponBoundByCouponId(1L))
                .isInstanceOf(ClientException.class)
                .hasMessageContaining("1");
    }

    static Stream<Arguments> getCouponBoundByCouponIdSuccessTestArgs() {
        return Stream.of(
                Arguments.of(
                        CouponTypeCode.FIXED_RATE,
                        CouponBoundCode.PRODUCT,
                        "12345",
                        null,
                        "12345",
                        1
                ),
                Arguments.of(
                        CouponTypeCode.FIXED_PRICE,
                        CouponBoundCode.CATEGORY,
                        null,
                        1000L,
                        "1000",
                        1
                ),
                Arguments.of(CouponTypeCode.FIXED_RATE, CouponBoundCode.ALL, null, null, null, 1),
                Arguments.of(CouponTypeCode.POINT, null, null, null, null, 0)
        );
    }
}