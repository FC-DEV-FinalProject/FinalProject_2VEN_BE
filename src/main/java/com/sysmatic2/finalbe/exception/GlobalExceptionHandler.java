package com.sysmatic2.finalbe.exception;

import jakarta.validation.ConstraintViolationException;
import org.apache.coyote.BadRequestException;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.MethodNotAllowedException;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 일반적인 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Internal Server Error",
                "message", "알 수 없는 오류가 발생했습니다.",
                "timestamp", Instant.now().toEpochMilli()
        ));
    }
    /*
        {
          "errors": {
            "tradingTypeOrder": "매매유형 순서는 양수여야 합니다.",
            "tradingTypeName": "매매유형명은 필수 입력 값입니다.",
            "tradingTypeIcon": "매매유형 아이콘 URL은 필수 입력 값입니다.",
            "isActive": "사용유무는 'Y' 또는 'N'만 허용됩니다."
          },
          "timestamp": 1678732200000
        }
     */
    // requestDto 유효성 검사 위배
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage()); // 각 필드별 커스텀 메시지 추가
        }

        return ResponseEntity.badRequest().body(Map.of(
                "errors", fieldErrors,
                "timestamp", Instant.now().toEpochMilli()
        ));
    }

    /*
        {
          "status": 404,
          "error": "Not Found",
          "message": "매매유형이 존재하지 않습니다. ID: 123",
          "timestamp": 1678732200000
        }
    */
    // 매매유형 찾을 수 없음
    @ExceptionHandler(TradingTypeNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTradingTypeNotFoundException(TradingTypeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "error", "Not Found",
                "message", ex.getMessage(),
                "timestamp", Instant.now().toEpochMilli()
        ));
    }

    // tradingTypeOrder 유니크 조건 위배
    @ExceptionHandler(DuplicateTradingTypeOrderException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateTradingTypeOrderException(DuplicateTradingTypeOrderException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "error", "Duplicate Order",
                "message", ex.getMessage(),
                "timestamp", Instant.now().toEpochMilli()
        ));
    }

    // 제약 조건 위반(고유성, 외래키, NotNull 위반 등), 참조 무결성 위반(외래 키로 참조된 엔티티 삭제 등)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "error", "Data Integrity Violation",
                "message", "데이터베이스 제약 조건을 위반했습니다.",
                "timestamp", Instant.now()
        ));
    }

    // 엔티티 유효성 검사 실패
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "error", "Constraint Violation",
                "message", "유효성 검사가 실패했습니다: " + ex.getMessage(),
                "timestamp", Instant.now()
        ));
    }

    // 존재하지 않는 ID로 삭제 시 발생
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Object> handleEmptyResultDataAccess(EmptyResultDataAccessException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "error", "Resource Not Found",
                "message", "삭제하려는 데이터가 존재하지 않습니다.",
                "timestamp", Instant.now()
        ));
    }

    //400
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map> handleBadRequestException(BadRequestException e) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", "BAD_REQUEST");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
    }

    //400
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", "BAD_REQUEST");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
    }

    //401 - 미인증(Spring Security 자동 예외 발생)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map> handleAuthenticationException(AuthenticationException e) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", "UNAUTHENTICATED");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
    }

    //403 - 비인가(Spring Security 자동 예외 발생)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map> handleAccessDeniedException(AccessDeniedException e) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", "UNAUTHORIZED");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMap);
    }

    //404
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map> handleNoSuchElementException(NoSuchElementException e) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", "NOT_FOUND");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);
    }

    //405
    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity<Map> handleMethodNotAllowedException(MethodNotAllowedException e) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", "METHOD_NOT_ALLOWED");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorMap);
    }
}
