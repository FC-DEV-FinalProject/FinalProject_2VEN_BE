package com.sysmatic2.finalbe.strategy.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TradingTypeRequestDto {

    @Positive(message = "매매유형 순서는 양수여야 합니다.")
    @Max(value = 100000000, message = "매매유형 순서는 100,000,000 이하만 가능합니다.")
    private Integer tradingTypeOrder; // 매매유형순서

    @NotBlank(message = "매매유형명은 필수 입력 값입니다.")
    private String tradingTypeName; // 매매유형명

    @NotBlank(message = "매매유형 아이콘 URL은 필수 입력 값입니다.")
    private String tradingTypeIcon; // 매매유형 아이콘 이미지 URL

    @Pattern(regexp = "[YN]", message = "사용유무는 'Y' 또는 'N'만 허용됩니다.")
    private String isActive; // 사용유무
}