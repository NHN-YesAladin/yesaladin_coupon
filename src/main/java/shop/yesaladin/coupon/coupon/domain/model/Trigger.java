package shop.yesaladin.coupon.coupon.domain.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.coupon.persistence.converter.TriggerTypeCodeCodeConverter;

/**
 * 쿠폰 트리거 엔터티 입니다.
 *
 * @author 서민지, 김홍대
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "triggers")
@Entity
public class Trigger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trigger_code_id")
    @Convert(converter = TriggerTypeCodeCodeConverter.class)
    private TriggerTypeCode triggerTypeCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;
}
