package com.sysmatic2.finalbe.cs.controller;

import com.sysmatic2.finalbe.cs.dto.AdminFAQDto;
import com.sysmatic2.finalbe.cs.dto.FAQResponse;
import com.sysmatic2.finalbe.cs.dto.UserFAQDto;
import com.sysmatic2.finalbe.cs.entity.FAQ;
import com.sysmatic2.finalbe.cs.service.FAQService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/faqs")
@Tag(name = "FAQ Controller", description = "FAQ를 관리하는 컨트롤러")
public class FAQController {

    private final FAQService faqService;

    public FAQController(FAQService faqService) {
        this.faqService = faqService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllFAQs(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int pageSize,
                                                          @RequestHeader(value = "Authorization", required = false) String role) {
        try {
            // role이 null인 경우 접근 제한
            if (role == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "UNAUTHORIZED", "message", "User is not authenticated"));
            }
            // JSON 반환값 Map으로 받아오기
            Map<String, Object> response = faqService.getAllFAQs(page, pageSize, role);

            // JSON 형태로 반환. 상태값 200
            return ResponseEntity.ok(response);

        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "FORBIDDEN", "message", "Access Denied: Security exception occurred"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "INTERNAL_SERVER_ERROR", "message", "An unexpected error occurred on the server"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createFAQ(@RequestBody AdminFAQDto faq, @RequestHeader(value = "Authorization", required = false) String role) {

        try {
            // role이 null이거나 ROLE_ADMIN이 아닌 경우 접근 제한
            if (role == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "UNAUTHORIZED", "message", "User is not authenticated"));
            } else if (!role.equals("ROLE_ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "FORBIDDEN", "message", "Access Denied: User does not have sufficient permissions"));
            }

            // faq 객체의 필수 필드가 유효하지 않은 경우 예외 처리
            if (faq.getWriterId() == null || faq.getQuestion() == null || faq.getQuestion().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "BAD_REQUEST", "message", "Invalid input: Missing required fields"));
            }

            AdminFAQDto createdFAQ = faqService.createFAQ(faq);
            return ResponseEntity.ok(createdFAQ);

        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "FORBIDDEN", "message", "Access Denied: Security exception occurred"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "INTERNAL_SERVER_ERROR", "message", "An unexpected error occurred on the server"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFAQ(@PathVariable Long id, @RequestBody AdminFAQDto faq, @RequestHeader(value = "Authorization", required = false) String role) {
        try {
            AdminFAQDto updatedFAQ = faqService.updateFAQ(id, faq, role);
            return ResponseEntity.ok(updatedFAQ);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "UPDATE_FAILED", "message", "Access Denied"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "UPDATE_FAILED", "message", "FAQ with id " + id + " not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "UPDATE_FAILED", "message", "An unexpected error occurred while updating FAQ"));
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFAQ(@PathVariable Long id, @RequestHeader(value = "Authorization", required = false) String role) {
        try {
            faqService.deleteFAQ(id, role);
            return ResponseEntity.ok(Map.of("message", "FAQ successfully deleted")); // 200 OK 상태 반환
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "DELETE_FAILED", "message", "Access Denied"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "DELETE_FAILED", "message", "FAQ with id " + id + " not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "DELETE_FAILED", "message", "An unexpected error occurred while deleting FAQ"));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchFAQs(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int pageSize,
                                                       @RequestParam(required = false) String keyword) {
        // JSON 반환값 Map으로 받아오기
        Map<String, Object> response = faqService.searchFAQs(page, pageSize, keyword);

        // JSON 형태로 반환. 상태값 200
        return ResponseEntity.ok(response);
    }


}
