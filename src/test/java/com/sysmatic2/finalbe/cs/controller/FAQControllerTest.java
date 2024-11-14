package com.sysmatic2.finalbe.cs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sysmatic2.finalbe.cs.dto.AdminFAQDto;
import com.sysmatic2.finalbe.cs.dto.FAQResponse;
import com.sysmatic2.finalbe.cs.dto.UserFAQDto;
import com.sysmatic2.finalbe.cs.entity.FAQ;
import com.sysmatic2.finalbe.cs.entity.FAQCategory;
import com.sysmatic2.finalbe.cs.repository.FAQCategoryRepository;
import com.sysmatic2.finalbe.cs.repository.FAQRepository;
import com.sysmatic2.finalbe.cs.service.FAQService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.ArgumentMatchers.eq;


@WebMvcTest(FAQController.class)
class FAQControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FAQService faqService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Create FAQ - Bad Request with Missing Fields")
    @WithMockUser(roles = {"ADMIN"})
    void createFAQ_BadRequest_MissingFields() throws Exception {
        AdminFAQDto faq = new AdminFAQDto(); // 필수 필드 누락

        mockMvc.perform(post("/api/faqs")
                        .content(objectMapper.writeValueAsString(faq))
                        .with(csrf())  // CSRF 토큰 추가
                        .header(HttpHeaders.AUTHORIZATION, "ROLE_ADMIN")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Invalid input: Missing required fields"));
    }

    @Test
    @DisplayName("Create FAQ - Success for Admin")
    @WithMockUser(roles = {"ADMIN"})
    void createFAQ_AdminSuccess() throws Exception {
        // given
        AdminFAQDto faq = new AdminFAQDto();
        faq.setQuestion("New Question?");
        faq.setAnswer("New Answer");
        faq.setWriterId(1L);
        faq.setPostedAt(LocalDateTime.now());

        AdminFAQDto createdFaq = new AdminFAQDto();
        createdFaq.setId(1L);
        createdFaq.setQuestion("New Question?");
        createdFaq.setAnswer("New Answer");
        createdFaq.setWriterId(1L);
        createdFaq.setPostedAt(LocalDateTime.now());

        given(faqService.createFAQ(any(AdminFAQDto.class))).willReturn(createdFaq);

        // when & then
        mockMvc.perform(post("/api/faqs")
                        .with(csrf())  // CSRF 토큰 추가
                        .content(objectMapper.writeValueAsString(faq))
                        .header("Authorization", "ROLE_ADMIN")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.question").value("New Question?"))
                .andExpect(jsonPath("$.answer").value("New Answer"));
    }



    @Test
    @DisplayName("Get all FAQs - Success for Admin")
    @WithMockUser(roles = {"ADMIN"})
    void getAllFAQs_AdminSuccess() throws Exception {
        // given
        FAQCategory category = new FAQCategory(1L, "general", "this is general");
        AdminFAQDto adminFAQ1 = new AdminFAQDto(1L, "Admin Question 1?", "Admin Answer 1", 1L, LocalDateTime.now(), null, true, category);
        AdminFAQDto adminFAQ2 = new AdminFAQDto(2L, "Admin Question 2?", "Admin Answer 2", 2L, LocalDateTime.now(), null, true, category);

        List<AdminFAQDto> faqs = List.of(adminFAQ1, adminFAQ2);
        Page<AdminFAQDto> faqPage = new PageImpl<>(faqs, PageRequest.of(0, 10), faqs.size());
        Map<String, Object> response = Map.of(
                "totalElements", faqPage.getTotalElements(),
                "isFirstPage", faqPage.isFirst(),
                "isLastPage", faqPage.isLast(),
                "totalPages", faqPage.getTotalPages(),
                "isSorted", faqPage.getSort().isSorted(),
                "pageSize", faqPage.getSize(),
                "currentPage", faqPage.getNumber(),
                "data", faqPage.getContent()
        );

        // faqService.getAllFAQs 호출 시 response 반환 설정
        given(faqService.getAllFAQs(0, 10, "ROLE_ADMIN")).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/faqs")
                        .param("page", "0")
                        .param("pageSize", "10")
                        .header("Authorization", "ROLE_ADMIN")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalElements").value(faqs.size()))
                .andExpect(jsonPath("$.isFirstPage").value(true))
                .andExpect(jsonPath("$.isLastPage").value(true))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.pageSize").value(10))
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.data[0].question").value("Admin Question 1?"))
                .andExpect(jsonPath("$.data[0].answer").value("Admin Answer 1"))
                .andExpect(jsonPath("$.data[0].faqCategory.name").value("general"))
                .andExpect(jsonPath("$.data[1].question").value("Admin Question 2?"))
                .andExpect(jsonPath("$.data[1].answer").value("Admin Answer 2"))
                .andExpect(jsonPath("$.data[1].faqCategory.name").value("general"));
    }

    @Test
    @DisplayName("Update FAQ - Success for Admin")
    @WithMockUser(roles = {"ADMIN"})
    void updateFAQ_AdminSuccess() throws Exception {
        // given
        FAQCategory category = new FAQCategory(1L, "general", "this is general");
        AdminFAQDto faqToUpdate = new AdminFAQDto(1L, "Updated Question?", "Updated Answer", 1L, LocalDateTime.now(), null, true, category);

        AdminFAQDto updatedFAQ = new AdminFAQDto(1L, "Updated Question?", "Updated Answer", 1L, LocalDateTime.now(), LocalDateTime.now(), true, category);

        given(faqService.updateFAQ(eq(1L), any(AdminFAQDto.class), eq("ROLE_ADMIN"))).willReturn(updatedFAQ);

        // when & then
        mockMvc.perform(put("/api/faqs/{id}", 1)
                        .with(csrf())  // CSRF 토큰 추가
                        .header("Authorization", "ROLE_ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faqToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedFAQ.getId()))
                .andExpect(jsonPath("$.question").value(updatedFAQ.getQuestion()))
                .andExpect(jsonPath("$.answer").value(updatedFAQ.getAnswer()));
    }


    @Test
    @DisplayName("Update FAQ - Not Found")
    @WithMockUser(roles = {"ADMIN"})
    void updateFAQ_NotFound() throws Exception {
        // given
        AdminFAQDto faqToUpdate = new AdminFAQDto(1L, "Updated Question?", "Updated Answer", 1L, LocalDateTime.now(), null, true, null);

        given(faqService.updateFAQ(eq(1L), any(AdminFAQDto.class), eq("ROLE_ADMIN"))).willThrow(new IllegalArgumentException("FAQ with id 1 not found"));

        // when & then
        mockMvc.perform(put("/api/faqs/{id}", 1)
                        .with(csrf())
                        .header("Authorization", "ROLE_ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faqToUpdate)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("UPDATE_FAILED"))
                .andExpect(jsonPath("$.message").value("FAQ with id 1 not found"));
    }

    @Test
    @DisplayName("Update FAQ - Internal Server Error")
    @WithMockUser(roles = {"ADMIN"})
    void updateFAQ_InternalServerError() throws Exception {
        // given
        AdminFAQDto faqToUpdate = new AdminFAQDto(1L, "Updated Question?", "Updated Answer", 1L, LocalDateTime.now(), null, true, null);

        given(faqService.updateFAQ(eq(1L), any(AdminFAQDto.class), eq("ROLE_ADMIN"))).willThrow(new RuntimeException());

        // when & then
        mockMvc.perform(put("/api/faqs/{id}", 1)
                        .with(csrf())
                        .header("Authorization", "ROLE_ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faqToUpdate)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("UPDATE_FAILED"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred while updating FAQ"));
    }


    @Test
    @DisplayName("Delete FAQ - Success for Admin")
    @WithMockUser(roles = {"ADMIN"})
    void deleteFAQ_AdminSuccess() throws Exception {
        // given
        willDoNothing().given(faqService).deleteFAQ(eq(1L), eq("ROLE_ADMIN"));

        // when & then
        mockMvc.perform(delete("/api/faqs/{id}", 1)
                        .with(csrf())  // CSRF 토큰 추가
                        .header("Authorization", "ROLE_ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("FAQ successfully deleted"));
    }

    @Test
    @DisplayName("Delete FAQ - Not Found")
    @WithMockUser(roles = {"ADMIN"})
    void deleteFAQ_NotFound() throws Exception {
        // given
        willThrow(new IllegalArgumentException("FAQ with id 1 not found")).given(faqService).deleteFAQ(eq(1L), eq("ROLE_ADMIN"));

        // when & then
        mockMvc.perform(delete("/api/faqs/{id}", 1)
                        .with(csrf())
                        .header("Authorization", "ROLE_ADMIN"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("DELETE_FAILED"))
                .andExpect(jsonPath("$.message").value("FAQ with id 1 not found"));
    }

    @Test
    @DisplayName("Delete FAQ - Internal Server Error")
    @WithMockUser(roles = {"ADMIN"})
    void deleteFAQ_InternalServerError() throws Exception {
        // given
        willThrow(new RuntimeException("Unexpected error")).given(faqService).deleteFAQ(eq(1L), eq("ROLE_ADMIN"));

        // when & then
        mockMvc.perform(delete("/api/faqs/{id}", 1)
                        .with(csrf())
                        .header("Authorization", "ROLE_ADMIN"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("DELETE_FAILED"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred while deleting FAQ"));
    }


    @ParameterizedTest
    @DisplayName("Search FAQs - Success for Keyword 'FAQ'")
    @ValueSource(strings = {"ADMIN", "USER"})
    @WithMockUser(roles = {"USER", "ADMIN"})
    void searchFAQs_Success(String role) throws Exception {
        // given
        String keyword = "Admin";

        FAQCategory category = new FAQCategory(1L, "general", "this is general");
        FAQ faq1 = new FAQ(1L, 1L, "Admin Question 1?", "Admin Answer 1",  LocalDateTime.now(), LocalDateTime.now(), null, category);
        FAQ faq2 = new FAQ(2L, 1L, "Admin Question 2?", "Admin Answer 2",  LocalDateTime.now(), LocalDateTime.now(), null, category);

        List<FAQ> faqs = List.of(faq1, faq2);
        Page<FAQ> faqPage = new PageImpl<>(faqs, PageRequest.of(0, 10), faqs.size());
        Map<String, Object> response = Map.of(
                "totalElements", faqPage.getTotalElements(),
                "isFirstPage", faqPage.isFirst(),
                "isLastPage", faqPage.isLast(),
                "totalPages", faqPage.getTotalPages(),
                "isSorted", faqPage.getSort().isSorted(),
                "pageSize", faqPage.getSize(),
                "currentPage", faqPage.getNumber(),
                "data", faqPage.getContent()
        );

        given(faqService.searchFAQs(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyString())).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/faqs/search")
                        .param("page", "0")
                        .param("pageSize", "10")
                        .param("keyword", keyword)
                        .header("Authorization", role)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalElements").value(faqs.size()))
                .andExpect(jsonPath("$.isFirstPage").value(true))
                .andExpect(jsonPath("$.isLastPage").value(true))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.pageSize").value(10))
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.data[0].question").value("Admin Question 1?"))
                .andExpect(jsonPath("$.data[0].answer").value("Admin Answer 1"))
                .andExpect(jsonPath("$.data[0].faqCategory.name").value("general"))
                .andExpect(jsonPath("$.data[1].question").value("Admin Question 2?"))
                .andExpect(jsonPath("$.data[1].answer").value("Admin Answer 2"))
                .andExpect(jsonPath("$.data[1].faqCategory.name").value("general"));
    }

}
