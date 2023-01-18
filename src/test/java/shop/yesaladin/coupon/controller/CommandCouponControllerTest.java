package shop.yesaladin.coupon.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.coupon.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.coupon.docs.ApiDocumentUtils.getDocumentResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
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
import shop.yesaladin.coupon.domain.model.CouponTypeCode;
import shop.yesaladin.coupon.domain.model.TriggerTypeCode;
import shop.yesaladin.coupon.dto.CouponResponseDto;
import shop.yesaladin.coupon.dto.PointCouponRequestDto;
import shop.yesaladin.coupon.service.inter.CommandCouponService;

@WebMvcTest(CommandCouponController.class)
@AutoConfigureRestDocs
class CommandCouponControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommandCouponService service;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("포인트 쿠폰 생성 성공")
    void createPointCouponTest() throws Exception {
        // given
//        CouponRequestDto requestBody = new CouponRequestDto(
//                TriggerTypeCode.MEMBER_GRADE_WHITE,
//                "test coupon",
//                10,
//                null,
//                null,
//                LocalDate.of(2023, 3, 1),
//                CouponTypeCode.POINT,
//                1000,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null
//        );

        PointCouponRequestDto requestBody = new PointCouponRequestDto(
                TriggerTypeCode.MEMBER_GRADE_WHITE,
                "test coupon",
                10,
                null,
                null,
                LocalDate.of(2023, 3, 1),
                CouponTypeCode.POINT,
                1000
        );

        Mockito.when(service.createPointCoupon(Mockito.any())).thenReturn(new CouponResponseDto(
                requestBody.getName(), requestBody.getCouponTypeCode()));

        // when
        ResultActions actual = mockMvc.perform(post("/v1/coupons?point").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)));

        // then
        actual.andExpect(status().isCreated())
                 .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        // docs
        actual.andDo(document(
                "create-coupon-success",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("triggerTypeCode").type(JsonFieldType.STRING)
                                .description("생성할 쿠폰의 트리거 타입"),
                        fieldWithPath("name").type(JsonFieldType.STRING)
                                .description("생성할 쿠폰의 이름"),
                        fieldWithPath(("quantity")).type(JsonFieldType.NUMBER)
                                .optional()
                                .description("생성할 쿠폰의 수량, null 일 경우 무제한"),
                        fieldWithPath("fileUri").type(JsonFieldType.STRING)
                                .optional()
                                .description("생성할 쿠폰의 이미지 URI"),
                        fieldWithPath("duration").type(JsonFieldType.STRING)
                                .optional()
                                .description("생성할 쿠폰의 사용 기간"),
                        fieldWithPath("expirationDate").type(JsonFieldType.STRING)
                                .optional()
                                .description("생성할 쿠폰의 만료기간"),
                        fieldWithPath("couponTypeCode").type(JsonFieldType.STRING)
                                .description("생성할 쿠폰의 타입"),
                        fieldWithPath("chargePointAmount").type(JsonFieldType.NUMBER)
                                .description("포인트 쿠폰의 충전 포인트 금액")
                ),
                responseFields(
                        fieldWithPath("name").type(JsonFieldType.STRING)
                                .description("생성된 쿠폰의 이름"),
                        fieldWithPath("couponTypeCode").type(JsonFieldType.STRING)
                                .description("생성된 쿠폰의 종류")
                )
        ));
    }

}