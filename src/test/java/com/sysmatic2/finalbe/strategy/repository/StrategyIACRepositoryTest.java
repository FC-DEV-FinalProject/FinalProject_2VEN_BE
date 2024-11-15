//package com.sysmatic2.finalbe.strategy.repository;
//
//import com.sysmatic2.finalbe.admin.entity.InvestmentAssetClassesEntity;
//import com.sysmatic2.finalbe.strategy.entity.StrategyEntity;
//import com.sysmatic2.finalbe.admin.entity.TradingTypeEntity;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//public class StrategyIACRepositoryTest {
//    @Autowired
//    StrategyIACRepository sIacRepo;
//
//    //테스트 데이터 넣기
//    //전략 i번에 i개의 투자자산 분류 넣기
//    @Test
//    void insertStrategyIAC(){
//        sIacRepo.deleteAll();
//
//        for(int i = 1; i <= 5; i++){
//            //전략 생성하기
//            StrategyEntity strategyEntity = new StrategyEntity();
//            strategyEntity.setStrategyId(Long.valueOf(i));
//
////            strategyEntity.setTradingTypeEntity();
//
//
//        }
//    }
//}
