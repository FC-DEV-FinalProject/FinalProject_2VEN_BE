package com.sysmatic2.finalbe.strategy.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TradingTypeResponseDto {
    private Integer tradingTypeId; // 매매유형 ID
    private Integer tradingTypeOrder; // 매매유형순서
    private String tradingTypeName; // 매매유형명
    private String tradingTypeIcon; // 매매유형 아이콘 이미지 URL
    private String isActive; // 사용유무
}
