package com.sysmatic2.finalbe.admin.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentAssetClassesPayloadDto {
    //등록용 DTO
    @Positive
    @Max(value=1000)
    private Integer order; //null 가능

    @Size(max=40)
    @NotEmpty
    private String investmentAssetClassesName;

    private String investmentAssetClassesIcon;

    @Pattern(regexp = "Y|N")
    private String isActive;
}
