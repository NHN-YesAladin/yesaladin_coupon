package shop.yesaladin.coupon.coupon.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.coupon.config.KafkaTopicConfig;
import shop.yesaladin.coupon.coupon.dto.CouponIssueRequestDto;
import shop.yesaladin.coupon.coupon.dto.CouponIssueResponseDto;
import shop.yesaladin.coupon.coupon.kafka.CouponProducer;
import shop.yesaladin.coupon.coupon.service.inter.CouponConsumerService;
import shop.yesaladin.coupon.coupon.service.inter.QueryIssuedCouponService;
import shop.yesaladin.coupon.dto.CouponGiveDto;
import shop.yesaladin.coupon.message.CouponGiveRequestMessage;
import shop.yesaladin.coupon.message.CouponGiveRequestResponseMessage;

@RequiredArgsConstructor
@Service
public class CouponConsumerServiceImpl implements CouponConsumerService {

    private final CouponProducer couponProducer;
    private final QueryIssuedCouponService queryIssuedCouponService;

    @Override
    public void responseCouponGiveRequestMessage(CouponGiveRequestMessage message) {
        List<CouponIssueResponseDto> responseDtoList = null;
        try {
            responseDtoList = queryIssuedCouponService.getCouponIssueResponseDtoList(
                    CouponIssueRequestDto.fromCouponGiveRequestMessage(message));
        } catch (Exception e) {
            // 쿠폰 발행 실패 응답
            CouponGiveRequestResponseMessage giveRequestResponseMessage = CouponGiveRequestResponseMessage.builder()
                    .requestId(message.getRequestId())
                    .success(false)
                    .errorMessage(e.getMessage())
                    .build();
            couponProducer.responseGiveRequest(giveRequestResponseMessage);
            return;
        }

        assert responseDtoList != null;
        List<CouponGiveDto> coupons = responseDtoList.stream()
                .map(CouponIssueResponseDto::toCouponGiveDto)
                .collect(Collectors.toList());

        // 동일한 requestId 를 포함하는 지급 요청 응답 메시지를 보낸다.
        CouponGiveRequestResponseMessage giveRequestResponseMessage = CouponGiveRequestResponseMessage.builder()
                .requestId(message.getRequestId())
                .coupons(coupons)
                .success(true)
                .build();

        couponProducer.responseGiveRequest(giveRequestResponseMessage);
    }
}
