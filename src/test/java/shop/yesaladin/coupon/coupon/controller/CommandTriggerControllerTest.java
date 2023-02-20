package shop.yesaladin.coupon.coupon.controller;


import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.service.inter.CommandTriggerService;

@WebMvcTest(CommandTriggerController.class)
@AutoConfigureRestDocs
class CommandTriggerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommandTriggerService commandTriggerService;

    @Test
    @DisplayName("쿠폰 발행 중단 요청 성공")
    void stopIssueCouponTest() throws Exception {
        // given
        TriggerTypeCode triggerTypeCode = TriggerTypeCode.SIGN_UP;
        long couponId = 3L;
        Mockito.doNothing()
                .when(commandTriggerService)
                .stopIssueCoupon(Mockito.any(), Mockito.anyLong());

        // when
        ResultActions resultActions = mockMvc.perform(delete("/v1/triggers").contentType(MediaType.APPLICATION_JSON)
                .queryParam("trigger-type", triggerTypeCode.name())
                .queryParam("coupon-id", String.valueOf(couponId)));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true));

        // docs
        resultActions.andDo(document("stop-issue-coupon-success",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("trigger-type").description("발행을 중단할 쿠폰의 트리거 타입"),
                        parameterWithName("coupon-id").description("발행을 중단할 쿠폰의 아이디")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("HTTP status code"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT)
                                .description("null")
                                .optional(),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러메시지")
                                .optional()
                )
        ));
    }
}