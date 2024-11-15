package com.sysmatic2.finalbe.admin.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TradingTypeRegistrationDto {
    private Integer tradingTypeId; // 매매유형 ID
    private String tradingTypeName; // 매매유형명
    private String tradingTypeIcon; // 매매유형 아이콘 이미지 URL
}