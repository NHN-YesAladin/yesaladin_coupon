package shop.yesaladin.coupon.coupon.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.coupon.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.coupon.docs.ApiDocumentUtils.getDocumentResponse;

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
import shop.yesaladin.coupon.coupon.domain.model.CouponTypeCode;
import shop.yesaladin.coupon.coupon.dto.CouponResponseDto;
import shop.yesaladin.coupon.coupon.service.inter.CommandCouponService;

@WebMvcTest(CommandCouponController.class)
@AutoConfigureRestDocs
class CommandCouponControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommandCouponService service;

    @Test
    @DisplayName("포인트 쿠폰 생성 성공")
    void createPointCouponTest() throws Exception {
        // given
        String name = "1,000 point coupon";
        Mockito.when(service.createPointCoupon(Mockito.any()))
                .thenReturn(new CouponResponseDto(name, CouponTypeCode.POINT));

        // when
        ResultActions actual = mockMvc.perform(post("/v1/coupons?point").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("triggerTypeCode", "MEMBER_GRADE_WHITE")
                .param("name", name)
                .param("inUnlimited", "false")
                .param("quantity", "10")
                .param("expirationDate", LocalDate.now().plusYears(1).toString())
                .param("couponTypeCode", "POINT")
                .param("chargePointAmount", "1000"));

        // then
        actual.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        Mockito.verify(service).createPointCoupon(Mockito.any());

        // docs
        actual.andDo(document(
                "create-point-coupon-success",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("point").description("쿠폰의 타입(포인트)"),
                        parameterWithName("triggerTypeCode").description("쿠폰의 트리거 타입"),
                        parameterWithName("name").description("쿠폰의 이름"),
                        parameterWithName("inUnlimited").description("쿠폰 발행 무제한 여부"),
                        parameterWithName("quantity").optional()
                                .description("쿠폰의 수량, null 일 경우 무제한"),
                        parameterWithName("imageFile").optional().description("업로드된 쿠폰 이미지 파일"),
                        parameterWithName("duration").optional().description("쿠폰의 사용 기간"),
                        parameterWithName("expirationDate").optional().description("쿠폰의 만료기간"),
                        parameterWithName("couponTypeCode").description("쿠폰의 타입"),
                        parameterWithName("chargePointAmount").description("포인트 쿠폰의 충전 포인트 금액")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("응답 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("응답 status code"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT)
                                .description("응답 본문 데이터"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .optional()
                                .description("에러 메시지 내용"),
                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("생성된 쿠폰의 이름"),
                        fieldWithPath("data.couponTypeCode").type(JsonFieldType.STRING)
                                .description("생성된 쿠폰의 종류")
                )
        ));
    }

    @Test
    @DisplayName("정액할인 쿠폰 생성 성공")
    void createAmountCouponTest() throws Exception {
        // given
        String name = "1000won discount coupon";
        Mockito.when(service.createAmountCoupon(Mockito.any()))
                .thenReturn(new CouponResponseDto(name, CouponTypeCode.FIXED_PRICE));

        // when
        ResultActions actual = mockMvc.perform(post("/v1/coupons?amount").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("triggerTypeCode", "MEMBER_GRADE_WHITE")
                .param("name", name)
                .param("isUnlimited", "true")
                .param("duration", "7")
                .param("couponTypeCode", "FIXED_PRICE")
                .param("minOrderAmount", "10000")
                .param("discountAmount", "1000")
                .param("canBeOverlapped", "false")
                .param("couponBoundCode", "ALL"));

        // then
        actual.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        Mockito.verify(service).createAmountCoupon(Mockito.any());

        // docs
        actual.andDo(document(
                "create-amount-coupon-success",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("amount").description("쿠폰의 타입(정액할인)"),
                        parameterWithName("triggerTypeCode").description("쿠폰의 트리거 타입"),
                        parameterWithName("name").description("쿠폰의 이름"),
                        parameterWithName("isUnlimited").description("쿠폰 발행 무제한 여부"),
                        parameterWithName("quantity").optional()
                                .description("쿠폰의 수량, null 일 경우 무제한"),
                        parameterWithName("imageFile").optional().description("업로드된 쿠폰 이미지 파일"),
                        parameterWithName("duration").optional().description("쿠폰의 사용 기간"),
                        parameterWithName("expirationDate").optional().description("쿠폰의 만료기간"),
                        parameterWithName("couponTypeCode").description("쿠폰의 타입"),
                        parameterWithName("minOrderAmount").description("쿠폰을 적용할 수 있는 최소 주문 금액"),
                        parameterWithName("discountAmount").description("할인 금액"),
                        parameterWithName("canBeOverlapped").description("중복 할인 가능 여부"),
                        parameterWithName("couponBoundCode").description("쿠폰의 적용 범위 코드"),
                        parameterWithName("isbn").optional().description("쿠폰이 적용될 수 있는 상품의 ISBN"),
                        parameterWithName("categoryId").optional()
                                .description("쿠폰이 적용될 수 있는 카테고리 Id")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("응답 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("응답 status code"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT)
                                .description("응답 본문 데이터"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .optional()
                                .description("에러 메시지 내용"),
                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("생성된 쿠폰의 이름"),
                        fieldWithPath("data.couponTypeCode").type(JsonFieldType.STRING)
                                .description("생성된 쿠폰의 종류")
                )
        ));
    }

    @Test
    @DisplayName("정율할인 쿠폰 생성 성공")
    void createdRateCouponTest() throws Exception {
        // given
        String name = "SF genre 10% discount coupon";
        Mockito.when(service.createRateCoupon(Mockito.any()))
                .thenReturn(new CouponResponseDto(name, CouponTypeCode.FIXED_RATE));

        // when
        ResultActions actual = mockMvc.perform(post("/v1/coupons?rate").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("triggerTypeCode", "COUPON_OF_THE_MONTH")
                .param("name", name)
                .param("isUnlimited", "false")
                .param("quantity", "100")
                .param("duration", "7")
                .param("couponTypeCode", "FIXED_RATE")
                .param("minOrderAmount", "1000")
                .param("maxDiscountAmount", "2000")
                .param("discountRate", "10")
                .param("canBeOverlapped", "true")
                .param("couponBoundCode", "CATEGORY")
                .param("categoryId", "100"));

        // then
        actual.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        Mockito.verify(service).createRateCoupon(Mockito.any());

        // docs
        actual.andDo(document(
                "create-amount-coupon-success",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("rate").description("쿠폰의 타입(정율할인)"),
                        parameterWithName("triggerTypeCode").description("쿠폰의 트리거 타입"),
                        parameterWithName("name").description("쿠폰의 이름"),
                        parameterWithName("isUnlimited").description("쿠폰 발행 무제한 여부"),
                        parameterWithName("quantity").optional()
                                .description("쿠폰의 수량, null 일 경우 무제한"),
                        parameterWithName("imageFile").optional().description("업로드된 쿠폰 이미지 파일"),
                        parameterWithName("imageFileUri").optional().description("쿠폰의 이미지 URI"),
                        parameterWithName("duration").optional().description("쿠폰의 사용 기간"),
                        parameterWithName("expirationDate").optional().description("쿠폰의 만료기간"),
                        parameterWithName("couponTypeCode").description("쿠폰의 타입"),
                        parameterWithName("minOrderAmount").description("쿠폰을 적용할 수 있는 최소 주문 금액"),
                        parameterWithName("maxDiscountAmount").description("최대 할인 금액"),
                        parameterWithName("discountRate").description("쿠폰에 적용할 할인율"),
                        parameterWithName("canBeOverlapped").description("중복 할인 가능 여부"),
                        parameterWithName("couponBoundCode").description("쿠폰의 적용 범위 코드"),
                        parameterWithName("isbn").optional().description("쿠폰이 적용될 수 있는 상품의 ISBN"),
                        parameterWithName("categoryId").optional()
                                .description("쿠폰이 적용될 수 있는 카테고리 Id")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("응답 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("응답 status code"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT)
                                .description("응답 본문 데이터"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .optional()
                                .description("에러 메시지 내용"),
                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("생성된 쿠폰의 이름"),
                        fieldWithPath("data.couponTypeCode").type(JsonFieldType.STRING)
                                .description("생성된 쿠폰의 종류")
                )
        ));
    }

}