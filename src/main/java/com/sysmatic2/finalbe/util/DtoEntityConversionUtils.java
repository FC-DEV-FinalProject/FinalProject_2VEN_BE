package com.sysmatic2.finalbe.util;

import com.sysmatic2.finalbe.admin.dto.InvestmentAssetClassesDto;
import com.sysmatic2.finalbe.admin.dto.TradingTypeAdminRequestDto;
import com.sysmatic2.finalbe.admin.dto.TradingTypeAdminResponseDto;
import com.sysmatic2.finalbe.admin.dto.InvestmentAssetClassesRegistrationDto;
import com.sysmatic2.finalbe.admin.dto.TradingTypeRegistrationDto;
import com.sysmatic2.finalbe.admin.entity.InvestmentAssetClassesEntity;
import com.sysmatic2.finalbe.admin.entity.TradingTypeEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * DtoEntityConversionUtils
 * <p>
 * 엔티티와 DTO 간의 변환 로직을 중앙에서 관리하는 유틸리티 클래스입니다.
 * 모든 메서드는 정적 메서드로, DTO와 엔티티 간의 변환을 간단히 수행할 수 있습니다.
 */
public class DtoEntityConversionUtils {

    // 인스턴스화를 방지하기 위한 private 생성자
    private DtoEntityConversionUtils() {}

    /**
     * InvestmentAssetClassesEntity를 InvestmentAssetClassesDto로 변환하는 메서드.
     *
     * @param iacEntity 변환할 InvestmentAssetClassesEntity 객체
     * @return 변환된 InvestmentAssetClassesDto 객체
     */
    public static InvestmentAssetClassesDto toDto(InvestmentAssetClassesEntity iacEntity) {
        InvestmentAssetClassesDto iacDto = new InvestmentAssetClassesDto();
        iacDto.setInvestmentAssetClassesId(iacEntity.getInvestmentAssetClassesId());
        iacDto.setOrder(iacEntity.getOrder());
        iacDto.setInvestmentAssetClassesName(iacEntity.getInvestmentAssetClassesName());
        iacDto.setInvestmentAssetClassesIcon(iacEntity.getInvestmentAssetClassesIcon());
        iacDto.setIsActive(iacEntity.getIsActive());
        return iacDto;
    }

    /**
     * InvestmentAssetClassesDto를 InvestmentAssetClassesEntity로 변환하는 메서드.
     *
     * @param iacDto 변환할 InvestmentAssetClassesDto 객체
     * @return 변환된 InvestmentAssetClassesEntity 객체
     */
    public static InvestmentAssetClassesEntity toEntity(InvestmentAssetClassesDto iacDto) {
        InvestmentAssetClassesEntity iacEntity = new InvestmentAssetClassesEntity();
        iacEntity.setOrder(iacDto.getOrder());
        iacEntity.setInvestmentAssetClassesName(iacDto.getInvestmentAssetClassesName());
        iacEntity.setInvestmentAssetClassesIcon(iacDto.getInvestmentAssetClassesIcon());
        iacEntity.setIsActive(iacDto.getIsActive());
        return iacEntity;
    }

    /**
     * TradingTypeEntity를 TradingTypeAdminResponseDto로 변환하는 메서드.
     *
     * @param tradingTypeEntity 변환할 TradingTypeEntity 객체
     * @return 변환된 TradingTypeAdminResponseDto 객체
     */
    public static TradingTypeAdminResponseDto toDto(TradingTypeEntity tradingTypeEntity) {
        TradingTypeAdminResponseDto dto = new TradingTypeAdminResponseDto();
        dto.setTradingTypeId(tradingTypeEntity.getTradingTypeId());
        dto.setTradingTypeOrder(tradingTypeEntity.getTradingTypeOrder());
        dto.setTradingTypeName(tradingTypeEntity.getTradingTypeName());
        dto.setTradingTypeIcon(tradingTypeEntity.getTradingTypeIcon());
        dto.setIsActive(tradingTypeEntity.getIsActive());
        return dto;
    }

    /**
     * TradingTypeAdminRequestDto를 TradingTypeEntity로 변환하는 메서드.
     *
     * @param dto 변환할 TradingTypeAdminRequestDto 객체
     * @return 변환된 TradingTypeEntity 객체
     */
    public static TradingTypeEntity toEntity(TradingTypeAdminRequestDto dto) {
        TradingTypeEntity tradingTypeEntity = new TradingTypeEntity();
        tradingTypeEntity.setTradingTypeOrder(dto.getTradingTypeOrder());
        tradingTypeEntity.setTradingTypeName(dto.getTradingTypeName());
        tradingTypeEntity.setTradingTypeIcon(dto.getTradingTypeIcon());
        tradingTypeEntity.setIsActive(dto.getIsActive());
        return tradingTypeEntity;
    }

    /**
     * TradingTypeEntity 리스트를 TradingTypeRegistrationDto 리스트로 변환하는 메서드.
     *
     * @param tradingTypes 변환할 TradingTypeEntity 리스트
     * @return 변환된 TradingTypeRegistrationDto 리스트
     */
    public static List<TradingTypeRegistrationDto> convertToTradingTypeDtos(List<TradingTypeEntity> tradingTypes) {
        return tradingTypes.stream()
                .map(tradingType -> {
                    TradingTypeRegistrationDto dto = new TradingTypeRegistrationDto();
                    dto.setTradingTypeId(tradingType.getTradingTypeId());
                    dto.setTradingTypeName(tradingType.getTradingTypeName());
                    dto.setTradingTypeIcon(tradingType.getTradingTypeIcon());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * InvestmentAssetClassEntity 리스트를 InvestmentAssetClassesRegistrationDto 리스트로 변환하는 메서드.
     *
     * @param assetClasses 변환할 InvestmentAssetClassEntity 리스트
     * @return 변환된 InvestmentAssetClassesRegistrationDto 리스트
     */
    public static List<InvestmentAssetClassesRegistrationDto> convertToInvestmentAssetClassDtos(List<InvestmentAssetClassesEntity> assetClasses) {
        return assetClasses.stream()
                .map(assetClass -> {
                    InvestmentAssetClassesRegistrationDto dto = new InvestmentAssetClassesRegistrationDto();
                    dto.setInvestmentAssetClassesId(assetClass.getInvestmentAssetClassesId());
                    dto.setInvestmentAssetClassesName(assetClass.getInvestmentAssetClassesName());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
