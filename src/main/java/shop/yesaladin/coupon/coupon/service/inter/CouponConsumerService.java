package shop.yesaladin.coupon.coupon.service.inter;

import shop.yesaladin.coupon.message.CouponGiveRequestMessage;

public interface CouponConsumerService {

    void responseCouponGiveRequestMessage(CouponGiveRequestMessage message);
}
