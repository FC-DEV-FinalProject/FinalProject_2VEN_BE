package com.sysmatic2.finalbe.strategy.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class StrategyRegistrationDto {
    private List<TradingTypeRegistrationDto> tradingTypeRegistrationDtoList;
    private List<InvestmentAssetClassesRegistrationDto> investmentAssetClassesRegistrationDtoList;
}
