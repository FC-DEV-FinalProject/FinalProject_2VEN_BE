package com.sysmatic2.finalbe.strategy.repository;

import com.sysmatic2.finalbe.strategy.entity.StrategyEntity;
import com.sysmatic2.finalbe.strategy.entity.TradingTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface StrategyRepository extends JpaRepository<StrategyEntity, Long> {
    // 작성자 ID로 전략 목록 조회
    List<StrategyEntity> findByWriterId(Long writerId);

    // 전략 상태 코드로 조회
    List<StrategyEntity> findByStrategyStatusCode(String strategyStatusCode);

    // 팔로워 수가 특정 값 이상인 전략 조회
    List<StrategyEntity> findByFollowersCountGreaterThanEqual(Long followersCount);

    // 운용일 수로 전략 조회
    List<StrategyEntity> findByStrategyOperationDays(int strategyOperationDays);

    // 누적 수익률이 특정 값 이상인 전략 조회
    List<StrategyEntity> findByCumulativeReturnGreaterThanEqual(BigDecimal cumulativeReturn);

    // 작성일을 기준으로 전략 정렬 조회
    List<StrategyEntity> findByOrderByWritedAtDesc();
}
