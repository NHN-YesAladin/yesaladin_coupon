package shop.yesaladin.coupon.coupon.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.coupon.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.coupon.docs.ApiDocumentUtils.getDocumentResponse;
import static shop.yesaladin.coupon.docs.DocumentFormatGenerator.defaultValue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.coupon.code.CouponBoundCode;
import shop.yesaladin.coupon.code.CouponTypeCode;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.domain.model.AmountCoupon;
import shop.yesaladin.coupon.coupon.dto.CouponSummaryDto;
import shop.yesaladin.coupon.coupon.dto.MemberCouponSummaryDto;
import shop.yesaladin.coupon.coupon.service.inter.QueryCouponService;

@WebMvcTest(QueryCouponController.class)
@AutoConfigureRestDocs
class QueryCouponControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private QueryCouponService queryCouponService;

    @Test
    @DisplayName("triggered 쿠폰 목록 조회 성공")
    void getTriggeredCouponListTest() throws Exception {
        // given
        String name = "test amount coupon";
        long couponId = 1L;

        AmountCoupon coupon = AmountCoupon.builder()
                .id(couponId)
                .name(name)
                .isUnlimited(false)
                .minOrderAmount(10_000)
                .discountAmount(1_000)
                .expirationDate(LocalDate.of(2023, 1, 4))
                .couponTypeCode(CouponTypeCode.FIXED_PRICE)
                .triggerList(Collections.emptyList())
                .build();

        CouponSummaryDto couponSummaryDto = CouponSummaryDto.builder()
                .id(coupon.getId())
                .name(coupon.getName())
                .triggerTypeCode(TriggerTypeCode.MEMBER_GRADE_GOLD)
                .couponTypeCode(coupon.getCouponTypeCode())
                .isUnlimited(coupon.isUnlimited())
                .duration(coupon.getDuration())
                .expirationDate(coupon.getExpirationDate())
                .createdDateTime(coupon.getCreatedDatetime())
                .minOrderAmount(coupon.getMinOrderAmount())
                .discountAmount(coupon.getDiscountAmount())
                .build();

        List<CouponSummaryDto> couponSummaryDtoList = new ArrayList<>();
        couponSummaryDtoList.add(couponSummaryDto);
        PageImpl<CouponSummaryDto> couponSummaryDtoPage = new PageImpl<>(couponSummaryDtoList);
        Mockito.when(queryCouponService.getTriggeredCouponList(Mockito.any()))
                .thenReturn(couponSummaryDtoPage);

        // when
        ResultActions actual = mockMvc.perform(get("/v1/coupons").contentType(MediaType.APPLICATION_JSON)
                .queryParam("page", "0")
                .queryParam("size", "5"));

        // then
        actual.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.data.currentPage").value(0))
                .andExpect(jsonPath("$.data.totalDataCount").value(1))
                .andExpect(jsonPath("$.data.dataList[0].name").value(name));

        // docs
        actual.andDo(document(
                "get-paginated-triggered-coupon-summary-list-success",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("page").description("페이지네이션 페이지 번호")
                                .optional()
                                .attributes(defaultValue(0)),
                        parameterWithName("size").description("페이지네이션 사이즈")
                                .optional()
                                .attributes(defaultValue(10))
                ),
//                responseFields(beneathPath("data").withSubsectionId("data")),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("응답 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("응답 status code"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 본문 데이터"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .optional()
                                .description("에러 메시지 내용"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER)
                                .description("현재 페이지 번호"),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER)
                                .description("총 페이지 수"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER)
                                .description("조회된 총 쿠폰 수"),
                        fieldWithPath("data.dataList").type(JsonFieldType.ARRAY)
                                .optional()
                                .description("조회된 쿠폰 요약 정보"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER)
                                .optional()
                                .description("조회된 쿠폰 id"),
                        fieldWithPath("data.dataList.[].name").type(JsonFieldType.STRING)
                                .optional()
                                .description("조회된 쿠폰 이름"),
                        fieldWithPath("data.dataList.[].triggerTypeCode").type(JsonFieldType.STRING)
                                .optional()
                                .description("조회된 쿠폰의 트리거 타입"),
                        fieldWithPath("data.dataList.[].couponTypeCode").type(JsonFieldType.STRING)
                                .optional()
                                .description("조회된 쿠폰 타입"),
                        fieldWithPath("data.dataList.[].isUnlimited").type(JsonFieldType.BOOLEAN)
                                .optional()
                                .description("조회된 쿠폰의 무제한 여부"),
                        fieldWithPath("data.dataList.[].fileUri").type(JsonFieldType.STRING)
                                .optional()
                                .description("쿠폰 이미지 경로"),
                        fieldWithPath("data.dataList.[].duration").type(JsonFieldType.NUMBER)
                                .optional()
                                .description("조회된 쿠폰의 유효기간"),
                        fieldWithPath("data.dataList.[].expirationDate").type(JsonFieldType.STRING)
                                .optional()
                                .description("조회된 쿠폰의 만료일"),
                        fieldWithPath("data.dataList.[].createdDateTime").type(JsonFieldType.STRING)
                                .optional()
                                .description("조회된 쿠폰의 생성일자"),
                        fieldWithPath("data.dataList.[].minOrderAmount").type(JsonFieldType.NUMBER)
                                .optional()
                                .description("조회된 할인 쿠폰의 최소 주문금액"),
                        fieldWithPath("data.dataList.[].discountAmount").type(JsonFieldType.NUMBER)
                                .optional()
                                .description("조회된 정액할인 쿠폰의 할인금액"),
                        fieldWithPath("data.dataList.[].chargePointAmount").type(JsonFieldType.NUMBER)
                                .optional()
                                .description("조회된 포인트 쿠폰의 충전 포인트 금액"),
                        fieldWithPath("data.dataList.[].maxDiscountAmount").type(JsonFieldType.NUMBER)
                                .optional()
                                .description("조회된 정율할인 쿠폰의 최대 할인금액"),
                        fieldWithPath("data.dataList.[].discountRate").type(JsonFieldType.NUMBER)
                                .optional()
                                .description("조회된 정율할인 쿠폰의 할인율"),
                        fieldWithPath("data.dataList.[].unlimited").type(JsonFieldType.BOOLEAN)
                                .optional()
                                .description("조회된 쿠폰의 무제한 여부")
                )
        ));
    }

    @Test
    @DisplayName("쿠폰코드 리스트로 회원쿠폰요약정보 조회 성공")
    void getMemberCouponSummaryListTest() throws Exception {
        // given
        List<MemberCouponSummaryDto> memberCouponSummaryDtoList = new ArrayList<>();
        List<String> couponCodeList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            String couponCode = UUID.randomUUID().toString();
            couponCodeList.add(couponCode);
            MemberCouponSummaryDto dto = MemberCouponSummaryDto.builder()
                    .name("test" + i)
                    .couponCode(couponCode)
                    .amount(i)
                    .minOrderAmount(1000)
                    .maxDiscountAmount(2000)
                    .canBeOverlapped(false)
                    .couponTypeCode(CouponTypeCode.FIXED_RATE)
                    .expireDate(LocalDate.of(2023, 12, 20))
                    .isUsed(false)
                    .couponBound("categoryId#" + i)
                    .couponBoundCode(CouponBoundCode.CATEGORY)
                    .build();
            memberCouponSummaryDtoList.add(dto);
        }

        // when
        Mockito.when(queryCouponService.getMemberCouponList(Mockito.anyList()))
                .thenReturn(memberCouponSummaryDtoList);
        ResultActions resultActions = mockMvc.perform(get("/v1/coupons").queryParam(
                "couponCodes",
                String.join(",", couponCodeList)
        )).andDo(print());

        System.out.println(String.join(",", couponCodeList));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.[0].couponCode").value(couponCodeList.get(0)));

        // docs
        resultActions.andDo(document(
                "get-member-coupon-summary-list-success",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(parameterWithName("couponCodes").description("조회할 쿠폰 코드 리스트")),
                responseFields(
                        beneathPath("data").withSubsectionId("data"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("쿠폰 이름"),
                        fieldWithPath("couponCode").type(JsonFieldType.STRING).description("쿠폰 코드"),
                        fieldWithPath("amount").type(JsonFieldType.NUMBER)
                                .description("쿠폰의 금액(할인금액/할인율/포인트충전액)"),
                        fieldWithPath("minOrderAmount").type(JsonFieldType.NUMBER)
                                .description("최소 주문 금액"),
                        fieldWithPath("maxDiscountAmount").type(JsonFieldType.NUMBER)
                                .description("최대 할인 금액"),
                        fieldWithPath("canBeOverlapped").type(JsonFieldType.BOOLEAN)
                                .description("중복 가능 여부"),
                        fieldWithPath("couponTypeCode").type(JsonFieldType.STRING)
                                .description("쿠폰의 타입(정액할인/정율할인/포인트충전)"),
                        fieldWithPath("expireDate").type(JsonFieldType.STRING).description("만료일"),
                        fieldWithPath("isUsed").type(JsonFieldType.BOOLEAN).description("사용 여부"),
                        fieldWithPath("couponBound").type(JsonFieldType.STRING)
                                .description("쿠폰 할인 적용 범위(ISBN/CategoryId)"),
                        fieldWithPath("couponBoundCode").type(JsonFieldType.STRING)
                                .description("쿠폰 할인 적용 범위 코드(ALL/CATEGORY/PRODUCT)")
                )
        ));
    }
}
