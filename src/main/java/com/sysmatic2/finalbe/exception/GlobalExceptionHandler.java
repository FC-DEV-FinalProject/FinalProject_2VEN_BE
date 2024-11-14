package com.sysmatic2.finalbe.exception;

import jakarta.validation.ConstraintViolationException;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    //500
    //일반적인 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "INTERNAL_SERVER_ERROR",
                "errorType", ex.getClass().getSimpleName(),
                "message", "알 수 없는 오류가 발생했습니다.",
                "timestamp", Instant.now()
        ));
    }

    //400
    //1. HttpMessageNotReadableException 처리
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "error", "HTTP_MESSAGE_NOT_READABLE",
                "errorType", e.getClass().getSimpleName(),
                "message", "잘못된 데이터 타입입니다.",
                "timestamp", Instant.now()
        ));
    }

    //2. 엔티티 유효성 검사 실패
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "error", "CONSTRAINT_VIOLATION",
                "errorType", ex.getClass().getSimpleName(),
                "message", "유효성 검사가 실패했습니다: " + ex.getMessage(),
                "timestamp", Instant.now()
        ));
    }

    //3. 메서드 매개변수 유효성 검사 위배
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
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage()); // 각 필드별 커스텀 메시지 추가
        }

        return ResponseEntity.badRequest().body(Map.of(
                "errors", fieldErrors,
                "errorType", ex.getClass().getSimpleName(),
                "message", "유효성 검사에 실패했습니다.",
                "timestamp", Instant.now()
        ));
    }

    //401
    //1. 미인증(Spring Security 자동 예외 발생)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "error", "UNAUTHORIZED",
                "errorType", e.getClass().getSimpleName(),
                "message", "로그인 정보가 없습니다.",
                "timestamp", Instant.now()
        ));
    }

    //403
    //1. 비인가(Spring Security 자동 예외 발생)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                "error", "FORBIDDEN",
                "errorType", e.getClass().getSimpleName(),
                "message", "권한이 없습니다.",
                "timestamp", Instant.now()
        ));
    }

    //404
    //1. NoSuchElementException 처리
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "error", "NO_SUCH_ELEMENT",
                "errorType", e.getClass().getSimpleName(),
                "message", "해당되는 데이터를 찾을 수 없습니다.",
                "timestamp", Instant.now()
        ));
    }

    //2. 매매유형 찾을 수 없음
    /*
    {
      "status": 404,
      "error": "Not Found",
      "message": "매매유형이 존재하지 않습니다. ID: 123",
      "timestamp": 1678732200000
    }
    */
    @ExceptionHandler(TradingTypeNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTradingTypeNotFoundException(TradingTypeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "error", "NOT_FOUND",
                "errorType", ex.getClass().getSimpleName(),
                "message", ex.getMessage(),
                "timestamp", Instant.now()
        ));
    }

    //3. 존재하지 않는 ID로 삭제 시 발생
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Object> handleEmptyResultDataAccess(EmptyResultDataAccessException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "error", "RESOURCE_NOT_FOUND",
                "errorType", ex.getClass().getSimpleName(),
                "message", "해당되는 데이터가 존재하지 않습니다.",
                "timestamp", Instant.now()
        ));
    }

    //405
    //1. MethodNotAllowedException 처리
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(Map.of(
                "error", "METHOD_NOT_ALLOWED",
                "errorType", ex.getClass().getSimpleName(),
                "message", "호출 메서드가 잘못되었습니다.",
                "timestamp", Instant.now()
        ));
    }

    //409
    //1. 제약 조건 위반(고유성, 외래키, NotNull 위반 등), 참조 무결성 위반(외래 키로 참조된 엔티티 삭제 등)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "error", "DATA_INTEGRITY_VIOLATION",
                "errorType", ex.getClass().getSimpleName(),
                "message", "데이터베이스 제약 조건을 위반했습니다.",
                "timestamp", Instant.now()
        ));
    }

    //2. tradingTypeOrder 유니크 조건 위배
    @ExceptionHandler(DuplicateTradingTypeOrderException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateTradingTypeOrderException(DuplicateTradingTypeOrderException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "error", "DUPLICATE_ORDER",
                "errorType", ex.getClass().getSimpleName(),
                "message", ex.getMessage(),
                "timestamp", Instant.now()
        ));
    }
}
