package shop.yesaladin.coupon.coupon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.coupon.coupon.dto.CouponTriggerDto;
import shop.yesaladin.coupon.coupon.service.inter.CommandTriggerService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/triggers")
public class CommandTriggerController {

    private final CommandTriggerService commandTriggerService;

    @DeleteMapping(params = {"trigger-code-id", "coupon-id"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Void> stopIssueCoupon(@ModelAttribute CouponTriggerDto dto) {
        commandTriggerService.stopIssueCoupon(dto.getTriggerTypeCode(), dto.getCouponId());

        return ResponseDto.<Void>builder()
                .success(true)
                .data(null)
                .build();
    }
}
