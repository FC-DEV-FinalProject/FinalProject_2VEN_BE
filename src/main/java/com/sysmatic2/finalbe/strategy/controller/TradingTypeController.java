package com.sysmatic2.finalbe.strategy.controller;

import com.sysmatic2.finalbe.strategy.dto.TradingTypeRequestDto;
import com.sysmatic2.finalbe.strategy.dto.TradingTypeResponseDto;
import com.sysmatic2.finalbe.strategy.service.TradingTypeService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class TradingTypeController {

    private final TradingTypeService tradingTypeService;

    // 1. 매매유형 목록
    @GetMapping("/trading-types")
    @ApiResponse(responseCode = "200", description = "List of Trading Types")
    public ResponseEntity<Map<String, Object>> getAllTradingTypes(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(required = false) String isActive) {
        // 매매유형 전체 조회
        Page<TradingTypeResponseDto> dtoList = tradingTypeService.findAllTradingTypes(page, pageSize, isActive);

        // 타임스탬프를 추가
        Instant timestamp = Instant.now();

        // 매매유형 개수와 목록 전체 JSON 형태로 반환. 상태값 200
        // Map.of: 불변 Map 객체
        return ResponseEntity.ok(Map.of(
                "count", dtoList.getSize(),
                "data", dtoList,
                "timestamp", timestamp.toString()
        ));
    }

    // 1-1. 매매유형 분류 상세 조회 메서드
    @GetMapping("/trading_types/{id}")
    @ApiResponse(responseCode = "200", description = "Get Trading Type by ID")
    public ResponseEntity<Map<String, Object>> getTradingTypeById(@PathVariable("id") Integer id) {
        // 매매유형 ID로 조회
        TradingTypeResponseDto tradingTypeResponseDto = tradingTypeService.findTradingTypeById(id);

        // 타임스탬프를 추가
        Instant timestamp = Instant.now();

        // 조회한 매매유형 JSON 형태로 반환. 상태값 200
        return ResponseEntity.ok(Map.of(
                "data", tradingTypeResponseDto,
                "timestamp", timestamp.toString()
        ));
    }

    // 2. 매매유형 등록
    @PostMapping("/trading_types")
    @ApiResponse(responseCode = "201", description = "Create Trading Type")
    public ResponseEntity<Map<String, String>> createTradingType(@Valid @RequestBody TradingTypeRequestDto tradingTypeRequestDto) {
        // 매매유형 등록
        tradingTypeService.createTradingType(tradingTypeRequestDto);

        // 등록 성공 메시지 JSON 형태로 반환. 상태값 201
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("msg", "CREATE_SUCCESS"));
    }

    // 3. 매매유형 삭제
    @DeleteMapping("/trading_types/{id}")
    @ApiResponse(responseCode = "204", description = "Delete Trading Type")
    public ResponseEntity<Void> deleteTradingType(@PathVariable Integer id) {
        tradingTypeService.deleteTradingType(id);
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }

    // 3-1. 매매유형 논리적 삭제
    @PatchMapping("/trading_types/{id}")
    @ApiResponse(responseCode = "204", description = "Soft Delete Trading Type")
    public ResponseEntity<Void> softDeleteTradingType(@PathVariable Integer id) {
        tradingTypeService.softDeleteTradingType(id);
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }

    // 4. 매매유형 수정
    @PutMapping("/trading_types/{id}")
    @ApiResponse(responseCode = "204", description = "Update Trading Type")
    public ResponseEntity<Void> updateTradingType(
            @PathVariable Integer id,
            @Valid @RequestBody TradingTypeRequestDto tradingTypeRequestDto) {

        tradingTypeService.updateTradingType(id, tradingTypeRequestDto);
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }
}
