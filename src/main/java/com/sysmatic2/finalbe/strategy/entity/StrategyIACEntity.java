package com.sysmatic2.finalbe.strategy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "strategy_investment_asset_classes")
@IdClass(StrategyIACId.class)
@Getter
@Setter
@ToString
public class StrategyIACEntity extends Auditable {
    //복합키(@IdClass) - 전략ID, 투자자산분류ID
    @Id
    @ManyToOne
    @JoinColumn(name="strategy_id", nullable = false)
    private StrategyEntity strategyEntity; //전략 ID

    @Id
    @ManyToOne
    @JoinColumn(name="investment_asset_classes_id", nullable = false)
    private InvestmentAssetClassesEntity investmentAssetClassesEntity; //투자자산분류 ID

    @Column(name="is_active", nullable = false, columnDefinition = "CHAR(1)")
    @Pattern(regexp = "Y|N", message = "isActive 필드는 'Y' 또는 'N'만 허용됩니다.")
    private String isActive; //사용유무

    @CreatedBy
    @Column(name="writer_id", nullable = false)
    private Long writedBy; //작성자Id

    @CreatedDate
    @Column(name="writed_at", nullable = false)
    private LocalDateTime writedAt; //작성일시

    @LastModifiedBy
    @Column(name="updater_id")
    private Long updatedBy; //수정자Id

    @LastModifiedDate
    @Column(name="updated_at")
    private LocalDateTime updatedAt; //수정일시
}
