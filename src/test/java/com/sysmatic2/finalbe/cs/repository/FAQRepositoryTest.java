package com.sysmatic2.finalbe.cs.repository;

import com.sysmatic2.finalbe.cs.entity.FAQ;
import com.sysmatic2.finalbe.cs.entity.FAQCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FAQRepositoryTest {

    @Autowired
    private FAQRepository faqRepository;

    @Autowired
    private FAQCategoryRepository faqCategoryRepository;

    @BeforeEach
    public void setUp(){
        faqRepository.deleteAll();
    }

    @Test
    public void whenSaveFaq_thenSuccess_forAdmin(){
        // given
        FAQCategory faqCategory = new FAQCategory();
        faqCategory.setName("category");
        faqCategory.setDescription("This is sample category");
        faqCategoryRepository.save(faqCategory);

        FAQ adminFaq = new FAQ();
        adminFaq.setQuestion("faq_question");
        adminFaq.setAnswer("faq_answer");
        adminFaq.setPostedAt(LocalDateTime.now());
        adminFaq.setWriterId(1L);
        adminFaq.setFaqCategory(faqCategory);

        // when
        FAQ savedFaq = faqRepository.save(adminFaq);

        // then
        assertNotNull(savedFaq.getId());
        assertNotNull(savedFaq.getQuestion());
        assertNotNull(savedFaq.getAnswer());
        assertNotNull(savedFaq.getFaqCategory());
    }

    @Test
    public void whenSaveFaq_thenFindById(){
        // given
        FAQCategory faqCategory = new FAQCategory();
        faqCategory.setName("category");
        faqCategory.setDescription("This is sample category");
        faqCategoryRepository.save(faqCategory);

        FAQ faq = new FAQ();
        faq.setQuestion("faq_question");
        faq.setAnswer("faq_answer");
        faq.setPostedAt(LocalDateTime.now());
        faq.setWriterId(1L);
        faq.setFaqCategory(faqCategory);

        // when
        FAQ savedFaq = faqRepository.save(faq);
        Optional<FAQ> findFAQ = faqRepository.findById(savedFaq.getId());

        // then
        assertTrue(findFAQ.isPresent());
        assertEquals(savedFaq.getQuestion(), findFAQ.get().getQuestion());
        assertEquals(savedFaq.getAnswer(), findFAQ.get().getAnswer());
    }

    @Test
    public void whenSaveFaq_thenFindAll(){
        // given
        FAQCategory faqCategory = new FAQCategory();
        faqCategory.setName("category");
        faqCategory.setDescription("This is sample category");
        faqCategoryRepository.save(faqCategory);

        for(int i=1; i<=10; i++ ){
            FAQ faq = new FAQ();
            faq.setQuestion("faq_question" + i);
            faq.setAnswer("faq_answer" + i);
            faq.setPostedAt(LocalDateTime.now());
            faq.setWriterId(1L);
            faq.setFaqCategory(faqCategory);
            faqRepository.save(faq);
        }

        // when
        List<FAQ> findAll = faqRepository.findAll();

        // then
        assertEquals(10, findAll.size());
        assertEquals("faq_question1", findAll.get(0).getQuestion());
        assertEquals("faq_answer1", findAll.get(0).getAnswer());
    }

    @Test
    @DisplayName("Find all FAQs with pagination")
    void findAllWithPagination() {
        // given
        FAQCategory faqCategory = new FAQCategory();
        faqCategory.setName("category");
        faqCategory.setDescription("This is sample category");
        faqCategoryRepository.save(faqCategory);

        for (int i = 1; i <= 20; i++) {
            FAQ faq = new FAQ();
            faq.setQuestion("faq_question" + i);
            faq.setAnswer("faq_answer" + i);
            faq.setPostedAt(LocalDateTime.now());
            faq.setWriterId(1L);
            faq.setFaqCategory(faqCategory);
            faqRepository.save(faq);
        }

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<FAQ> result = faqRepository.findAll(pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(20);
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.getContent().size()).isEqualTo(10);
        assertThat(result.getContent().get(0).getQuestion()).isEqualTo("faq_question1");
    }





    @Test
    public void whenUpdateById_thenSuccess(){
        // given
        FAQCategory faqCategory = new FAQCategory();
        faqCategory.setName("category");
        faqCategory.setDescription("This is sample category");
        faqCategoryRepository.save(faqCategory);

        for(int i=1; i<=10; i++ ){
            FAQ faq = new FAQ();
            faq.setQuestion("faq_question" + i);
            faq.setAnswer("faq_answer" + i);
            faq.setPostedAt(LocalDateTime.now());
            faq.setWriterId(1L);
            faq.setFaqCategory(faqCategory);
            faqRepository.save(faq);
        }

        List<FAQ> findAll = faqRepository.findAll();

        // when
        findAll.get(0).setQuestion("updqted faq_question");
        findAll.get(0).setAnswer("updqted faq_answer");
        faqRepository.save(findAll.get(0));

        Optional<FAQ> findFAQ = faqRepository.findById(findAll.get(0).getId());

        // then
        assertTrue(findFAQ.isPresent());
        assertEquals(findAll.get(0).getQuestion(), findFAQ.get().getQuestion());
        assertEquals(findAll.get(0).getAnswer(), findFAQ.get().getAnswer());
    }

    @Test
    public void whenDeleteById_thenSuccess(){
        // given
        FAQCategory faqCategory = new FAQCategory();
        faqCategory.setName("category");
        faqCategory.setDescription("This is sample category");
        faqCategoryRepository.save(faqCategory);

        for(int i=1; i<=10; i++ ){
            FAQ faq = new FAQ();
            faq.setQuestion("faq_question" + i);
            faq.setAnswer("faq_answer" + i);
            faq.setPostedAt(LocalDateTime.now());
            faq.setWriterId(1L);
            faq.setFaqCategory(faqCategory);
            faqRepository.save(faq);
        }

        List<FAQ> findAll = faqRepository.findAll();

        // when
        faqRepository.deleteById(findAll.get(0).getId());
        Optional<FAQ> findFAQ = faqRepository.findById(findAll.get(0).getId());

        // then
        assertFalse(findFAQ.isPresent());
    }

    @Test
    public void whenFindByQuestionContainingOrAnswerContaining_thenSuccess(){
        // given
        FAQCategory faqCategory = new FAQCategory();
        faqCategory.setName("category");
        faqCategory.setDescription("This is sample category");
        faqCategoryRepository.save(faqCategory);

        FAQ faq = new FAQ();
        faq.setQuestion("faq_question");
        faq.setAnswer("faq_answer");
        faq.setPostedAt(LocalDateTime.now());
        faq.setWriterId(1L);
        faq.setFaqCategory(faqCategory);
        faqRepository.save(faq);

        FAQ faq2 = new FAQ();
        faq2.setQuestion("what is this?");
        faq2.setAnswer("This is answer");
        faq2.setPostedAt(LocalDateTime.now());
        faq2.setWriterId(1L);
        faq2.setFaqCategory(faqCategory);
        faqRepository.save(faq2);

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<FAQ> result = faqRepository.findByQuestionContainingOrAnswerContaining("faq", "faq", pageable);

        // then
        assertEquals(1, result.getTotalElements());
    }

}