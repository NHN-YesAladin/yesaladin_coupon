package shop.yesaladin.coupon.coupon.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static shop.yesaladin.coupon.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.coupon.docs.ApiDocumentUtils.getDocumentResponse;

import java.util.List;
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
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.dto.CouponGroupAndLimitDto;
import shop.yesaladin.coupon.coupon.service.inter.QueryCouponGroupService;

@WebMvcTest(QueryCouponGroupController.class)
@AutoConfigureRestDocs
class QueryCouponGroupControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private QueryCouponGroupService queryCouponGroupService;

    @Test
    @DisplayName("쿠폰 그룹 코드와 무제한 여부 조회에 성공한다.")
    void getCouponMetaForGiveTest() throws Exception {
        // given
        Mockito.when(queryCouponGroupService.getCouponGroupAndLimit(TriggerTypeCode.SIGN_UP, 1L))
                .thenReturn(List.of(new CouponGroupAndLimitDto("coupon-group-code", true)));
        // when
        ResultActions actual = mockMvc.perform(get("/v1/coupon-groups").queryParam(
                "trigger-type",
                "SIGN_UP"
        ).queryParam("coupon-id", "1"));

        // then
        actual.andExpect(jsonPath("$.data").isArray());
        actual.andExpect(jsonPath("$.data[0].couponGroupCode").value("coupon-group-code"));
        actual.andExpect(jsonPath("$.data[0].isLimited").value(true));

        // docs
        actual.andDo(document(
                "get-coupon-group-code-and-limit",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("trigger-type").description("조회할 트리거 타입 코드"),
                        parameterWithName("coupon-id").description("조회할 쿠폰 id").optional()
                ),
                responseFields(
                        beneathPath("data").withSubsectionId("data"),
                        fieldWithPath("couponGroupCode").type(JsonFieldType.STRING)
                                .description("요청한 쿠폰의 그룹 코드"),
                        fieldWithPath("isLimited").type(JsonFieldType.BOOLEAN)
                                .description("요청한 쿠폰의 무제한 여부")
                )
        ));
    }
}