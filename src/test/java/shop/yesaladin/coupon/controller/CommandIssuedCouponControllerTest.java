package shop.yesaladin.coupon.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.coupon.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.coupon.docs.ApiDocumentUtils.getDocumentResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import shop.yesaladin.coupon.dto.CouponIssueResponseDto;
import shop.yesaladin.coupon.service.inter.CommandIssueCouponService;

@WebMvcTest(CommandIssuedCouponController.class)
@AutoConfigureRestDocs
class CommandIssuedCouponControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommandIssueCouponService service;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("쿠폰 발행에 성공한다.")
    void issueCouponSuccess() throws Exception {
        // given
        Map<String, Number> requestBody = Map.of("couponId", 1L, "quantity", 10);
        List<String> response = List.of(
                "10bb3a03-7b72-4684-b794-db699f606312",
                "043d82da-200b-4063-a23e-dfa638f93a6e",
                "15545474-8d0a-4589-a278-b11a09f9c4ce",
                "7e94f084-2497-4be2-a896-b1c81f6a439b",
                "558c9231-fec7-4ea7-ae77-0508e3515d66",
                "f2f55d5a-99b2-412b-b539-0ef0be1492d2",
                "a2f9de06-fcd6-45f0-b823-b3dade968655",
                "aadaf24b-47c3-4e72-b4d5-e552d018c708",
                "0a62d7c5-75aa-42ca-8749-53223b36f36b",
                "fd2f0f65-2fdc-4ea4-8ea4-ef4d20a1418d"
        );
        Mockito.when(service.issueCoupon(Mockito.any()))
                .thenReturn(new CouponIssueResponseDto(response));

        // when
        ResultActions actual = mockMvc.perform(post("/v1/issuances").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)));

        // then
        actual.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.createdCouponCodes").isArray());

        // docs
        actual.andDo(document(
                "issue-coupon-success",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("couponId").type(JsonFieldType.NUMBER)
                                .description("발행할 쿠폰 ID"),
                        fieldWithPath("quantity").type(JsonFieldType.NUMBER)
                                .description("발행할 쿠폰 수")
                                .optional()
                                .description("쿠폰 데이터에 저장된 수량")
                ),
                responseFields(fieldWithPath("createdCouponCodes").type(JsonFieldType.ARRAY)
                        .description("발행된 쿠폰의 코드"))
        ));
    }

    @Test
    @DisplayName("유효성 검증 실패로 쿠폰 발행에 성공한다.")
    void issueCouponFail() throws Exception {
        // given
        Map<String, Integer> requestBody = new HashMap<>();
        requestBody.put("couponId", null);
        requestBody.put("quantity", -1);

        // when
        ResultActions actual = mockMvc.perform(post("/v1/issuances").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)));

        // then
        actual.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessageList").isArray());

        // docs
        actual.andDo(document(
                "issue-coupon-fail-cause-validation-fail",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("couponId").type(JsonFieldType.NULL) // 오류 발생을 위해 지정함. 실제론 Null값이 들어갈 수 없음.
                                .description("발행할 쿠폰 ID"),
                        fieldWithPath("quantity").type(JsonFieldType.NUMBER)
                                .description("발행할 쿠폰 수")
                                .optional()
                                .description("쿠폰 데이터에 저장된 수량")
                ),
                responseFields(fieldWithPath("errorMessageList").type(JsonFieldType.ARRAY)
                        .description("오류 메시지 리스트"))
        ));
    }

}