package com.sysmatic2.finalbe.cs.service;

import com.sysmatic2.finalbe.cs.dto.AdminFAQDto;
import com.sysmatic2.finalbe.cs.dto.FAQResponse;
import com.sysmatic2.finalbe.cs.dto.UserFAQDto;
import com.sysmatic2.finalbe.cs.entity.FAQ;
import com.sysmatic2.finalbe.cs.entity.FAQCategory;
import com.sysmatic2.finalbe.cs.repository.FAQCategoryRepository;
import com.sysmatic2.finalbe.cs.repository.FAQRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FAQServiceTest {

    @Autowired
    private FAQService faqService;

    @Autowired
    private FAQCategoryRepository faqCategoryRepository;

    @Autowired
    private FAQRepository faqRepository;

    private FAQCategory category;

    @BeforeEach
    void setUp() {
        faqRepository.deleteAll();
        faqCategoryRepository.deleteAll();

        // 테스트용 FAQCatgory를 저장
        category = new FAQCategory();
        category.setName("General");
        category.setDescription("General FAQs");
        faqCategoryRepository.save(category);
    }

    @Test
    public void whenCreateFAQ_thenSuccessForAdmin() {
        // given
        AdminFAQDto faq = new AdminFAQDto();
        faq.setQuestion("What is FAQ?");
        faq.setAnswer("This is FAQ");
        faq.setWriterId(1L);
        faq.setPostedAt(LocalDateTime.now());
        faq.setFaqCategory(category);

        // when
        AdminFAQDto createdFAQ = faqService.createFAQ(faq);

        // then
        assertNotNull(createdFAQ);
        assertEquals(createdFAQ.getQuestion(), faq.getQuestion());
        assertEquals(createdFAQ.getAnswer(), faq.getAnswer());
    }

    @Test
    void getAllFAQsAsAdminTest() {
        // given
        int page = 0;
        int pageSize = 10;
        String role = "ROLE_ADMIN";

        for (int i = 1; i <= 10; i++) {
            AdminFAQDto faq = new AdminFAQDto();
            faq.setQuestion("What is FAQ?" + i);
            faq.setAnswer("This is FAQ" + i);
            faq.setWriterId(1L);
            faq.setPostedAt(LocalDateTime.now());
            faq.setFaqCategory(category);
            faqService.createFAQ(faq);
        }

        // when
        Map<String, Object> response = faqService.getAllFAQs(page, pageSize, role);

        // then
        assertEquals(10L, response.get("totalElements"));
        assertEquals(1, response.get("totalPages"));
        assertTrue((Boolean) response.get("isFirstPage"));
        assertTrue((Boolean) response.get("isLastPage"));
        assertEquals(page, response.get("currentPage"));
        assertEquals(pageSize, response.get("pageSize"));

        // 데이터 검증
        List<?> data = (List<?>) response.get("data");
        assertEquals(10, data.size());
        assertTrue(data.get(0) instanceof AdminFAQDto);
    }


    @Test
    void getAllFAQsAsUserTest() {
        // given
        int page = 0;
        int pageSize = 10;
        String role = "ROLE_USER";

        for (int i = 1; i <= 10; i++) {
            AdminFAQDto faq = new AdminFAQDto();
            faq.setQuestion("What is FAQ?" + i);
            faq.setAnswer("This is FAQ" + i);
            faq.setWriterId(1L);
            faq.setPostedAt(LocalDateTime.now());
            faq.setFaqCategory(category);
            faqService.createFAQ(faq);
        }

        // when
        Map<String, Object> response = faqService.getAllFAQs(page, pageSize, role);

        // then
        assertEquals(10L, response.get("totalElements"));
        assertEquals(1, response.get("totalPages"));
        assertTrue((Boolean) response.get("isFirstPage"));
        assertTrue((Boolean) response.get("isLastPage"));
        assertEquals(page, response.get("currentPage"));
        assertEquals(pageSize, response.get("pageSize"));

        // 데이터 검증
        List<?> data = (List<?>) response.get("data");
        assertEquals(10, data.size());
        assertTrue(data.get(0) instanceof UserFAQDto);
    }

    @Test
    public void whenCreateFAQ_thenFindByIdForAdmin(){
        // given
        AdminFAQDto faq = new AdminFAQDto();
        faq.setQuestion("What is FAQ?");
        faq.setAnswer("This is FAQ");
        faq.setWriterId(1L);
        faq.setPostedAt(LocalDateTime.now());
        faq.setFaqCategory(category);

        // when
        AdminFAQDto createdFAQ = faqService.createFAQ(faq);
        FAQ findFAQ = faqRepository.findById(createdFAQ.getId()).get();

        // then
        assertNotNull(findFAQ);
        assertEquals(createdFAQ.getQuestion(), findFAQ.getQuestion());
        assertEquals(createdFAQ.getAnswer(), findFAQ.getAnswer());
    }

    @Test
    public void whenUpdateById_thenSuccessForAdmin() {
        // given
        AdminFAQDto faq = new AdminFAQDto();
        faq.setQuestion("What is FAQ?");
        faq.setAnswer("This is FAQ");
        faq.setWriterId(1L);
        faq.setPostedAt(LocalDateTime.now());
        faq.setFaqCategory(category);
        AdminFAQDto createdFAQ = faqService.createFAQ(faq);

        // when
        createdFAQ.setQuestion("updated FAQ?");
        createdFAQ.setAnswer("updated FAQ");

//        // 업데이트를 위해 AdminFAQDto를 FAQ 엔티티로 변환
//        FAQ updatedFAQEntity = new FAQ();
//        updatedFAQEntity.setId(createdFAQ.getId());  // ID 설정이 필요
//        updatedFAQEntity.setQuestion(createdFAQ.getQuestion());
//        updatedFAQEntity.setAnswer(createdFAQ.getAnswer());
//        updatedFAQEntity.setWriterId(createdFAQ.getWriterId());
//        updatedFAQEntity.setPostedAt(createdFAQ.getPostedAt());
//        updatedFAQEntity.setFaqCategory(createdFAQ.getFaqCategory());
//        updatedFAQEntity.setIsActive(createdFAQ.getIsActive());

        AdminFAQDto updatedFAQ = faqService.updateFAQ(createdFAQ.getId(), createdFAQ, "ADMIN");

        // then
        assertNotNull(updatedFAQ);
        assertEquals(createdFAQ.getId(), updatedFAQ.getId());
        assertEquals("updated FAQ?", updatedFAQ.getQuestion());
        assertEquals("updated FAQ", updatedFAQ.getAnswer());
    }

    @Test
    public void whenDeleteById_thenSuccessForAdmin() {
        // given
        AdminFAQDto faq = new AdminFAQDto();
        faq.setQuestion("What is FAQ?");
        faq.setAnswer("This is FAQ");
        faq.setWriterId(1L);
        faq.setPostedAt(LocalDateTime.now());
        faq.setFaqCategory(category);
        AdminFAQDto createdFAQ = faqService.createFAQ(faq);

        // when
        faqService.deleteFAQ(createdFAQ.getId(), "ADMIN");

        //then
        assertFalse(faqRepository.findById(createdFAQ.getId()).isPresent());
    }

    @Test
    @DisplayName("Search FAQs - Success for Keyword 'FAQ'")
    void testSearchFAQs_withKeywordFAQ() {
        // given
        String keyword = "faq";
        int page = 0;
        int pageSize = 10;
        String role = "ROLE_USER";

        FAQ faq = new FAQ();
        faq.setQuestion("What is faq?");
        faq.setAnswer("This is faq");
        faq.setWriterId(1L);
        faq.setPostedAt(LocalDateTime.now());
        faq.setFaqCategory(category);

        FAQ faq2 = new FAQ();
        faq2.setQuestion("What is this?");
        faq2.setAnswer("This is answer");
        faq2.setWriterId(1L);
        faq2.setPostedAt(LocalDateTime.now());
        faq2.setFaqCategory(category);

        faqRepository.save(faq);
        faqRepository.save(faq2);

        // when
        Map<String, Object> result = faqService.searchFAQs(page, pageSize, keyword);

        // then
        assertEquals(1L, result.get("totalElements"));
        assertEquals(1, result.get("totalPages"));
        assertTrue((Boolean) result.get("isFirstPage"));
        assertTrue((Boolean) result.get("isLastPage"));
        assertEquals(page, result.get("currentPage"));
        assertEquals(pageSize, result.get("pageSize"));

        // FAQ 데이터 확인
        @SuppressWarnings("unchecked")
        List<UserFAQDto> data = (List<UserFAQDto>) result.get("data");
        assertEquals(1, data.size());

        UserFAQDto faqData = data.get(0);
        assertEquals("What is faq?", faqData.getQuestion());
        assertEquals("This is faq", faqData.getAnswer());

    }
}