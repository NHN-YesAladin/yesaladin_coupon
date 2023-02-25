package shop.yesaladin.coupon.coupon.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import shop.yesaladin.coupon.config.GatewayProperties;
import shop.yesaladin.coupon.coupon.service.inter.GiveCouponService;
import shop.yesaladin.coupon.message.CouponCodesAndResultMessage;
import shop.yesaladin.coupon.message.CouponGiveRequestResponseMessage;

@RequiredArgsConstructor
@Component
public class CouponEventHandler {

    private final GiveCouponService giveCouponService;
    private final GatewayProperties gatewayProperties;
    private final RestTemplate restTemplate;

    @EventListener(CouponGiveRequestEvent.class)
    public void handleCouponGiveRequestEvent(CouponGiveRequestEvent event) {
        CouponGiveRequestResponseMessage responseMessage = giveCouponService.giveCoupon(event.getRequest());
        String requestUri = UriComponentsBuilder.fromUriString(gatewayProperties.getShopUrl())
                .pathSegment("coupons", "messages", "give", "response")
                .toUriString();
        RequestEntity<CouponGiveRequestResponseMessage> requestEntity = RequestEntity.post(
                        requestUri)
                .body(responseMessage);

        restTemplate.exchange(requestEntity, Void.class);
    }

    @EventListener(CouponCodesAndResultMessage.class)
    public void handleCouponGivenResultEvent(CouponGivenResultEvent event) {
        giveCouponService.finishGiveCouponRequest(event.getResult());
    }

    @EventListener(CouponCancelGiveEvent.class)
    public void handleCouponCancelGiveMessageEvent(CouponCancelGiveEvent event) {
        giveCouponService.cancelGiveCoupon(event.getMessage());
    }
}
