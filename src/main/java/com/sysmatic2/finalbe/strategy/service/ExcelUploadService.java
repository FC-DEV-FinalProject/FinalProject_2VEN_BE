package com.sysmatic2.finalbe.strategy.service;

import com.sysmatic2.finalbe.strategy.common.LocalDateDeserializer;
import com.sysmatic2.finalbe.strategy.dto.DailyStatisticsReqDto;
import com.sysmatic2.finalbe.exception.ExcelValidationException;
import com.sysmatic2.finalbe.strategy.entity.DailyStatisticsEntity;
import com.sysmatic2.finalbe.strategy.entity.StrategyEntity;
import com.sysmatic2.finalbe.strategy.repository.DailyStatisticsRepository;
import com.sysmatic2.finalbe.strategy.repository.StrategyRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExcelUploadService {

  private final Validator validator;
  private final StrategyRepository strategyRepository;
  private final DailyStatisticsService dailyStatisticsService;
  private final DailyStatisticsRepository dailyStatisticsRepository;
  private static final int MAX_ROWS = 2000;
  private static final int EXPECTED_COLUMNS = 3;
  private final LocalDateDeserializer localDateDeserializer = new LocalDateDeserializer(); // LocalDateDeserializer 객체 생성

  /**
   * 엑셀 파일의 데이터를 추출 및 저장
   *
   * @param file       업로드된 엑셀 파일
   * @param strategyId 전략 ID
   * @return 저장된 DailyStatisticsEntity 리스트
   */
  @Transactional
  public List<DailyStatisticsEntity> extractAndSaveData(MultipartFile file, Long strategyId, String memberId, Boolean isTrader) {
    if (strategyId == null) {
      throw new IllegalArgumentException("Strategy ID는 null일 수 없습니다.");
    }

    //전략 정보
    StrategyEntity strategyEntity = strategyRepository.findById(strategyId).orElseThrow(
            () -> new NoSuchElementException("해당 전략의 정보가 없습니다."));

    //트레이더인 경우 작성자 판별
    if(isTrader && !strategyEntity.getWriterId().equals(memberId)){
      throw new AccessDeniedException("데이터 작성 권한이 없습니다.");
    }

    List<DailyStatisticsReqDto> dataList = extractAndValidateData(file);

    List<DailyStatisticsEntity> savedEntities = new ArrayList<>();
    for (DailyStatisticsReqDto dto : dataList) {
      dailyStatisticsService.registerDailyStatistics(strategyId, dto);
      DailyStatisticsEntity entity = dailyStatisticsRepository.findByStrategyIdAndDate(strategyId, dto.getDate())
              .orElseThrow(() -> new IllegalStateException("Failed to save DailyStatisticsEntity for date " + dto.getDate()));
      savedEntities.add(entity);
    }

    return savedEntities;
  }

  /**
   * 엑셀 파일의 데이터를 추출 및 유효성 검증
   *
   * @param file 업로드된 엑셀 파일
   * @return 추출된 DailyStatisticsReqDto 리스트
   */
  public List<DailyStatisticsReqDto> extractAndValidateData(MultipartFile file) {
    List<DailyStatisticsReqDto> excelData = new ArrayList<>();
    Map<LocalDate, Integer> dateMap = new HashMap<>();

    try (InputStream inputStream = file.getInputStream();
         Workbook workbook = WorkbookFactory.create(inputStream)) {

      if (workbook.getNumberOfSheets() > 1) {
        throw new ExcelValidationException("엑셀 파일에 여러 시트가 포함되어 있습니다. 첫 번째 시트만 허용됩니다.");
      }

      Sheet sheet = workbook.getSheetAt(0);

      int rowNumber = 0;
      for (Row row : sheet) {
        rowNumber++;

        if (rowNumber > MAX_ROWS + 1) {
          throw new ExcelValidationException("엑셀 파일의 행 수가 2000개를 초과했습니다.");
        }

        if (rowNumber == 1) {
          continue;
        }

        if (row.getPhysicalNumberOfCells() != EXPECTED_COLUMNS) {
          throw new ExcelValidationException("행 " + rowNumber + "의 칼럼 수가 정확히 " + EXPECTED_COLUMNS + "개가 아닙니다.");
        }

        DailyStatisticsReqDto dto = parseRowToDto(row, rowNumber);

        if (dateMap.containsKey(dto.getDate())) {
          int firstRowNumber = dateMap.get(dto.getDate());
          throw new ExcelValidationException("중복된 날짜가 발견되었습니다: " + dto.getDate() + " (행 " + firstRowNumber + ", " + rowNumber + ")");
        }
        dateMap.put(dto.getDate(), rowNumber);

        validateDto(dto, rowNumber);

        excelData.add(dto);
      }

      if (excelData.isEmpty()) {
        throw new ExcelValidationException("엑셀 파일에 데이터가 존재하지 않습니다.");
      }

    } catch (Exception e) {
      throw new ExcelValidationException("엑셀 데이터 추출 중 오류가 발생했습니다: " + e.getMessage(), e);
    }

    return excelData;
  }

  /**
   * 엑셀 행을 DTO로 변환
   *
   * @param row       엑셀 행
   * @param rowNumber 행 번호 (오류 메시지용)
   * @return DailyStatisticsReqDto 객체
   */
  private DailyStatisticsReqDto parseRowToDto(Row row, int rowNumber) {
      Cell dateCell = row.getCell(0);
      Cell depWdPriceCell = row.getCell(1);
      Cell dailyProfitLossCell = row.getCell(2);

      if (dateCell == null) {
        throw new ExcelValidationException("행 " + rowNumber + "의 날짜가 비어 있습니다.");
      }

      LocalDate date;
      if (dateCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(dateCell)) {
        date = dateCell.getLocalDateTimeCellValue().toLocalDate();
        localDateDeserializer.deserialize(date.toString()); // 숫자 날짜에 대한 검증
      } else if (dateCell.getCellType() == CellType.STRING) {
        date = localDateDeserializer.deserialize(dateCell.getStringCellValue()); // 문자열 파싱 및 검증
      } else {
        throw new ExcelValidationException("행 " + rowNumber + "의 날짜 형식이 유효하지 않습니다.");
      }

      BigDecimal depWdPrice = null;
      if (depWdPriceCell != null) {
        if (depWdPriceCell.getCellType() == CellType.NUMERIC) {
          depWdPrice = BigDecimal.valueOf(depWdPriceCell.getNumericCellValue());
        } else {
          throw new ExcelValidationException("행 " + rowNumber + "의 입출금 금액이 유효한 숫자가 아닙니다.");
        }
      }

      BigDecimal dailyProfitLoss = null;
      if (dailyProfitLossCell != null) {
        if (dailyProfitLossCell.getCellType() == CellType.NUMERIC) {
          dailyProfitLoss = BigDecimal.valueOf(dailyProfitLossCell.getNumericCellValue());
        } else {
          throw new ExcelValidationException("행 " + rowNumber + "의 일손익 금액이 유효한 숫자가 아닙니다.");
        }
      }

      return DailyStatisticsReqDto.builder()
              .date(date)
              .depWdPrice(depWdPrice)
              .dailyProfitLoss(dailyProfitLoss)
              .build();
  }

  /**
   * DTO의 Bean Validation 수행
   *
   * @param dto       DTO 객체
   * @param rowNumber 행 번호 (오류 메시지용)
   */
  private void validateDto(DailyStatisticsReqDto dto, int rowNumber) {
    Set<ConstraintViolation<DailyStatisticsReqDto>> violations = validator.validate(dto);
    if (!violations.isEmpty()) {
      StringBuilder sb = new StringBuilder();
      sb.append("행 ").append(rowNumber).append("의 유효성 검사 실패: ");
      for (ConstraintViolation<DailyStatisticsReqDto> violation : violations) {
        sb.append(violation.getPropertyPath()).append(" ").append(violation.getMessage()).append("; ");
      }
      throw new ExcelValidationException(sb.toString());
    }
  }

}
