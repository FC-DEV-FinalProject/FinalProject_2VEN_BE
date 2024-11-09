package com.sysmatic2.finalbe.strategy.repository;

import com.sysmatic2.finalbe.strategy.entity.TradingTypeEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class TradingTypeEntityRepositoryTest {

    @Autowired
    TradingTypeRepository tradingTypeRepository;

    public void createTradingTypeList() {
        for (int i = 1; i <= 10; i++) {
            TradingTypeEntity tradingTypeEntity = new TradingTypeEntity();
            tradingTypeEntity.setTradingTypeOrder(i);
            tradingTypeEntity.setTradingTypeName("test Trading" + i);
            tradingTypeEntity.setTradingTypeIcon("test" + i + ".png");

            // i가 홀수일 때 "Y", 짝수일 때 "N"을 설정
            tradingTypeEntity.setIsActive(i % 2 == 0 ? "N" : "Y");

            tradingTypeRepository.save(tradingTypeEntity);
        }
    }

    @BeforeEach
    public void resetDatabase() {
        tradingTypeRepository.deleteAll();
    }

    @Test
    @DisplayName("매매유형 등록 테스트")
    public void registerTradingTypeTest() {
        // 새로운 tradingType 엔티티 생성
        TradingTypeEntity tradingTypeEntity = new TradingTypeEntity();
        tradingTypeEntity.setTradingTypeOrder(1);
        tradingTypeEntity.setTradingTypeName("Day Trading");
        tradingTypeEntity.setTradingTypeIcon("icon_day.png");
        tradingTypeEntity.setTradingTypeDescription("Short-term trades within the day.");
        tradingTypeEntity.setIsActive("Y");

        // 엔티티 저장
        TradingTypeEntity savedTradingTypeEntity = tradingTypeRepository.save(tradingTypeEntity);
        System.out.println(savedTradingTypeEntity.toString());

        // 검증
        /*
            등록 전 엔티티와 등록 후 엔티티 비교
         */
        Assertions.assertEquals(tradingTypeEntity.getTradingTypeId(), savedTradingTypeEntity.getTradingTypeId());
        Assertions.assertEquals(tradingTypeEntity.getTradingTypeOrder(), savedTradingTypeEntity.getTradingTypeOrder());
        Assertions.assertEquals(tradingTypeEntity.getTradingTypeName(), savedTradingTypeEntity.getTradingTypeName());
        Assertions.assertEquals(tradingTypeEntity.getTradingTypeIcon(), savedTradingTypeEntity.getTradingTypeIcon());
        Assertions.assertEquals(tradingTypeEntity.getTradingTypeDescription(), savedTradingTypeEntity.getTradingTypeDescription());
        Assertions.assertEquals(tradingTypeEntity.getIsActive(), savedTradingTypeEntity.getIsActive());
    }

    @Test
    @DisplayName("unique 중복 검증")
    public void uniqueDuplicateValidation() {
        // 새로운 tradingType 엔티티 생성
        TradingTypeEntity tradingTypeEntity = new TradingTypeEntity();
        tradingTypeEntity.setTradingTypeOrder(1);
        tradingTypeEntity.setTradingTypeName("Day Trading");
        tradingTypeEntity.setTradingTypeIcon("icon_day.png");
        tradingTypeEntity.setTradingTypeDescription("Short-term trades within the day.");
        tradingTypeEntity.setIsActive("Y");

        // 첫 번째 엔티티 저장
        TradingTypeEntity savedTradingTypeEntity = tradingTypeRepository.save(tradingTypeEntity);

        // 동일한 order를 가진 두 번째 엔티티 생성
        TradingTypeEntity duplicateTradingTypeEntity = new TradingTypeEntity();
        duplicateTradingTypeEntity.setTradingTypeOrder(1); // 중복 값 설정
        duplicateTradingTypeEntity.setTradingTypeName("Swing Trading");
        duplicateTradingTypeEntity.setTradingTypeIcon("icon_swing.png");
        duplicateTradingTypeEntity.setTradingTypeDescription("Medium-term trades over several days.");
        duplicateTradingTypeEntity.setIsActive("N");

        // 중복 저장 시 예외 발생 여부 검증
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            tradingTypeRepository.save(duplicateTradingTypeEntity);
        });
    }

    @Test
    @DisplayName("매매유형 조회 테스트")
    public void retrieveTradingTypeTest() {
        // 새로운 tradingType 엔티티 생성
        TradingTypeEntity tradingTypeEntity = new TradingTypeEntity();
        tradingTypeEntity.setTradingTypeOrder(1);
        tradingTypeEntity.setTradingTypeName("Day Trading");
        tradingTypeEntity.setTradingTypeIcon("icon_day.png");
        tradingTypeEntity.setTradingTypeDescription("Short-term trades within the day.");
        tradingTypeEntity.setIsActive("Y");

        // 엔티티 저장
        TradingTypeEntity savedTradingTypeEntity = tradingTypeRepository.save(tradingTypeEntity);
        // 저장된 매매유형명으로 검색
        TradingTypeEntity retrievedTradingTypeEntity = tradingTypeRepository.findByTradingTypeName(savedTradingTypeEntity.getTradingTypeName());

        // 조회된 매매유형이 NULL이 아님
        assertNotNull(retrievedTradingTypeEntity);
        // 저장한 매매유형명과 조회된 매매유형명이 같아야함
        assertEquals(savedTradingTypeEntity.getTradingTypeName(), retrievedTradingTypeEntity.getTradingTypeName());
    }

    @Test
    @DisplayName("매매유형 수정 테스트")
    public void updateTradingTypeTest() {
        // 새로운 tradingType 엔티티 생성
        TradingTypeEntity tradingTypeEntity = new TradingTypeEntity();
        tradingTypeEntity.setTradingTypeOrder(1);
        tradingTypeEntity.setTradingTypeName("Day Trading");
        tradingTypeEntity.setTradingTypeIcon("icon_day.png");
        tradingTypeEntity.setTradingTypeDescription("Short-term trades within the day.");
        tradingTypeEntity.setIsActive("Y");

        // 엔티티 저장
        TradingTypeEntity savedTradingTypeEntity = tradingTypeRepository.save(tradingTypeEntity);
        // 저장된 엔티티 수정
        savedTradingTypeEntity.setTradingTypeName("Modified Scalping");
        // 수정된 엔티티 저장
        TradingTypeEntity updatedTradingTypeEntity = tradingTypeRepository.save(savedTradingTypeEntity);

        // 수정된 엔티티와 저장된 엔티티 비교
        assertEquals(savedTradingTypeEntity.getTradingTypeName(), updatedTradingTypeEntity.getTradingTypeName());
    }

    @Test
    @DisplayName("매매유형 삭제 테스트")
    public void deleteTradingTypeTest() {
        // 새로운 tradingType 엔티티 생성
        TradingTypeEntity tradingTypeEntity = new TradingTypeEntity();
        tradingTypeEntity.setTradingTypeOrder(1);
        tradingTypeEntity.setTradingTypeName("Day Trading");
        tradingTypeEntity.setTradingTypeIcon("icon_day.png");
        tradingTypeEntity.setTradingTypeDescription("Short-term trades within the day.");
        tradingTypeEntity.setIsActive("Y");

        // 엔티티 저장
        TradingTypeEntity savedTradingTypeEntity = tradingTypeRepository.save(tradingTypeEntity);
        // 엔티티 삭제
        tradingTypeRepository.delete(savedTradingTypeEntity);

        // 잘 삭제됐는지 조회
        Optional<TradingTypeEntity> deletedTradingType = tradingTypeRepository.findById(savedTradingTypeEntity.getTradingTypeId());
        // 비어있으면 삭제 성공
        assertTrue(deletedTradingType.isEmpty());
    }

    @Test
    @DisplayName("isActive 필수값 검증 테스트")
    public void isActiveNotNullValidation() {
        // 새로운 tradingType 엔티티 생성
        TradingTypeEntity tradingTypeEntity = new TradingTypeEntity();
        tradingTypeEntity.setTradingTypeOrder(2);
        tradingTypeEntity.setTradingTypeName("Swing Trading");
        tradingTypeEntity.setTradingTypeIcon("icon_swing.png");
        tradingTypeEntity.setTradingTypeDescription("Medium-term trades over several days.");

        // isActive 값이 설정되지 않음
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            tradingTypeRepository.save(tradingTypeEntity);
        });
    }

    @Test
    @DisplayName("tradingTypeIcon 필수값 검증 테스트")
    public void tradingTypeIconNotNullValidation() {
        TradingTypeEntity tradingTypeEntity = new TradingTypeEntity();
        tradingTypeEntity.setTradingTypeOrder(3);
        tradingTypeEntity.setTradingTypeName("Position Trading");
        tradingTypeEntity.setIsActive("Y");

        // tradingTypeIcon 값이 설정되지 않음
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            tradingTypeRepository.save(tradingTypeEntity);
        });
    }

    @Test
    @DisplayName("tradingTypeName 고유성 검증 테스트")
    public void duplicateTradingTypeNameTest() {
        // 첫 번째 엔티티 저장
        TradingTypeEntity tradingTypeEntity1 = new TradingTypeEntity();
        tradingTypeEntity1.setTradingTypeOrder(4);
        tradingTypeEntity1.setTradingTypeName("Day Trading");
        tradingTypeEntity1.setTradingTypeIcon("icon_day.png");
        tradingTypeEntity1.setIsActive("Y");
        tradingTypeRepository.save(tradingTypeEntity1);

        // 두 번째 엔티티 저장 - tradingTypeName 중복값 설정
        TradingTypeEntity tradingTypeEntity2 = new TradingTypeEntity();
        tradingTypeEntity2.setTradingTypeOrder(5);
        tradingTypeEntity2.setTradingTypeName("Day Trading"); // 중복된 이름
        tradingTypeEntity2.setTradingTypeIcon("icon_day_alt.png");
        tradingTypeEntity2.setIsActive("N");

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            tradingTypeRepository.save(tradingTypeEntity2);
        });
    }

    @Test
    @DisplayName("tradingTypeOrder 고유성 검증 테스트")
    public void uniqueTradingTypeOrderTest() {
        // 첫 번째 엔티티 저장
        TradingTypeEntity tradingTypeEntity1 = new TradingTypeEntity();
        tradingTypeEntity1.setTradingTypeOrder(10);
        tradingTypeEntity1.setTradingTypeName("Swing Trading");
        tradingTypeEntity1.setTradingTypeIcon("icon_swing.png");
        tradingTypeEntity1.setIsActive("Y");
        tradingTypeRepository.save(tradingTypeEntity1);

        // 두 번째 엔티티 저장 - tradingTypeOrder 중복값 설정
        TradingTypeEntity tradingTypeEntity2 = new TradingTypeEntity();
        tradingTypeEntity2.setTradingTypeOrder(10); // 중복된 order
        tradingTypeEntity2.setTradingTypeName("Position Trading");
        tradingTypeEntity2.setTradingTypeIcon("icon_position.png");
        tradingTypeEntity2.setIsActive("N");

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            tradingTypeRepository.save(tradingTypeEntity2);
        });
    }

    @Test
    @DisplayName("tradingTypeDescription null 등록 테스트")
    public void optionalTradingTypeDescriptionTest() {
        // tradingTypeDescription에 null주고 엔티티 등록
        TradingTypeEntity tradingTypeEntity = new TradingTypeEntity();
        tradingTypeEntity.setTradingTypeOrder(6);
        tradingTypeEntity.setTradingTypeName("Scalping");
        tradingTypeEntity.setTradingTypeIcon("icon_scalping.png");
        tradingTypeEntity.setIsActive("Y");

        TradingTypeEntity savedTradingTypeEntity = tradingTypeRepository.save(tradingTypeEntity);
        assertNull(savedTradingTypeEntity.getTradingTypeDescription());
    }

    @Test
    @DisplayName("매매유형 여러개 등록 후 조회 개수 확인 테스트")
    public void multipleEntitiesCountTest() {
        // 다중 엔티티 등록
        createTradingTypeList();

        // 모든 엔티티 조회 후 개수 확인
        List<TradingTypeEntity> tradingTypeEntities = tradingTypeRepository.findAll();
        assertEquals(10, tradingTypeEntities.size());
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회 시도 테스트")
    public void findByIdNonExistentTest() {
        Optional<TradingTypeEntity> tradingType = tradingTypeRepository.findById(-1);
        assertTrue(tradingType.isEmpty());
    }

    @Test
    @DisplayName("isActive 상태별 페이지네이션 조회 테스트")
    public void findByIsActiveWithPaginationTest() {
        // 여러 개의 엔티티 추가
        createTradingTypeList();

        // isActive가 "Y"인 엔티티를 페이지네이션으로 조회 (2개씩)
        Pageable pageable = PageRequest.of(0, 2); // 첫 번째 페이지, 페이지 크기 2
        Page<TradingTypeEntity> activeTypesPage = tradingTypeRepository.findByIsActive("Y", pageable);

        // 조회한 페이지의 개수와 각 요소 확인
        assertEquals(2, activeTypesPage.getSize()); // 페이지 크기가 2인지 확인
        assertEquals(3, activeTypesPage.getTotalPages()); // 총 페이지가 5개인지 확인 (Y가 5개이므로 페이지 크기 2일 때 3페이지)
        assertEquals(5, activeTypesPage.getTotalElements()); // "Y" 상태의 엔티티가 총 5개인지 확인
        assertEquals("test Trading1", activeTypesPage.getContent().get(0).getTradingTypeName());
        assertEquals("test Trading3", activeTypesPage.getContent().get(1).getTradingTypeName());

        // 첫 페이지 여부 및 다음 페이지 존재 여부 확인
        assertTrue(activeTypesPage.isFirst()); // 첫 번째 페이지인지 확인
        assertFalse(activeTypesPage.isLast()); // 마지막 페이지가 아님을 확인
        assertTrue(activeTypesPage.hasNext()); // 다음 페이지가 존재하는지 확인

        // 두 번째 페이지 조회
        Pageable secondPage = PageRequest.of(1, 2);
        Page<TradingTypeEntity> secondActiveTypesPage = tradingTypeRepository.findByIsActive("Y", secondPage);

        assertEquals(2, secondActiveTypesPage.getSize()); // 페이지 크기가 2인지 확인
        assertEquals("test Trading5", secondActiveTypesPage.getContent().get(0).getTradingTypeName());
        assertEquals("test Trading7", secondActiveTypesPage.getContent().get(1).getTradingTypeName());

        // isActive가 "N"인 엔티티를 페이지네이션으로 조회 (2개씩)
        Page<TradingTypeEntity> inactiveTypesPage = tradingTypeRepository.findByIsActive("N", pageable);

        // 조회한 페이지의 개수와 각 요소 확인
        assertEquals(2, inactiveTypesPage.getSize()); // 페이지 크기가 2인지 확인
        assertEquals(3, inactiveTypesPage.getTotalPages()); // 총 페이지가 5개인지 확인 (N이 5개이므로 페이지 크기 2일 때 3페이지)
        assertEquals(5, inactiveTypesPage.getTotalElements()); // "N" 상태의 엔티티가 총 5개인지 확인
        assertEquals("test Trading2", inactiveTypesPage.getContent().get(0).getTradingTypeName());
        assertEquals("test Trading4", inactiveTypesPage.getContent().get(1).getTradingTypeName());

        // 두 번째 페이지의 첫 페이지 및 마지막 페이지 여부 확인
        assertFalse(secondActiveTypesPage.isFirst()); // 첫 번째 페이지가 아님을 확인
        assertFalse(secondActiveTypesPage.isLast()); // 마지막 페이지가 아님을 확인
        assertTrue(secondActiveTypesPage.hasNext()); // 다음 페이지가 존재하는지 확인

        // 세 번째 페이지 조회 (마지막 페이지, 한 개의 엔티티만 포함)
        Pageable thirdPage = PageRequest.of(2, 2);
        Page<TradingTypeEntity> thirdActiveTypesPage = tradingTypeRepository.findByIsActive("Y", thirdPage);

        assertEquals(1, thirdActiveTypesPage.getContent().size()); // 마지막 페이지는 1개의 엔티티만 포함
        assertEquals("test Trading9", thirdActiveTypesPage.getContent().get(0).getTradingTypeName());

        // 마지막 페이지 여부 확인
        assertFalse(thirdActiveTypesPage.isFirst()); // 첫 번째 페이지가 아님을 확인
        assertTrue(thirdActiveTypesPage.isLast()); // 마지막 페이지임을 확인
        assertFalse(thirdActiveTypesPage.hasNext()); // 다음 페이지가 없음을 확인
    }

    @Test
    @DisplayName("시스템 컬럼 자동 업데이트 테스트")
    public void auditingFieldsAutoUpdateTest() {
        TradingTypeEntity tradingTypeEntity = new TradingTypeEntity();
        tradingTypeEntity.setTradingTypeOrder(9);
        tradingTypeEntity.setTradingTypeName("Long-term Trading");
        tradingTypeEntity.setTradingTypeIcon("icon_longterm.png");
        tradingTypeEntity.setIsActive("Y");

        // 엔티티 저장
        TradingTypeEntity savedTradingTypeEntity = tradingTypeRepository.save(tradingTypeEntity);

        // createdBy와 createdAt 필드가 자동으로 생성되었는지 검증
        assertNotNull(savedTradingTypeEntity.getCreatedBy());
        assertNotNull(savedTradingTypeEntity.getCreatedAt());

        // 엔티티 업데이트
        savedTradingTypeEntity.setTradingTypeName("Updated Long-term Trading");
        TradingTypeEntity updatedTradingTypeEntity = tradingTypeRepository.save(savedTradingTypeEntity);

        // modifiedBy와 updatedAt 필드가 자동으로 업데이트되었는지 검증
        assertNotNull(updatedTradingTypeEntity.getModifiedBy());
        assertNotNull(updatedTradingTypeEntity.getModifiedAt());
    }
}