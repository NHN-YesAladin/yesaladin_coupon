package shop.yesaladin.coupon.coupon.service.inter;

import shop.yesaladin.coupon.message.CouponCodesAndResultMessage;
import shop.yesaladin.coupon.message.CouponCodesMessage;
import shop.yesaladin.coupon.message.CouponGiveRequestMessage;
import shop.yesaladin.coupon.message.CouponGiveRequestResponseMessage;

/**
 * 쿠폰 관련 요청 메시지를 처리하기 위한 서비스 인터페이스니다.
 *
 * @author 서민지
 * @since 1.0
 */
public interface GiveCouponService {

    /**
     * Shop 서버에서 발행한 쿠폰 지급 요청 메시지를 처리 후 응답메시지를 발행합니다. 지급할 발행 쿠폰의 지급 상태를 "지급 대기" 상태로 업데이트합니다. 발행쿠폰이
     * 존재하지 않는 경우 Client Exception 을 발생시킵니다.
     *
     * @param message 쿠폰 지급 요청 메시지
     * @return
     */
    CouponGiveRequestResponseMessage consumeCouponGiveRequestMessage(CouponGiveRequestMessage message);

    /**
     * Shop 서버에서 발행한 쿠폰 지급 결과 메시지를 처리합니다. 지급 성공시 발행쿠폰의 지급 상태(지급완료)와 지급 일시를 업데이트합니다. 지급 실패시 지급 상태를
     * "미지급" 상태로 업데이트합니다.
     *
     * @param message 쿠폰 지급 결과 메시지
     */
    void consumeCouponGivenMessage(CouponCodesAndResultMessage message);

    /**
     * Shop 서버에서 발행한 쿠폰 지급 취소 메시지를 처리합니다. 발행쿠폰의 지급 상태(미지급)와 지급 일시(null)를 업데이트합니다.
     *
     * @param message 쿠폰 지급 취소 메시지
     */
    void consumeCouponGiveRequestCancelMessage(CouponCodesMessage message);
}
