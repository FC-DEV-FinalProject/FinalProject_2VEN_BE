package com.sysmatic2.finalbe.strategy.controller;

import com.sysmatic2.finalbe.strategy.dto.TradingTypeAdminRequestDto;
import com.sysmatic2.finalbe.strategy.dto.TradingTypeAdminResponseDto;
import com.sysmatic2.finalbe.strategy.service.TradingTypeService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Validated
public class TradingTypeController {

    private final TradingTypeService tradingTypeService;

    // 1. 매매유형 목록
    @GetMapping("/trading-types")
    @ApiResponse(responseCode = "200", description = "List of Trading Types")
    public ResponseEntity<Map<String, Object>> getAllTradingTypes(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page number must be 0 or greater") int page,
            @RequestParam(defaultValue = "10") @Positive(message = "Page size must be greater than zero") int pageSize,
            @RequestParam(required = false) String isActive) {
        // JSON 반환값 Map으로 받아오기
        Map<String, Object> response = tradingTypeService.findAllTradingTypes(page, pageSize, isActive);

        // JSON 형태로 반환. 상태값 200
        return ResponseEntity.ok(response);
    }

    // 1-1. 매매유형 상세 조회 메서드
    @GetMapping("/trading-types/{id}")
    @ApiResponse(responseCode = "200", description = "Get Trading Type by ID")
    public ResponseEntity<Map<String, Object>> getTradingTypeById(@PathVariable("id") Integer id) {
        // 매매유형 ID로 조회
        TradingTypeAdminResponseDto tradingTypeAdminResponseDto = tradingTypeService.findTradingTypeById(id);

        // 타임스탬프를 추가
        Instant timestamp = Instant.now();

        // 조회한 매매유형 JSON 형태로 반환. 상태값 200
        return ResponseEntity.ok(Map.of(
                "data", tradingTypeAdminResponseDto,
                "timestamp", timestamp.toString()
        ));
    }

    // 2. 매매유형 등록
    @PostMapping("/trading-types")
    @ApiResponse(responseCode = "201", description = "Create Trading Type")
    public ResponseEntity<Map<String, String>> createTradingType(@Valid @RequestBody TradingTypeAdminRequestDto tradingTypeAdminRequestDto) {
        // 매매유형 등록
        tradingTypeService.createTradingType(tradingTypeAdminRequestDto);

        // 타임스탬프를 추가
        Instant timestamp = Instant.now();

        // 등록 성공 메시지 JSON 형태로 반환. 상태값 201
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "msg", "CREATE_SUCCESS",
                "timestamp", timestamp.toString()
        ));
    }

    // 3. 매매유형 삭제
    @DeleteMapping("/trading-types/{id}")
    @ApiResponse(responseCode = "200", description = "Delete Trading Type")
    public ResponseEntity<Map<String, String>> deleteTradingType(@PathVariable Integer id) {
        tradingTypeService.deleteTradingType(id);

        // 타임스탬프를 추가
        Instant timestamp = Instant.now();

        return ResponseEntity.ok(Map.of(
                "msg", "DELETE_SUCCESS",
                "timestamp", timestamp.toString()
        ));
    }

    // 3-1. 매매유형 논리적 삭제
    @PatchMapping("/trading-types/{id}")
    @ApiResponse(responseCode = "200", description = "Soft Delete Trading Type")
    public ResponseEntity<Map<String, String>> softDeleteTradingType(@PathVariable Integer id) {
        tradingTypeService.softDeleteTradingType(id);

        // 타임스탬프를 추가
        Instant timestamp = Instant.now();

        return ResponseEntity.ok(Map.of(
                "msg", "DELETE_SUCCESS",
                "timestamp", timestamp.toString()
        ));
    }

    // 4. 매매유형 수정
    @PutMapping("/trading-types/{id}")
    @ApiResponse(responseCode = "200", description = "Update Trading Type")
    public ResponseEntity<Map<String, String>> updateTradingType(
            @PathVariable Integer id,
            @Valid @RequestBody TradingTypeAdminRequestDto tradingTypeAdminRequestDto) {
        System.out.println("id = " + id);
        System.out.println("tradingTypeService = " + tradingTypeAdminRequestDto);
        tradingTypeService.updateTradingType(id, tradingTypeAdminRequestDto);

        // 타임스탬프를 추가
        Instant timestamp = Instant.now();

        return ResponseEntity.ok(Map.of(
                "msg", "UPDATE_SUCCESS",
                "timestamp", timestamp.toString()
        ));
    }
}
