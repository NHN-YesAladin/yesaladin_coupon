package shop.yesaladin.coupon.coupon.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.coupon.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.coupon.docs.ApiDocumentUtils.getDocumentResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.coupon.code.CouponBoundCode;
import shop.yesaladin.coupon.coupon.domain.model.CouponBound;
import shop.yesaladin.coupon.coupon.dto.CouponBoundResponseDto;
import shop.yesaladin.coupon.coupon.service.inter.QueryCouponBoundService;

@WebMvcTest(QueryCouponBoundController.class)
@AutoConfigureRestDocs
class QueryCouponBoundControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private QueryCouponBoundService queryCouponBoundService;

    @Test
    @DisplayName("쿠폰 ID로 쿠폰 적용 범위 조회에 성공한다.")
    void getCouponBoundByCouponId() throws Exception {
        // given
        long couponId = 1L;
        CouponBound couponBound = CouponBound.builder()
                .couponBoundCode(CouponBoundCode.CATEGORY)
                .categoryId(1000L)
                .couponId(1L)
                .build();
        CouponBoundResponseDto response = CouponBoundResponseDto.fromEntity(couponBound);
        Mockito.when(queryCouponBoundService.getCouponBoundByCouponId(1L)).thenReturn(response);

        // when
        ResultActions actual = mockMvc.perform(get("/v1/coupons/{couponId}/bounds", couponId));

        // then
        actual.andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.errorMessages").isEmpty())
                .andExpect(jsonPath("$.data.couponId").value(1))
                .andExpect(jsonPath("$.data.boundCode").value("CATEGORY"))
                .andExpect(jsonPath("$.data.bound").value("1000"));

        // docs
        actual.andDo(document(
                "get-coupon-bound-by-coupon-id",
                getDocumentRequest(), getDocumentResponse(),
                responseFields(
                        beneathPath("data").withSubsectionId("data"),
                        fieldWithPath("couponId").type(JsonFieldType.NUMBER)
                                .description("조회를 요청한 쿠폰의 ID"),
                        fieldWithPath("boundCode").type(JsonFieldType.STRING)
                                .description("조회된 쿠폰의 적용범위 코드. 포인트 쿠폰인 경우에는 Null"),
                        fieldWithPath("bound").type(JsonFieldType.STRING)
                                .description("조회된 쿠폰의 적용범위. 포인트 쿠폰이거나 전체 범위 쿠폰인 경우에는 Null")
                )
        ));

    }

}