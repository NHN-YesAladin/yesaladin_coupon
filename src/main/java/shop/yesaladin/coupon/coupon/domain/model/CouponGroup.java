package shop.yesaladin.coupon.coupon.domain.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import shop.yesaladin.coupon.coupon.persistence.converter.CouponTypeCodeConverter;
import shop.yesaladin.coupon.trigger.TriggerTypeCode;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "coupon_groups")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class CouponGroup {

    @Id
    @Column
    private Long id;

    @Column(name = "trigger_code_id", nullable = false)
    @Convert(converter = CouponTypeCodeConverter.class)
    private TriggerTypeCode triggerTypeCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Column(nullable = false, length = 36)
    private String groupCode;

    @CreatedDate
    private LocalDateTime createdDatetime;
}
