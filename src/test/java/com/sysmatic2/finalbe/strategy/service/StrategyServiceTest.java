package com.sysmatic2.finalbe.strategy.service;

import com.sysmatic2.finalbe.strategy.dto.StrategyPayloadDto;
import com.sysmatic2.finalbe.admin.repository.InvestmentAssetClassesRepository;
import com.sysmatic2.finalbe.strategy.repository.StrategyStandardCodeRepository;
import com.sysmatic2.finalbe.admin.repository.TradingTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class StrategyServiceTest {
    @Autowired
    private StrategyService strategyService;
    @Autowired
    private InvestmentAssetClassesRepository iacRepo;
    @Autowired
    private TradingTypeRepository ttRepo;
    @Autowired
    private StrategyStandardCodeRepository standardCodeRepo;

//    @Test
//    public void strategyInputTest() throws Exception {
//        //총 5개의 DTO 생성, i번째 DTO는 i개의 투자자산 분류를 가지고 있다.
//        for(int i = 1; i <= 5; i++){
//            StrategyPayloadDto strategyPayloadDto = new StrategyPayloadDto();
//            strategyPayloadDto.setStrategyTitle("testing"+i);
//            strategyPayloadDto.setTradingTypeId(i);
//            strategyPayloadDto.setTradingCycleCode("TESTING");
//            strategyPayloadDto.setMinInvestmentAmount("TESTING");
//            List<Integer> tempList = new ArrayList<>();
//            for(int j = 0; j < i; j++){
//                tempList.add(j+1);
//            }
//            strategyPayloadDto.setInvestmentAssetClassesIdList(tempList);
//            if(i%2 == 0){
//                strategyPayloadDto.setIsPosted("Y");
//            }
//            strategyPayloadDto.setIsPosted("N");
//
//            strategyService.register(strategyPayloadDto);
//        }
//    }

    @Test
    public void testRegister_Successful() throws Exception {
        // Given
        StrategyPayloadDto dto = new StrategyPayloadDto();

        dto.setStrategyTitle("Test Strategy");
        dto.setTradingTypeId(1);
        dto.setTradingCycleCode("STRATEGY_TRADINGCYCLE_DAY");
        dto.setMinInvestmentAmount("1~1000");
        dto.setStrategyOverview("Test Overview");
        List<Integer> tempList = new ArrayList<>();
            for(int j = 0; j < 2; j++){
                tempList.add(j+1);
            }
        dto.setInvestmentAssetClassesIdList(tempList);
        dto.setIsPosted("Y");

        // When
        strategyService.register(dto);


    }


}
