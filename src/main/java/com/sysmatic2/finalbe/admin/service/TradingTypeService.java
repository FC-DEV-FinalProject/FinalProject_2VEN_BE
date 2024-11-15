package com.sysmatic2.finalbe.admin.service;

import com.sysmatic2.finalbe.exception.DuplicateTradingTypeOrderException;
import com.sysmatic2.finalbe.exception.TradingTypeNotFoundException;
import com.sysmatic2.finalbe.admin.dto.TradingTypeAdminRequestDto;
import com.sysmatic2.finalbe.admin.dto.TradingTypeAdminResponseDto;
import com.sysmatic2.finalbe.admin.entity.TradingTypeEntity;
import com.sysmatic2.finalbe.admin.repository.TradingTypeRepository;
import com.sysmatic2.finalbe.util.DtoEntityConversionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.sysmatic2.finalbe.util.CreatePageResponse.createPageResponse;
import static com.sysmatic2.finalbe.util.DtoEntityConversionUtils.toDto;
import static com.sysmatic2.finalbe.util.DtoEntityConversionUtils.toEntity;

// 관리자 페이지 - 매매유형 관리
@Service
@RequiredArgsConstructor
public class TradingTypeService {
    private final TradingTypeRepository tradingTypeRepository;

    @Transactional(readOnly = true)
    // 1. 매매유형 전체 목록을 가져오는 메서드
    public Map<String, Object> findAllTradingTypes(int page, int pageSize, String isActive) {
        // TODO 현재 사용자 ID를 가져와 관리자인지 식별

        // 페이징 관련 객체 설정
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("tradingTypeOrder").ascending());

        // isActive값에 따라 조회할 목록 필터링
        Page<TradingTypeEntity> tradingTypeList;
        if (isActive == null) {
            tradingTypeList = tradingTypeRepository.findAll(pageable); // 모든 데이터 조회
        } else {
            tradingTypeList = tradingTypeRepository.findByIsActive(isActive, pageable); // 활성 상태에 따른 조회
        }
        // 페이지 객체 리스트 타입 변경
        Page<TradingTypeAdminResponseDto> pageDtoList = tradingTypeList.map(DtoEntityConversionUtils::toDto);

        // map으로 필요한 정보 추출하여 반환
        return createPageResponse(pageDtoList);
    }

    @Transactional(readOnly = true)
    // 1-1. 매매유형 분류 상세 조회 메서드
    public TradingTypeAdminResponseDto findTradingTypeById(Integer id) {
        // TODO 현재 사용자 ID를 가져와 관리자인지 식별
        // id 값으로 TradingType 조회
        TradingTypeEntity tradingTypeEntity = tradingTypeRepository.findById(id)
                .orElseThrow(() -> new TradingTypeNotFoundException(id));

        // 조회된 엔티티를 DTO로 변환하여 반환
        return toDto(tradingTypeEntity);
    }

    // 2. 매매유형을 등록하는 메서드
    @Transactional
    public void createTradingType(TradingTypeAdminRequestDto tradingTypeAdminRequestDto) {
        // TODO 현재 사용자 ID를 가져와 관리자인지 식별
        // 요청된 순서 값 확인
        Integer order = tradingTypeAdminRequestDto.getTradingTypeOrder();

        if (order == null) {
            // trading_type_order가 null인 경우 최대 순서 값 + 1 설정
            Integer maxOrder = tradingTypeRepository.findMaxTradingTypeOrder()
                    .orElse(0) + 1; // 순서가 없으면 기본값 0 설정 후 +1
            // 매매유형순서 DTO에 설정
            tradingTypeAdminRequestDto.setTradingTypeOrder(maxOrder);
        } else if (tradingTypeRepository.findByTradingTypeOrder(order).isPresent()) { // trading_type_order 중복 확인
            throw new DuplicateTradingTypeOrderException(order);
        }

        // 요청 DTO를 엔티티로 변환하여 매매유형 등록
        TradingTypeEntity tradingTypeEntity = toEntity(tradingTypeAdminRequestDto);
        tradingTypeRepository.save(tradingTypeEntity);
    }

    // 3. 매매유형 삭제 메서드
    // id값 받으면 해당 매매유형 삭제
    @Transactional
    public void deleteTradingType(Integer id) {
        // TODO 현재 사용자 ID를 가져와 관리자인지 식별
        // id 값으로 TradingType 조회
        TradingTypeEntity tradingTypeEntity = tradingTypeRepository.findById(id)
                .orElseThrow(() -> new TradingTypeNotFoundException(id));

        // 해당 매매유형 삭제
        tradingTypeRepository.delete(tradingTypeEntity);
    }

    // 3-1. 매매유형 논리적 삭제 메서드
    // 해당 id값의 매매유형 사용유무 "N"으로 변경
    @Transactional
    public void softDeleteTradingType(Integer id) {
        // TODO 현재 사용자 ID를 가져와 관리자인지 식별
        // id 값으로 TradingType 조회
        TradingTypeEntity tradingTypeEntity = tradingTypeRepository.findById(id)
                .orElseThrow(() -> new TradingTypeNotFoundException(id));

        tradingTypeEntity.setIsActive("N"); // 논리적 삭제를 위해 isActive를 'N'으로 설정
        tradingTypeRepository.save(tradingTypeEntity); // 변경 사항 저장
    }

    // 4. 매매유형 수정하는 메서드
    @Transactional
    public void updateTradingType(Integer id, TradingTypeAdminRequestDto tradingTypeAdminRequestDto) {
        // TODO 현재 사용자 ID를 가져와 관리자인지 식별
        // id 값으로 TradingType 조회
        TradingTypeEntity existingTradingTypeEntity = tradingTypeRepository.findById(id)
                .orElseThrow(() -> new TradingTypeNotFoundException(id));

        // 중복된 trading_type_order 값 체크 (단, 수정 대상이 아닌 다른 엔티티와 중복되는지 확인)
        // requestOrder이 비어 있지 않고 기존의 순서랑 수정된 순서가 같지 않다면
        // 또한 다른 매매유형이 쓰고 있는 순서가 아니라면
        Integer requestedOrder = tradingTypeAdminRequestDto.getTradingTypeOrder();
        if (requestedOrder != null && !requestedOrder.equals(existingTradingTypeEntity.getTradingTypeOrder())) {
            tradingTypeRepository.findByTradingTypeOrder(requestedOrder)
                    .ifPresent(order -> {
                        throw new DuplicateTradingTypeOrderException(requestedOrder);
                    });
        }

        // 업데이트할 필드 설정
        existingTradingTypeEntity.setTradingTypeOrder(tradingTypeAdminRequestDto.getTradingTypeOrder());
        existingTradingTypeEntity.setTradingTypeName(tradingTypeAdminRequestDto.getTradingTypeName());
        existingTradingTypeEntity.setTradingTypeIcon(tradingTypeAdminRequestDto.getTradingTypeIcon());
        existingTradingTypeEntity.setIsActive(tradingTypeAdminRequestDto.getIsActive());

        // 수정한 엔티티 저장
        tradingTypeRepository.save(existingTradingTypeEntity);
    }
}
