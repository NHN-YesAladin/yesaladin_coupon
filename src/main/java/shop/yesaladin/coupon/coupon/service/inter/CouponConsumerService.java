package shop.yesaladin.coupon.coupon.service.inter;

import shop.yesaladin.coupon.message.CouponCodesAndResultMessage;
import shop.yesaladin.coupon.message.CouponCodesMessage;
import shop.yesaladin.coupon.message.CouponGiveRequestMessage;

/**
 * 쿠폰 관련 요청 메시지를 처리하기 위한 서비스 인터페이스니다.
 *
 * @author 서민지
 * @since 1.0
 */
public interface CouponConsumerService {

    /**
     * Shop 서버에서 발행한 쿠폰 지급 요청 메시지를 처리 후 응답메시지를 발행합니다.
     *
     * @param message 쿠폰 지급 요청 메시지
     */
    void consumeCouponGiveRequestMessage(CouponGiveRequestMessage message);

    /**
     * Shop 서버에서 발행한 쿠폰 지급 결과 메시지를 처리합니다.
     *
     * @param message 쿠폰 지급 결과 메시지
     */
    void consumeCouponGivenMessage(CouponCodesAndResultMessage message);

    /**
     * Shop 서버에서 발행한 쿠폰 지급 취소 메시지를 처리합니다.
     *
     * @param message 쿠폰 지급 취소 메시지
     */
    void consumeCouponGiveRequestCancelMessage(CouponCodesMessage message);

    /**
     * Shop 서버에서 발행한 쿠폰 사용 요청 메시지 처리 후 응답메시지를 발행합니다.
     *
     * @param message 쿠폰 사용 요청 메시지
     */
    void consumeCouponUsedMessage(CouponCodesAndResultMessage message);

    /**
     * Shop 서버에서 발행한 쿠폰 사용 취소 메시지를 처리합니다.
     *
     * @param message 쿠폰 사용 취소 메시지
     */
    void consumeCouponUseRequestCancelMessage(CouponCodesMessage message);
}
