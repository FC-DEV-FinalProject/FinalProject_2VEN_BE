package com.sysmatic2.finalbe.strategy.service;

import com.sysmatic2.finalbe.admin.service.TradingTypeService;
import com.sysmatic2.finalbe.exception.DuplicateTradingTypeOrderException;
import com.sysmatic2.finalbe.exception.TradingTypeNotFoundException;
import com.sysmatic2.finalbe.admin.dto.TradingTypeAdminRequestDto;
import com.sysmatic2.finalbe.admin.dto.TradingTypeAdminResponseDto;
import com.sysmatic2.finalbe.admin.entity.TradingTypeEntity;
import com.sysmatic2.finalbe.admin.repository.TradingTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TradingTypeServiceTest {

    @InjectMocks
    private TradingTypeService tradingTypeService;

    @Mock
    private TradingTypeRepository tradingTypeRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // 1. findAllTradingTypes 메서드 테스트
    @Test
    @DisplayName("전체 목록 조회 - isActive가 null일 때 모든 데이터를 반환")
    void findAllTradingTypes_shouldReturnAllWhenIsActiveIsNull() {
        // Given: 테스트를 위한 기본 데이터를 설정합니다.
        TradingTypeEntity entity1 = new TradingTypeEntity();
        entity1.setTradingTypeId(1);
        entity1.setTradingTypeOrder(1);
        entity1.setTradingTypeName("Type 1");
        entity1.setTradingTypeIcon("Icon 1");
        entity1.setIsActive("Y");

        TradingTypeEntity entity2 = new TradingTypeEntity();
        entity2.setTradingTypeId(2);
        entity2.setTradingTypeOrder(2);
        entity2.setTradingTypeName("Type 2");
        entity2.setTradingTypeIcon("Icon 2");
        entity2.setIsActive("Y");

        // TradingTypeEntity의 페이지 객체 생성 (두 개의 엔티티가 포함된 페이지)
        Page<TradingTypeEntity> tradingTypes = new PageImpl<>(List.of(entity1, entity2));
        // tradingTypeRepository.findAll() 호출 시, tradingTypes를 반환하도록 목 객체의 동작 설정
        when(tradingTypeRepository.findAll(any(Pageable.class))).thenReturn(tradingTypes);

        // When: 테스트 대상 메서드인 findAllTradingTypes()를 호출합니다.
        Map<String, Object> result = tradingTypeService.findAllTradingTypes(0, 10, null);

        // Then: 반환된 결과가 예상과 일치하는지 검증합니다.
        assertNotNull(result); // 결과가 null이 아닌지 확인
        assertEquals(2, ((List<TradingTypeAdminResponseDto>)result.get("data")).size()); // 반환된 페이지의 요소 수가 2인지 확인

        // tradingTypeRepository.findAll()이 한 번 호출되었는지 확인
        verify(tradingTypeRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("활성 상태가 'Y'일 때 활성화된 트레이딩 타입만 반환")
    void findAllTradingTypes_shouldReturnOnlyActiveWhenIsActiveIsY() {
        // Given: 테스트를 위한 활성화된 상태의 TradingTypeEntity 객체를 생성
        TradingTypeEntity entity1 = new TradingTypeEntity();
        entity1.setTradingTypeId(1);
        entity1.setTradingTypeOrder(1);
        entity1.setTradingTypeName("Active Type");
        entity1.setTradingTypeIcon("Icon A");
        entity1.setIsActive("Y");

        // 활성화된 엔티티를 포함하는 Page 객체를 생성하고, Repository 호출 시 반환하도록 설정
        Page<TradingTypeEntity> tradingTypes = new PageImpl<>(List.of(entity1));
        when(tradingTypeRepository.findByIsActive(eq("Y"), any(Pageable.class))).thenReturn(tradingTypes);

        // When: 테스트 대상 메서드를 호출하여 활성 상태가 'Y'인 데이터를 요청
        Map<String, Object> result = tradingTypeService.findAllTradingTypes(0, 10, "Y");

        // Then: 반환된 결과가 예상과 일치하는지 검증
        assertNotNull(result); // 결과가 null이 아닌지 확인
        assertEquals(1, ((List<TradingTypeAdminResponseDto>) result.get("data")).size()); // 반환된 데이터의 크기 검증
        assertEquals("Active Type", ((List<TradingTypeAdminResponseDto>) result.get("data")).get(0).getTradingTypeName()); // 이름 확인

        // findByIsActive("Y") 메서드가 한 번 호출되었는지 확인
        verify(tradingTypeRepository, times(1)).findByIsActive(eq("Y"), any(Pageable.class));
    }

    @Test
    @DisplayName("활성 상태가 'N'일 때 비활성화된 트레이딩 타입만 반환")
    void findAllTradingTypes_shouldReturnOnlyInactiveWhenIsActiveIsN() {
        // Given: 테스트를 위한 비활성화된 상태의 TradingTypeEntity 객체를 생성
        TradingTypeEntity entity1 = new TradingTypeEntity();
        entity1.setTradingTypeId(2);
        entity1.setTradingTypeOrder(2);
        entity1.setTradingTypeName("Inactive Type");
        entity1.setTradingTypeIcon("Icon B");
        entity1.setIsActive("N");

        // 비활성화된 엔티티를 포함하는 Page 객체를 생성하고, Repository 호출 시 반환하도록 설정
        Page<TradingTypeEntity> tradingTypes = new PageImpl<>(List.of(entity1));
        when(tradingTypeRepository.findByIsActive(eq("N"), any(Pageable.class))).thenReturn(tradingTypes);

        // When: 테스트 대상 메서드를 호출하여 비활성 상태가 'N'인 데이터를 요청
        Map<String, Object> result = tradingTypeService.findAllTradingTypes(0, 10, "N");

        // Then: 반환된 결과가 예상과 일치하는지 검증
        assertNotNull(result); // 결과가 null이 아닌지 확인
        assertEquals(1, ((List<TradingTypeAdminResponseDto>) result.get("data")).size()); // 반환된 데이터의 크기 검증
        assertEquals("Inactive Type", ((List<TradingTypeAdminResponseDto>) result.get("data")).get(0).getTradingTypeName()); // 이름 확인

        // findByIsActive("N") 메서드가 한 번 호출되었는지 확인
        verify(tradingTypeRepository, times(1)).findByIsActive(eq("N"), any(Pageable.class));
    }


    @Test
    @DisplayName("페이지에 데이터가 없는 경우 빈 리스트 반환")
    void findAllTradingTypes_shouldReturnEmptyListWhenNoDataAvailable() {
        // Given: 빈 페이지 객체를 생성하고, Repository 호출 시 반환하도록 설정
        Page<TradingTypeEntity> emptyPage = new PageImpl<>(List.of());
        when(tradingTypeRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        // When: 테스트 대상 메서드를 호출하여 데이터가 없는 경우를 시뮬레이션
        Map<String, Object> result = tradingTypeService.findAllTradingTypes(0, 10, null);

        // Then: 반환된 결과가 예상과 일치하는지 검증
        assertNotNull(result); // 결과가 null이 아닌지 확인
        assertTrue(((List<TradingTypeAdminResponseDto>) result.get("data")).isEmpty()); // 반환된 데이터가 비어 있는지 확인

        // findAll 메서드가 한 번 호출되었는지 확인
        verify(tradingTypeRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("데이터베이스 예외 발생 시 예외 처리 검증")
    void findAllTradingTypes_shouldThrowExceptionWhenDatabaseErrorOccurs() {
        // Given: Repository에서 findAll 메서드를 호출할 때 RuntimeException을 발생시키도록 설정
        when(tradingTypeRepository.findAll(any(Pageable.class))).thenThrow(new RuntimeException("Database error"));

        // When & Then: 예외가 발생하는지 확인하고, 발생한 예외의 메시지가 예상과 일치하는지 검증
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            tradingTypeService.findAllTradingTypes(0, 10, null);
        });
        assertEquals("Database error", exception.getMessage());

        // findAll 메서드가 한 번 호출되었는지 확인
        verify(tradingTypeRepository, times(1)).findAll(any(Pageable.class));
    }

    // 2. findTradingTypeById 테스트
    @Test
    @DisplayName("ID로 매매유형을 조회 - 정상 조회")
    void findTradingTypeById_shouldReturnTradingTypeWhenIdExists() {
        // Given: 존재하는 TradingTypeEntity 객체를 준비하고 리포지토리 호출 시 반환되도록 설정
        TradingTypeEntity entity = new TradingTypeEntity();
        entity.setTradingTypeId(1);
        entity.setTradingTypeName("Type 1");
        when(tradingTypeRepository.findById(1)).thenReturn(Optional.of(entity));

        // When: 테스트 대상 메서드를 호출하여 특정 ID로 매매유형을 조회
        TradingTypeAdminResponseDto result = tradingTypeService.findTradingTypeById(1);

        // Then: 반환된 결과가 예상과 일치하는지 확인
        assertNotNull(result); // 결과가 null이 아닌지 확인
        assertEquals("Type 1", result.getTradingTypeName()); // 반환된 매매유형 이름 검증
    }

    @Test
    @DisplayName("ID로 매매유형을 조회 - 존재하지 않는 ID로 조회 시 예외 발생")
    void findTradingTypeById_shouldThrowExceptionWhenIdNotFound() {
        // Given: ID가 999인 매매유형이 없도록 리포지토리 동작을 설정
        when(tradingTypeRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then: 존재하지 않는 ID로 조회 시 TradingTypeNotFoundException 예외 발생 확인
        assertThrows(TradingTypeNotFoundException.class, () -> tradingTypeService.findTradingTypeById(999));
    }

    // 3. createTradingType 테스트
    @Test
    @DisplayName("매매유형을 등록 - 순서가 null일 때 최대 순서 + 1로 설정")
    void createTradingType_shouldSetOrderWhenOrderIsNull() {
        // Given: 순서가 없는 TradingTypeAdminRequestDto 객체 준비 및 최대 순서 값을 2로 설정
        TradingTypeAdminRequestDto requestDto = new TradingTypeAdminRequestDto();
        requestDto.setTradingTypeName("New Type");
        when(tradingTypeRepository.findMaxTradingTypeOrder()).thenReturn(Optional.of(2));

        // When: 테스트 대상 메서드를 호출하여 매매유형을 등록
        tradingTypeService.createTradingType(requestDto);

        // Then: 최대 순서 + 1이 설정되었는지 확인하고 매매유형이 저장되었는지 검증
        assertEquals(3, requestDto.getTradingTypeOrder());
        verify(tradingTypeRepository, times(1)).save(any(TradingTypeEntity.class));
    }

    @Test
    @DisplayName("매매유형을 등록 - 중복된 순서로 등록 시 예외 발생")
    void createTradingType_shouldThrowExceptionWhenOrderIsDuplicate() {
        // Given: 중복된 순서 값이 포함된 TradingTypeAdminRequestDto 객체 생성
        TradingTypeAdminRequestDto requestDto = new TradingTypeAdminRequestDto();
        requestDto.setTradingTypeOrder(1);
        TradingTypeEntity tradingTypeEntity = new TradingTypeEntity();
        tradingTypeEntity.setTradingTypeOrder(1);
        when(tradingTypeRepository.findByTradingTypeOrder(1)).thenReturn(Optional.of(tradingTypeEntity));

        // When & Then: 중복된 순서로 등록 시 DuplicateTradingTypeOrderException 예외 발생 확인
        assertThrows(DuplicateTradingTypeOrderException.class, () -> tradingTypeService.createTradingType(requestDto));
    }

    @Test
    @DisplayName("매매유형을 등록 - 정상적으로 등록")
    void createTradingType_shouldSaveWhenOrderIsValid() {
        // Given: 유효한 순서 값이 포함된 TradingTypeAdminRequestDto 객체 준비
        TradingTypeAdminRequestDto requestDto = new TradingTypeAdminRequestDto();
        requestDto.setTradingTypeOrder(1);
        requestDto.setTradingTypeName("Valid Type");

        // When: 테스트 대상 메서드를 호출하여 매매유형을 정상적으로 등록
        tradingTypeService.createTradingType(requestDto);

        // Then: 매매유형이 저장되었는지 검증
        verify(tradingTypeRepository, times(1)).save(any(TradingTypeEntity.class));
    }

    // 4. deleteTradingType 테스트
    @Test
    @DisplayName("매매유형을 삭제 - 정상 삭제")
    void deleteTradingType_shouldDeleteWhenIdExists() {
        // Given: 삭제할 TradingTypeEntity 객체를 준비하고 리포지토리 호출 시 반환되도록 설정
        TradingTypeEntity entity = new TradingTypeEntity();
        when(tradingTypeRepository.findById(1)).thenReturn(Optional.of(entity));

        // When: 테스트 대상 메서드를 호출하여 매매유형을 삭제
        tradingTypeService.deleteTradingType(1);

        // Then: delete 메서드가 한 번 호출되었는지 확인
        verify(tradingTypeRepository, times(1)).delete(entity);
    }

    @Test
    @DisplayName("매매유형을 삭제 - 존재하지 않는 ID로 삭제 시 예외 발생")
    void deleteTradingType_shouldThrowExceptionWhenIdNotFound() {
        // Given: ID가 999인 매매유형이 없도록 설정
        when(tradingTypeRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then: 존재하지 않는 ID로 삭제 시 TradingTypeNotFoundException 예외 발생 확인
        assertThrows(TradingTypeNotFoundException.class, () -> tradingTypeService.deleteTradingType(999));
    }

    // 5. softDeleteTradingType 테스트
    @Test
    @DisplayName("매매유형을 논리적 삭제 - 정상적으로 논리적 삭제")
    void softDeleteTradingType_shouldSoftDeleteWhenIdExists() {
        // Given: 논리적 삭제할 TradingTypeEntity 객체를 준비하고 리포지토리 호출 시 반환되도록 설정
        TradingTypeEntity entity = new TradingTypeEntity();
        when(tradingTypeRepository.findById(1)).thenReturn(Optional.of(entity));

        // When: 테스트 대상 메서드를 호출하여 논리적 삭제 수행
        tradingTypeService.softDeleteTradingType(1);

        // Then: isActive 필드가 'N'으로 설정되었는지 확인하고, 저장 메서드가 호출되었는지 검증
        assertEquals("N", entity.getIsActive());
        verify(tradingTypeRepository, times(1)).save(entity);
    }

    @Test
    @DisplayName("매매유형을 논리적 삭제 - 존재하지 않는 ID로 삭제 시 예외 발생")
    void softDeleteTradingType_shouldThrowExceptionWhenIdNotFound() {
        // Given: ID가 999인 매매유형이 없도록 설정
        when(tradingTypeRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then: 존재하지 않는 ID로 삭제 시 TradingTypeNotFoundException 예외 발생 확인
        assertThrows(TradingTypeNotFoundException.class, () -> tradingTypeService.softDeleteTradingType(999));
    }

    // 6. updateTradingType 테스트
    @Test
    @DisplayName("매매유형을 수정 - 정상적으로 업데이트")
    void updateTradingType_shouldUpdateWhenIdExistsAndOrderIsValid() {
        // Given: 기존 매매유형과 업데이트할 요청 DTO를 생성
        TradingTypeEntity entity = new TradingTypeEntity();
        entity.setTradingTypeOrder(1);
        TradingTypeAdminRequestDto requestDto = new TradingTypeAdminRequestDto();
        requestDto.setTradingTypeOrder(2);
        requestDto.setTradingTypeName("Updated Type");

        when(tradingTypeRepository.findById(1)).thenReturn(Optional.of(entity));
        when(tradingTypeRepository.findByTradingTypeOrder(2)).thenReturn(Optional.empty());

        // When: 테스트 대상 메서드를 호출하여 매매유형을 업데이트
        tradingTypeService.updateTradingType(1, requestDto);

        // Then: 필드 값이 업데이트되었는지 확인하고, save 메서드가 호출되었는지 검증
        assertEquals(2, entity.getTradingTypeOrder());
        assertEquals("Updated Type", entity.getTradingTypeName());
        verify(tradingTypeRepository, times(1)).save(entity);
    }

    @Test
    @DisplayName("매매유형을 수정 - 존재하지 않는 ID로 수정 시 예외 발생")
    void updateTradingType_shouldThrowExceptionWhenIdNotFound() {
        // Given: ID가 999인 매매유형이 없도록 설정
        TradingTypeAdminRequestDto requestDto = new TradingTypeAdminRequestDto();
        when(tradingTypeRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then: 존재하지 않는 ID로 수정 시 TradingTypeNotFoundException 예외 발생 확인
        assertThrows(TradingTypeNotFoundException.class, () -> tradingTypeService.updateTradingType(999, requestDto));
    }

    @Test
    @DisplayName("매매유형을 수정 - 중복된 순서로 수정 시 예외 발생")
    void updateTradingType_shouldThrowExceptionWhenOrderIsDuplicate() {
        // Given: 기존 매매유형과 중복된 순서 값을 가진 요청 DTO를 생성
        TradingTypeEntity entity = new TradingTypeEntity();
        entity.setTradingTypeOrder(1);
        TradingTypeAdminRequestDto requestDto = new TradingTypeAdminRequestDto();
        requestDto.setTradingTypeOrder(2);

        when(tradingTypeRepository.findById(1)).thenReturn(Optional.of(entity));
        when(tradingTypeRepository.findByTradingTypeOrder(2)).thenReturn(Optional.of(new TradingTypeEntity()));

        // When & Then: 중복된 순서로 수정 시 DuplicateTradingTypeOrderException 예외 발생 확인
        assertThrows(DuplicateTradingTypeOrderException.class, () -> tradingTypeService.updateTradingType(1, requestDto));
    }
}