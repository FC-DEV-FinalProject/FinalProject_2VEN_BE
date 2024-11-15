package com.sysmatic2.finalbe.strategy.service;

import com.sysmatic2.finalbe.strategy.dto.StrategyRegistrationDto;
import com.sysmatic2.finalbe.admin.entity.InvestmentAssetClassesEntity;
import com.sysmatic2.finalbe.admin.entity.TradingTypeEntity;
import com.sysmatic2.finalbe.admin.repository.InvestmentAssetClassesRepository;
import com.sysmatic2.finalbe.admin.repository.TradingTypeRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class StrategyServiceMockTest {
    @Mock
    private TradingTypeRepository ttRepo;

    @Mock
    private InvestmentAssetClassesRepository iacRepo;

    @InjectMocks
    private StrategyService strategyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Strategy Registration Form을 반환하는 메서드 테스트")
    void getStrategyRegistrationForm_ShouldReturnStrategyRegistrationDto() {
        // Given: 목 데이터를 설정합니다.
        TradingTypeEntity tradingTypeDto = new TradingTypeEntity();
        tradingTypeDto.setTradingTypeId(1);
        tradingTypeDto.setTradingTypeName("Type A");
        tradingTypeDto.setTradingTypeIcon("icon_A.png");

        InvestmentAssetClassesEntity investmentAssetClassDto = new InvestmentAssetClassesEntity();
        investmentAssetClassDto.setInvestmentAssetClassesId(1);
        investmentAssetClassDto.setInvestmentAssetClassesName("Class A");
        investmentAssetClassDto.setInvestmentAssetClassesIcon("icon_B.png");

        when(ttRepo.findByIsActiveOrderByTradingTypeOrderAsc("Y")).thenReturn(List.of(tradingTypeDto));
        when(iacRepo.findByIsActiveOrderByOrderAsc("Y")).thenReturn(List.of(investmentAssetClassDto));

        // When: 메서드 호출
        StrategyRegistrationDto result = strategyService.getStrategyRegistrationForm();

        // Then: 반환된 DTO가 예상대로 설정되었는지 검증
        assertEquals(1, result.getTradingTypeRegistrationDtoList().size());
        assertEquals(1, result.getInvestmentAssetClassesRegistrationDtoList().size());

        assertEquals("Type A", result.getTradingTypeRegistrationDtoList().get(0).getTradingTypeName());
        assertEquals("Class A", result.getInvestmentAssetClassesRegistrationDtoList().get(0).getInvestmentAssetClassesName());
    }
}