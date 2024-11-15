package com.sysmatic2.finalbe.strategy.repository;

import com.sysmatic2.finalbe.strategy.entity.StrategyEntity;
import com.sysmatic2.finalbe.strategy.entity.TradingTypeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StrategyRepositoryTest {
    @Autowired
    private StrategyRepository strategyRepository;

    @Autowired
    private TradingTypeRepository tradingTypeRepository;

    @BeforeEach
    void setUp() {
        strategyRepository.deleteAll();
        tradingTypeRepository.deleteAll();
    }

    private TradingTypeEntity createAndSaveTradingType(int order, String isActive) {
        TradingTypeEntity tradingType = new TradingTypeEntity();
        tradingType.setTradingTypeName("Trading Type " + order);
        tradingType.setTradingTypeOrder(order);
        tradingType.setTradingTypeIcon("icon_" + order + ".png");
        tradingType.setIsActive(isActive);
        return tradingTypeRepository.save(tradingType);
    }


    @Test
    @DisplayName("20개의 전략 엔티티와 TradingType 연관 저장 테스트")
    void saveTwentyStrategyEntitiesWithTradingType() {
        // Given: 20개의 TradingTypeEntity 생성 및 저장
        List<TradingTypeEntity> tradingTypes = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            tradingTypes.add(createAndSaveTradingType(i, i % 2 == 0 ? "Y" : "N"));
        }

        // Given: 20개의 StrategyEntity 생성하여 TradingTypeEntity와 연관 설정
        List<StrategyEntity> strategies = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            StrategyEntity strategy = new StrategyEntity();
            strategy.setStrategyTitle("Strategy " + i);
            strategy.setWriterId((long) i);
            strategy.setIsPosted(i % 2 == 0 ? "Y" : "N");
            strategy.setMinInvestmentAmount("100000" + i);
            strategy.setWritedAt(LocalDateTime.now().minusDays(i));
            strategy.setPrincipal(new BigDecimal("1000000").add(BigDecimal.valueOf(i * 10000)));
            strategy.setStrategyStatusCode(i % 2 == 0 ? "STRATEGY_STATUS_ACTIVE" : "STRATEGY_STATUS_INACTIVE");
            strategy.setTradingCycleCode("WEEKLY");
            strategy.setFollowersCount((long) (i * 10));
            strategy.setCumulativeReturn(new BigDecimal(i).multiply(new BigDecimal("1.5")));
            strategy.setOneYearReturn(new BigDecimal(i).multiply(new BigDecimal("2.0")));
            strategy.setMdd(new BigDecimal("5.0").add(new BigDecimal(i)));
            strategy.setSmScore(new BigDecimal("50.0").subtract(new BigDecimal(i)));
            strategy.setStrategyOverview("Overview for Strategy " + i);
            strategy.setStrategyOperationDays(i * 10);

            // 각 전략에 매매유형(tradingType) 설정
            strategy.setTradingTypeEntity(tradingTypes.get(i - 1));
            strategies.add(strategy);
        }

        // When: 20개의 StrategyEntity 저장
        strategyRepository.saveAll(strategies);

        // Then: 20개의 StrategyEntity가 저장되었는지 확인
        List<StrategyEntity> savedStrategies = strategyRepository.findAll();
        assertEquals(20, savedStrategies.size());
    }

    @Test
    @DisplayName("전략 저장 및 조회")
    void saveAndFindStrategy() {
        // Given
        TradingTypeEntity tradingType = createAndSaveTradingType(1, "Y");

        StrategyEntity strategy = new StrategyEntity();
        strategy.setStrategyTitle("Test Strategy");
        strategy.setWriterId(1L);
        strategy.setIsPosted("Y");
        strategy.setMinInvestmentAmount("100000");
        strategy.setWritedAt(LocalDateTime.now());
        strategy.setPrincipal(new BigDecimal("1000000"));
        strategy.setStrategyStatusCode("STRATEGY_STATUS_ACTIVE");
        strategy.setTradingCycleCode("WEEKLY");
        strategy.setFollowersCount(100L);
        strategy.setTradingTypeEntity(tradingType);

        // When
        strategyRepository.save(strategy);
        Optional<StrategyEntity> retrievedStrategy = strategyRepository.findById(strategy.getStrategyId());

        // Then
        assertTrue(retrievedStrategy.isPresent());
        assertEquals("Test Strategy", retrievedStrategy.get().getStrategyTitle());
    }

    @Test
    @DisplayName("작성자 ID로 전략 목록 조회")
    void findByWriterId() {
        // Given
        TradingTypeEntity tradingType1 = createAndSaveTradingType(1, "Y");
        TradingTypeEntity tradingType2 = createAndSaveTradingType(2, "Y");

        StrategyEntity strategy1 = new StrategyEntity();
        strategy1.setWriterId(1L);
        strategy1.setStrategyTitle("Strategy 1");
        strategy1.setIsPosted("Y");
        strategy1.setMinInvestmentAmount("100000");
        strategy1.setWritedAt(LocalDateTime.now());
        strategy1.setPrincipal(new BigDecimal("1000000"));
        strategy1.setStrategyStatusCode("STRATEGY_STATUS_ACTIVE");
        strategy1.setTradingCycleCode("WEEKLY");
        strategy1.setFollowersCount(50L);
        strategy1.setTradingTypeEntity(tradingType1);

        StrategyEntity strategy2 = new StrategyEntity();
        strategy2.setWriterId(1L);
        strategy2.setStrategyTitle("Strategy 2");
        strategy2.setIsPosted("Y");
        strategy2.setMinInvestmentAmount("200000");
        strategy2.setWritedAt(LocalDateTime.now());
        strategy2.setPrincipal(new BigDecimal("1500000"));
        strategy2.setStrategyStatusCode("STRATEGY_STATUS_ACTIVE");
        strategy2.setTradingCycleCode("MONTHLY");
        strategy2.setFollowersCount(70L);
        strategy2.setTradingTypeEntity(tradingType2);

        strategyRepository.save(strategy1);
        strategyRepository.save(strategy2);

        // When
        List<StrategyEntity> strategies = strategyRepository.findByWriterId(1L);

        // Then
        assertEquals(2, strategies.size());
    }

    @Test
    @DisplayName("전략 상태로 조회")
    void findByStrategyStatusCode() {
        // Given
        StrategyEntity activeStrategy = new StrategyEntity();
        activeStrategy.setStrategyTitle("Active Strategy");
        activeStrategy.setWriterId(1L);
        activeStrategy.setIsPosted("Y");
        activeStrategy.setMinInvestmentAmount("100000");
        activeStrategy.setWritedAt(LocalDateTime.now());
        activeStrategy.setPrincipal(new BigDecimal("1000000"));
        activeStrategy.setFollowersCount(100L);
        activeStrategy.setStrategyStatusCode("STRATEGY_STATUS_ACTIVE");
        activeStrategy.setTradingCycleCode("WEEKLY");
        strategyRepository.save(activeStrategy);

        StrategyEntity inactiveStrategy = new StrategyEntity();
        inactiveStrategy.setStrategyTitle("Inactive Strategy");
        inactiveStrategy.setWriterId(2L);
        inactiveStrategy.setIsPosted("Y");
        inactiveStrategy.setMinInvestmentAmount("200000");
        inactiveStrategy.setWritedAt(LocalDateTime.now().minusDays(1));
        inactiveStrategy.setPrincipal(new BigDecimal("2000000"));
        inactiveStrategy.setFollowersCount(50L);
        inactiveStrategy.setStrategyStatusCode("STRATEGY_STATUS_INACTIVE");
        inactiveStrategy.setTradingCycleCode("MONTHLY");
        strategyRepository.save(inactiveStrategy);

        // When
        List<StrategyEntity> activeStrategies = strategyRepository.findByStrategyStatusCode("STRATEGY_STATUS_ACTIVE");

        // Then
        assertEquals(1, activeStrategies.size());
        assertEquals("STRATEGY_STATUS_ACTIVE", activeStrategies.get(0).getStrategyStatusCode());
    }

    @Test
    @DisplayName("팔로워 수가 특정 값 이상인 전략 조회")
    void findByFollowersCountGreaterThanEqual() {
        // Given
        TradingTypeEntity tradingType1 = createAndSaveTradingType(1, "Y");
        TradingTypeEntity tradingType2 = createAndSaveTradingType(2, "Y");

        StrategyEntity strategy1 = new StrategyEntity();
        strategy1.setFollowersCount(100L);
        strategy1.setStrategyTitle("Strategy 1");
        strategy1.setWriterId(1L);
        strategy1.setIsPosted("Y");
        strategy1.setMinInvestmentAmount("100000");
        strategy1.setWritedAt(LocalDateTime.now());
        strategy1.setPrincipal(new BigDecimal("1000000"));
        strategy1.setStrategyStatusCode("STRATEGY_STATUS_ACTIVE");
        strategy1.setTradingCycleCode("WEEKLY");
        strategy1.setTradingTypeEntity(tradingType1);

        StrategyEntity strategy2 = new StrategyEntity();
        strategy2.setFollowersCount(200L);
        strategy2.setStrategyTitle("Strategy 2");
        strategy2.setWriterId(2L);
        strategy2.setIsPosted("Y");
        strategy2.setMinInvestmentAmount("200000");
        strategy2.setWritedAt(LocalDateTime.now());
        strategy2.setPrincipal(new BigDecimal("1500000"));
        strategy2.setStrategyStatusCode("STRATEGY_STATUS_ACTIVE");
        strategy2.setTradingCycleCode("WEEKLY");
        strategy2.setTradingTypeEntity(tradingType2);

        strategyRepository.save(strategy1);
        strategyRepository.save(strategy2);

        // When
        List<StrategyEntity> popularStrategies = strategyRepository.findByFollowersCountGreaterThanEqual(100L);

        // Then
        assertEquals(2, popularStrategies.size());
    }


    @Test
    @DisplayName("운용일 수에 따른 전략 조회")
    void findByStrategyOperationDays() {
        // Given
        TradingTypeEntity tradingType1 = createAndSaveTradingType(1, "Y");
        TradingTypeEntity tradingType2 = createAndSaveTradingType(2, "Y");

        StrategyEntity strategy1 = new StrategyEntity();
        strategy1.setStrategyOperationDays(10);
        strategy1.setStrategyTitle("Strategy 1");
        strategy1.setWriterId(1L);
        strategy1.setIsPosted("Y");
        strategy1.setMinInvestmentAmount("100000");
        strategy1.setWritedAt(LocalDateTime.now());
        strategy1.setPrincipal(new BigDecimal("1000000"));
        strategy1.setStrategyStatusCode("STRATEGY_STATUS_ACTIVE");
        strategy1.setTradingCycleCode("WEEKLY");
        strategy1.setFollowersCount(50L);
        strategy1.setTradingTypeEntity(tradingType1);

        StrategyEntity strategy2 = new StrategyEntity();
        strategy2.setStrategyOperationDays(20);
        strategy2.setStrategyTitle("Strategy 2");
        strategy2.setWriterId(2L);
        strategy2.setIsPosted("Y");
        strategy2.setMinInvestmentAmount("200000");
        strategy2.setWritedAt(LocalDateTime.now());
        strategy2.setPrincipal(new BigDecimal("1500000"));
        strategy2.setStrategyStatusCode("STRATEGY_STATUS_ACTIVE");
        strategy2.setTradingCycleCode("WEEKLY");
        strategy2.setFollowersCount(100L);
        strategy2.setTradingTypeEntity(tradingType2);

        strategyRepository.save(strategy1);
        strategyRepository.save(strategy2);

        // When
        List<StrategyEntity> strategiesWith10Days = strategyRepository.findByStrategyOperationDays(10);

        // Then
        assertEquals(1, strategiesWith10Days.size());
        assertEquals(10, strategiesWith10Days.get(0).getStrategyOperationDays());
    }


    @Test
    @DisplayName("누적 수익률이 특정 값 이상인 전략 조회")
    void findByCumulativeReturnGreaterThanEqual() {
        // Given
        TradingTypeEntity tradingType1 = createAndSaveTradingType(1, "Y");
        TradingTypeEntity tradingType2 = createAndSaveTradingType(2, "Y");

        StrategyEntity strategy1 = new StrategyEntity();
        strategy1.setCumulativeReturn(new BigDecimal("10.5"));
        strategy1.setStrategyTitle("Strategy 1");
        strategy1.setWriterId(1L);
        strategy1.setIsPosted("Y");
        strategy1.setMinInvestmentAmount("100000");
        strategy1.setWritedAt(LocalDateTime.now());
        strategy1.setPrincipal(new BigDecimal("1000000"));
        strategy1.setStrategyStatusCode("STRATEGY_STATUS_ACTIVE");
        strategy1.setTradingCycleCode("WEEKLY");
        strategy1.setFollowersCount(50L);
        strategy1.setTradingTypeEntity(tradingType1);

        StrategyEntity strategy2 = new StrategyEntity();
        strategy2.setCumulativeReturn(new BigDecimal("20.0"));
        strategy2.setStrategyTitle("Strategy 2");
        strategy2.setWriterId(2L);
        strategy2.setIsPosted("Y");
        strategy2.setMinInvestmentAmount("200000");
        strategy2.setWritedAt(LocalDateTime.now());
        strategy2.setPrincipal(new BigDecimal("1500000"));
        strategy2.setStrategyStatusCode("STRATEGY_STATUS_ACTIVE");
        strategy2.setTradingCycleCode("WEEKLY");
        strategy2.setFollowersCount(100L);
        strategy2.setTradingTypeEntity(tradingType2);

        strategyRepository.save(strategy1);
        strategyRepository.save(strategy2);

        // When
        List<StrategyEntity> highReturnStrategies = strategyRepository.findByCumulativeReturnGreaterThanEqual(new BigDecimal("15.0"));

        // Then
        assertEquals(1, highReturnStrategies.size());
        assertEquals(0, highReturnStrategies.get(0).getCumulativeReturn().compareTo(new BigDecimal("20.0")));
    }

    @Test
    @DisplayName("전략 삭제")
    void deleteStrategy() {
        // Given
        TradingTypeEntity tradingType = createAndSaveTradingType(1, "Y");

        StrategyEntity strategy = new StrategyEntity();
        strategy.setStrategyTitle("Delete Strategy");
        strategy.setWriterId(1L);
        strategy.setIsPosted("Y");
        strategy.setMinInvestmentAmount("100000");
        strategy.setWritedAt(LocalDateTime.now());
        strategy.setPrincipal(new BigDecimal("1000000"));
        strategy.setStrategyStatusCode("STRATEGY_STATUS_ACTIVE");
        strategy.setTradingCycleCode("WEEKLY");
        strategy.setFollowersCount(50L);
        strategy.setTradingTypeEntity(tradingType);

        strategyRepository.save(strategy);

        // When
        strategyRepository.delete(strategy);
        Optional<StrategyEntity> retrievedStrategy = strategyRepository.findById(strategy.getStrategyId());

        // Then
        assertFalse(retrievedStrategy.isPresent());
    }


    @Test
    @DisplayName("작성일을 기준으로 전략 정렬 조회")
    void findByOrderByWritedAtDesc() {
        // Given
        TradingTypeEntity tradingType1 = createAndSaveTradingType(1, "Y");
        TradingTypeEntity tradingType2 = createAndSaveTradingType(2, "Y");

        StrategyEntity strategy1 = new StrategyEntity();
        strategy1.setStrategyTitle("Strategy 1");
        strategy1.setWriterId(1L);
        strategy1.setIsPosted("Y");
        strategy1.setMinInvestmentAmount("100000");
        strategy1.setWritedAt(LocalDateTime.now().minusDays(1));
        strategy1.setPrincipal(new BigDecimal("1000000"));
        strategy1.setStrategyStatusCode("STRATEGY_STATUS_ACTIVE");
        strategy1.setTradingCycleCode("WEEKLY");
        strategy1.setFollowersCount(50L);
        strategy1.setTradingTypeEntity(tradingType1);

        StrategyEntity strategy2 = new StrategyEntity();
        strategy2.setStrategyTitle("Strategy 2");
        strategy2.setWriterId(2L);
        strategy2.setIsPosted("Y");
        strategy2.setMinInvestmentAmount("200000");
        strategy2.setWritedAt(LocalDateTime.now());
        strategy2.setPrincipal(new BigDecimal("1500000"));
        strategy2.setStrategyStatusCode("STRATEGY_STATUS_ACTIVE");
        strategy2.setTradingCycleCode("WEEKLY");
        strategy2.setFollowersCount(100L);
        strategy2.setTradingTypeEntity(tradingType2);

        strategyRepository.save(strategy1);
        strategyRepository.save(strategy2);

        // When
        List<StrategyEntity> sortedStrategies = strategyRepository.findByOrderByWritedAtDesc();

        // Then
        assertEquals(2, sortedStrategies.size());
        assertTrue(sortedStrategies.get(0).getWritedAt().isAfter(sortedStrategies.get(1).getWritedAt()));
    }
}