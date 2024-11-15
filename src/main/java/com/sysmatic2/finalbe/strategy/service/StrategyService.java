package com.sysmatic2.finalbe.strategy.service;

import com.sysmatic2.finalbe.StandardCodeEntity;
import com.sysmatic2.finalbe.exception.TradingTypeNotFoundException;
import com.sysmatic2.finalbe.strategy.dto.InvestmentAssetClassesRegistrationDto;
import com.sysmatic2.finalbe.strategy.dto.StrategyPayloadDto;
import com.sysmatic2.finalbe.strategy.dto.StrategyRegistrationDto;
import com.sysmatic2.finalbe.strategy.dto.TradingTypeRegistrationDto;
import com.sysmatic2.finalbe.strategy.entity.InvestmentAssetClassesEntity;
import com.sysmatic2.finalbe.strategy.entity.StrategyEntity;
import com.sysmatic2.finalbe.strategy.entity.TradingTypeEntity;
import com.sysmatic2.finalbe.strategy.repository.InvestmentAssetClassesRepository;
import com.sysmatic2.finalbe.strategy.repository.StrategyRepository;
import com.sysmatic2.finalbe.strategy.repository.StrategyStandardCodeRepository;
import com.sysmatic2.finalbe.strategy.repository.TradingTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static com.sysmatic2.finalbe.util.DtoEntityConversionUtils.*;

@Service
@RequiredArgsConstructor
public class StrategyService {

    private final StrategyRepository strategyRepo;
    private final InvestmentAssetClassesRepository iacRepo;
    private final TradingTypeRepository ttRepo;
    private final StrategyStandardCodeRepository standardCodeRepository;

    //1. 전략 생성
    @Transactional
    public void register(StrategyPayloadDto strategyPayloadDto) throws Exception {
        //TODO) 트레이더 판별

        //전략 엔티티 생성
        StrategyEntity strategyEntity = new StrategyEntity();

        //페이로드에서 가져온 매매유형 id로 해당 매매유형 엔티티가져오기
        TradingTypeEntity ttEntity = ttRepo.findById(strategyPayloadDto.getTradingTypeId())
                .orElseThrow(() -> new TradingTypeNotFoundException(strategyPayloadDto.getTradingTypeId()));
        //페이로드에서 가져온 투자자산분류 id로 해당 투자자산 분류 엔티티들 가져오기
        List<Integer> iacIds = strategyPayloadDto.getInvestmentAssetClassesIdList();
        List<InvestmentAssetClassesEntity> iacEntities = iacRepo.findAllById(iacIds);
        //페이로드에서 가져온 공통코드 id로 해당 공통코드 엔티티 가져오기
//        StandardCodeEntity tradingCycleCode = standardCodeRepository.findById(strategyPayloadDto.getTradingCycleCode())
//                .orElseThrow(() -> new NoSuchElementException());

        //payload내용을 엔티티에 담기
        strategyEntity.setStrategyTitle(strategyPayloadDto.getStrategyTitle());
        strategyEntity.setTradingTypeEntity(ttEntity);
        strategyEntity.setTradingCycleCode(strategyPayloadDto.getTradingCycleCode());
        strategyEntity.setMinInvestmentAmount(strategyPayloadDto.getMinInvestmentAmount());
        strategyEntity.setStrategyOverview(strategyPayloadDto.getStrategyOverview());
        strategyEntity.setIsPosted(strategyPayloadDto.getIsPosted());

        //전략 상태 공통코드
//        StandardCodeEntity strategyStatusCode = standardCodeRepository.findById("STRATEGY_STATUS_UNDER_MANAGEMENT")
//                .orElseThrow(()-> new NoSuchElementException("STANDARD_CODE_NOT_EXIST"));
        strategyEntity.setStrategyStatusCode("STRATEGY_STATUS_UNDER_MANAGEMENT");

        //TODO) 작성자 설정
        strategyEntity.setWriterId(101L);

        //save()
        strategyRepo.save(strategyEntity);

        //생성한 전략 객체아이디 가져오기
        //전략 객체가져오기
        //전략 - 투자자산 분류 관계 테이블 엔티티 생성
        //관계 테이블 엔티티에 값 넣기
        //save()


    }

    /**
     2. 사용자 전략 등록 폼에 필요한 정보를 제공하는 메서드
     * @return StrategyRegistrationDto 전략 등록에 필요한 DTO
     */
    @Transactional
    public StrategyRegistrationDto getStrategyRegistrationForm() {
        // TradingType 및 InvestmentAssetClass 데이터를 각각 DTO 리스트로 변환
        List<TradingTypeRegistrationDto> tradingTypeDtos = convertToTradingTypeDtos(ttRepo.findByIsActiveOrderByTradingTypeOrderAsc("Y"));
        List<InvestmentAssetClassesRegistrationDto> investmentAssetClassDtos = convertToInvestmentAssetClassDtos(iacRepo.findByIsActiveOrderByOrderAsc("Y"));

        // DTO 설정 및 반환
        StrategyRegistrationDto strategyRegistrationDto = new StrategyRegistrationDto();
        strategyRegistrationDto.setTradingTypeRegistrationDtoList(tradingTypeDtos);
        strategyRegistrationDto.setInvestmentAssetClassesRegistrationDtoList(investmentAssetClassDtos);

        return strategyRegistrationDto;
    }
}

//이미지 링크는 이미지 링크+{imageId}의 형태라서 imageId만 DB에 저장