package com.sysmatic2.finalbe.strategy.entity;

import com.sysmatic2.finalbe.common.Auditable;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

import java.math.BigDecimal;
import java.time.YearMonth;

@Entity
@Table(name = "monthly_statistics")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder //테스트용
public class MonthlyStatisticsEntity extends Auditable {
    @ManyToOne
    @JoinColumn(name = "strategy_id", nullable = false)
    private StrategyEntity strategyEntity; // 전략 FK

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "monthly_statistics_id", nullable = false)
    private Long monthlyStatisticsId; // 전략 월간 통계 ID

    @Column(name = "analysis_month", nullable = false)
    private String analysisMonth; // 년월

    @Column(name = "monthly_average_principal", nullable = false, precision = 19, scale = 4)
    private BigDecimal monthlyAvgPrincipal; //월평균 원금 - 해당 월의 원금들의 평균값

    @Column(name = "monthly_dep_wd_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal monthlyDepWdAmount; // 월 입출금 총액 - 해당 월의 입출금액 총합

    @Column(name = "monthly_profit_loss", nullable = false, precision = 19, scale = 4)
    private BigDecimal monthlyProfitLoss; // 월손익 - 해당 월의 일손익 합산

    @Column(name = "monthly_return", nullable = false, precision = 10, scale = 4)
    private BigDecimal monthlyReturn; // 월 손익률 - (해당월 마지막 기준가 - 저번달 마지막 기준가) / 저번달 마지막 기준가

    @Column(name = "monthly_cumulative_profit_loss", nullable = false, precision = 19, scale = 4)
    private BigDecimal monthlyCumulativeProfitLoss; // 월누적손익 - 해당월 포함 월손익 합산

    @Column(name = "monthly_cumulative_return", nullable = false, precision = 10, scale = 4)
    private BigDecimal monthlyCumulativeReturn; // 월누적손익률(%) - 해당월 마지막 기준가 / 1000 - 1

    /**
     * 엔티티 데이터를 리스트로 매핑하는 메서드
     *
     * @return 필드 값 리스트
     */
    public List<Object> toList() {
        List<Object> data = new ArrayList<>();
        data.add(this.getMonthlyStatisticsId());
        data.add(this.getStrategyEntity().getStrategyId());
        data.add(this.getAnalysisMonth());
        data.add(this.getMonthlyAvgPrincipal());
        data.add(this.getMonthlyDepWdAmount());
        data.add(this.getMonthlyProfitLoss());
        data.add(this.getMonthlyReturn());
        data.add(this.getMonthlyCumulativeProfitLoss());
        data.add(this.getMonthlyCumulativeReturn());
        return data;
    }
}
