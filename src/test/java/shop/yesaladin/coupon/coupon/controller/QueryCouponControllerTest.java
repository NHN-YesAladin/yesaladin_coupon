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
    @DisplayName("triggered ?????? ?????? ?????? ??????")
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
                        parameterWithName("page").description("?????????????????? ????????? ??????")
                                .optional()
                                .attributes(defaultValue(0)),
                        parameterWithName("size").description("?????????????????? ?????????")
                                .optional()
                                .attributes(defaultValue(10))
                ),
//                responseFields(beneathPath("data").withSubsectionId("data")),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("?????? status code"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????? ?????? ?????????"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .optional()
                                .description("?????? ????????? ??????"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER)
                                .description("?????? ????????? ??????"),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER)
                                .description("??? ????????? ???"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER)
                                .description("????????? ??? ?????? ???"),
                        fieldWithPath("data.dataList").type(JsonFieldType.ARRAY)
                                .optional()
                                .description("????????? ?????? ?????? ??????"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER)
                                .optional()
                                .description("????????? ?????? id"),
                        fieldWithPath("data.dataList.[].name").type(JsonFieldType.STRING)
                                .optional()
                                .description("????????? ?????? ??????"),
                        fieldWithPath("data.dataList.[].triggerTypeCode").type(JsonFieldType.STRING)
                                .optional()
                                .description("????????? ????????? ????????? ??????"),
                        fieldWithPath("data.dataList.[].couponTypeCode").type(JsonFieldType.STRING)
                                .optional()
                                .description("????????? ?????? ??????"),
                        fieldWithPath("data.dataList.[].isUnlimited").type(JsonFieldType.BOOLEAN)
                                .optional()
                                .description("????????? ????????? ????????? ??????"),
                        fieldWithPath("data.dataList.[].fileUri").type(JsonFieldType.STRING)
                                .optional()
                                .description("?????? ????????? ??????"),
                        fieldWithPath("data.dataList.[].duration").type(JsonFieldType.NUMBER)
                                .optional()
                                .description("????????? ????????? ????????????"),
                        fieldWithPath("data.dataList.[].expirationDate").type(JsonFieldType.STRING)
                                .optional()
                                .description("????????? ????????? ?????????"),
                        fieldWithPath("data.dataList.[].createdDateTime").type(JsonFieldType.STRING)
                                .optional()
                                .description("????????? ????????? ????????????"),
                        fieldWithPath("data.dataList.[].minOrderAmount").type(JsonFieldType.NUMBER)
                                .optional()
                                .description("????????? ?????? ????????? ?????? ????????????"),
                        fieldWithPath("data.dataList.[].discountAmount").type(JsonFieldType.NUMBER)
                                .optional()
                                .description("????????? ???????????? ????????? ????????????"),
                        fieldWithPath("data.dataList.[].chargePointAmount").type(JsonFieldType.NUMBER)
                                .optional()
                                .description("????????? ????????? ????????? ?????? ????????? ??????"),
                        fieldWithPath("data.dataList.[].maxDiscountAmount").type(JsonFieldType.NUMBER)
                                .optional()
                                .description("????????? ???????????? ????????? ?????? ????????????"),
                        fieldWithPath("data.dataList.[].discountRate").type(JsonFieldType.NUMBER)
                                .optional()
                                .description("????????? ???????????? ????????? ?????????"),
                        fieldWithPath("data.dataList.[].unlimited").type(JsonFieldType.BOOLEAN)
                                .optional()
                                .description("????????? ????????? ????????? ??????")
                )
        ));
    }

    @Test
    @DisplayName("???????????? ???????????? ???????????????????????? ?????? ??????")
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
                requestParameters(parameterWithName("couponCodes").description("????????? ?????? ?????? ?????????")),
                responseFields(
                        beneathPath("data").withSubsectionId("data"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("?????? ??????"),
                        fieldWithPath("couponCode").type(JsonFieldType.STRING).description("?????? ??????"),
                        fieldWithPath("amount").type(JsonFieldType.NUMBER)
                                .description("????????? ??????(????????????/?????????/??????????????????)"),
                        fieldWithPath("minOrderAmount").type(JsonFieldType.NUMBER)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("maxDiscountAmount").type(JsonFieldType.NUMBER)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("canBeOverlapped").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("couponTypeCode").type(JsonFieldType.STRING)
                                .description("????????? ??????(????????????/????????????/???????????????)"),
                        fieldWithPath("expireDate").type(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("isUsed").type(JsonFieldType.BOOLEAN).description("?????? ??????"),
                        fieldWithPath("couponBound").type(JsonFieldType.STRING)
                                .description("?????? ?????? ?????? ??????(ISBN/CategoryId)"),
                        fieldWithPath("couponBoundCode").type(JsonFieldType.STRING)
                                .description("?????? ?????? ?????? ?????? ??????(ALL/CATEGORY/PRODUCT)")
                )
        ));
    }
}
