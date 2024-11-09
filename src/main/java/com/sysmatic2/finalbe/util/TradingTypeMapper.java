package com.sysmatic2.finalbe.util;

import com.sysmatic2.finalbe.strategy.dto.TradingTypeRequestDto;
import com.sysmatic2.finalbe.strategy.dto.TradingTypeResponseDto;
import com.sysmatic2.finalbe.strategy.entity.TradingTypeEntity;
import org.springframework.stereotype.Component;

@Component
public class TradingTypeMapper {
    // 엔티티를 응답 DTO로 변환하는 메서드
    public TradingTypeResponseDto toDto(TradingTypeEntity tradingTypeEntity) {
        TradingTypeResponseDto dto = new TradingTypeResponseDto();
        dto.setTradingTypeId(tradingTypeEntity.getTradingTypeId());
        dto.setTradingTypeOrder(tradingTypeEntity.getTradingTypeOrder());
        dto.setTradingTypeName(tradingTypeEntity.getTradingTypeName());
        dto.setTradingTypeIcon(tradingTypeEntity.getTradingTypeIcon());
        dto.setIsActive(tradingTypeEntity.getIsActive());
        return dto;
    }

    // 요청 DTO를 엔티티로 변환하는 메서드
    public TradingTypeEntity toEntity(TradingTypeRequestDto dto) {
        TradingTypeEntity tradingTypeEntity = new TradingTypeEntity();
        tradingTypeEntity.setTradingTypeOrder(dto.getTradingTypeOrder());
        tradingTypeEntity.setTradingTypeName(dto.getTradingTypeName());
        tradingTypeEntity.setTradingTypeIcon(dto.getTradingTypeIcon());
        tradingTypeEntity.setIsActive(dto.getIsActive());
        return tradingTypeEntity;
    }
}
