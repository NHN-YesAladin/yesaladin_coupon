package shop.yesaladin.coupon.coupon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.service.inter.CommandTriggerService;

/**
 * 쿠폰의 트리거와 관련된 요청을 처리하는 controller 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/triggers")
public class CommandTriggerController {

    private final CommandTriggerService commandTriggerService;

    /**
     * 쿠폰의 발행을 중단합니다.
     *
     * @param triggerTypeCode 중단시킬 쿠폰의 트리거 타입
     * @param couponId        중단시킬 쿠폰의 아이디
     * @return HTTP 응답 메시지를 담은 dto
     */
    @DeleteMapping(params = {"trigger-type", "coupon-id"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Void> stopIssueCoupon(
            @RequestParam(name = "trigger-type") TriggerTypeCode triggerTypeCode,
            @RequestParam(name = "coupon-id") Long couponId
    ) {
        commandTriggerService.stopIssueCoupon(triggerTypeCode, couponId);

        return ResponseDto.<Void>builder()
                .status(HttpStatus.OK)
                .success(true)
                .data(null)
                .build();
    }
}
