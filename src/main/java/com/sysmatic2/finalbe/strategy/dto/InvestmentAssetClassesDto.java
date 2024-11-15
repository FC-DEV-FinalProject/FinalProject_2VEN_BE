package com.sysmatic2.finalbe.strategy.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentAssetClassesDto {
    //프론트로 보내지는 DTO
    private Integer investmentAssetClassesId;
    private Integer order;
    private String investmentAssetClassesName;
    private String investmentAssetClassesIcon;
    private String isActive;
}

