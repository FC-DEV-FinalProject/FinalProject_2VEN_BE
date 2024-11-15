package com.sysmatic2.finalbe.strategy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@ToString
@Entity
@Table(name = "investment_asset_classes") //투자자산 분류
public class InvestmentAssetClassesEntity extends Auditable{
    @Id
    @Column(name="investment_asset_classes_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer investmentAssetClassesId; //투자자산 분류 ID

    @Column(name = "investment_asset_classes_order", nullable = false, unique = true)
    private Integer order; //투자자산 분류 순서

    @Column(name = "investment_asset_classes_name", length = 50, nullable = false, unique = true)
    private String investmentAssetClassesName; //투자자산 분류명

    @Column(name = "investment_asset_classes_icon")
    private String investmentAssetClassesIcon; //투자자산 분류 아이콘

    //사용X
    @Column(name = "introduce", length = 3000, nullable = true)
    private String introduce; //투자자산분류 설명

    @Column(name = "is_Active", length = 1, nullable = false)
    private String isActive; //사용 유무

    //IAC(1) : 관계(N)
    @OneToMany(mappedBy = "investmentAssetClassesEntity")
    private List<StrategyIACEntity> strategyIACEntities;

    //equals()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvestmentAssetClassesEntity that = (InvestmentAssetClassesEntity) o;
        return Objects.equals(order, that.order) && Objects.equals(investmentAssetClassesName, that.investmentAssetClassesName) && Objects.equals(investmentAssetClassesIcon, that.investmentAssetClassesIcon) && Objects.equals(isActive, that.isActive);
    }

    //hashCode()
    @Override
    public int hashCode() {
        return Objects.hash(order, investmentAssetClassesName, investmentAssetClassesIcon, isActive);
    }
}
