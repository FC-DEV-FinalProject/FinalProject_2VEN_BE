package com.sysmatic2.finalbe.cs.service;

import com.sysmatic2.finalbe.cs.dto.AdminFAQDto;
import com.sysmatic2.finalbe.cs.dto.FAQResponse;
import com.sysmatic2.finalbe.cs.dto.UserFAQDto;
import com.sysmatic2.finalbe.cs.entity.FAQ;
import com.sysmatic2.finalbe.cs.repository.FAQCategoryRepository;
import com.sysmatic2.finalbe.cs.repository.FAQRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sysmatic2.finalbe.util.CreatePageResponse.createPageResponse;

@Service
public class FAQService {

    private final FAQRepository faqRepository;
    private final FAQCategoryRepository faqCategoryRepository;

    @Autowired
    public FAQService(FAQRepository faqRepository, FAQCategoryRepository faqCategoryRepository) {
        this.faqRepository = faqRepository;
        this.faqCategoryRepository = faqCategoryRepository;
    }

    @Transactional
    public AdminFAQDto createFAQ(AdminFAQDto AdminFaq){
        FAQ faq = new FAQ();
        faq.setWriterId(AdminFaq.getWriterId());
        faq.setQuestion(AdminFaq.getQuestion());
        faq.setAnswer(AdminFaq.getAnswer());
        faq.setPostedAt(AdminFaq.getPostedAt());
        faq.setFaqCategory(AdminFaq.getFaqCategory());

        FAQ savedFaq = faqRepository.save(faq);
        return new AdminFAQDto(
                savedFaq.getId(),
                savedFaq.getQuestion(),
                savedFaq.getAnswer(),
                savedFaq.getWriterId(),
                savedFaq.getPostedAt(),
                savedFaq.getUpdatedAt(),
                savedFaq.getIsActive(),
                savedFaq.getFaqCategory()
        );
    }


    // 역할에 따른 FAQ 목록 조회
    @Transactional(readOnly = true)
    public Map<String, Object> getAllFAQs(int page, int pageSize, String role) {

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").ascending());
        Page<FAQ> faqPage = faqRepository.findAll(pageable);

        // ADMIN 역할인 경우 AdminFAQDto로 매핑하여 반환
        if ("ROLE_ADMIN".equalsIgnoreCase(role)) {

            // 페이지 객체 리스트 타입 변경
            Page<AdminFAQDto> pageDtoList = faqPage.map(faq -> new AdminFAQDto(
                                        faq.getId(),
                                        faq.getQuestion(),
                                        faq.getAnswer(),
                                        faq.getWriterId(),
                                        faq.getPostedAt(),
                                        faq.getUpdatedAt(),
                                        faq.getIsActive(),
                                        faq.getFaqCategory()
            ));
            return createPageResponse(pageDtoList);
        }
        // USER 역할인 경우 UserFAQDto로 매핑하여 반환
        else {
            Page<UserFAQDto> pageDtoList = faqPage.map(faq -> new UserFAQDto(
                    faq.getId(),
                    faq.getQuestion(),
                    faq.getAnswer(),
                    faq.getFaqCategory()
            ));
            return createPageResponse(pageDtoList);
        }

    }

    public FAQResponse getFAQById(Long id, String role) {
        FAQ faq = faqRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("FAQ with id " + id + " not found"));

        if ("ROLE_ADMIN".equalsIgnoreCase(role)) {
            return new AdminFAQDto(
                    faq.getId(),
                    faq.getQuestion(),
                    faq.getAnswer(),
                    faq.getWriterId(),
                    faq.getPostedAt(),
                    faq.getUpdatedAt(),
                    faq.getIsActive(),
                    faq.getFaqCategory()
            );
        } else {
            return new UserFAQDto(
                    faq.getId(),
                    faq.getQuestion(),
                    faq.getAnswer(),
                    faq.getFaqCategory()
            );
        }
    }

    @Transactional
    public AdminFAQDto updateFAQ(Long id, AdminFAQDto faq, String role){

        // 기존 FAQ 조회. 존재하지 않으면 예외 발생
        FAQ existingFAQ = faqRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("FAQ with id " + id + " not found"));

        // 필드 업데이트
        existingFAQ.setQuestion(faq.getQuestion());
        existingFAQ.setAnswer(faq.getAnswer());
        existingFAQ.setPostedAt(faq.getPostedAt());
        existingFAQ.setFaqCategory(faq.getFaqCategory());
        existingFAQ.setQuestion(faq.getQuestion());
        existingFAQ.setAnswer(faq.getAnswer());
        existingFAQ.setPostedAt(faq.getPostedAt());
        existingFAQ.setFaqCategory(faq.getFaqCategory());

        FAQ savedFaq = faqRepository.save(existingFAQ);
        return new AdminFAQDto(
                savedFaq.getId(),
                savedFaq.getQuestion(),
                savedFaq.getAnswer(),
                savedFaq.getWriterId(),
                savedFaq.getPostedAt(),
                savedFaq.getUpdatedAt(),
                savedFaq.getIsActive(),
                savedFaq.getFaqCategory()
        );
    }

    @Transactional
    public void deleteFAQ(Long id, String role){

        // 기존 FAQ 조회. 존재하지 않으면 예외 발생
        FAQ existingFAQ = faqRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("FAQ with id " + id + " not found"));

        faqRepository.delete(existingFAQ);
    }


    public Map<String, Object> searchFAQs(int page, int pageSize, String keyword) {

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").ascending());
        Page<FAQ> faqPage = faqRepository.findByQuestionContainingOrAnswerContaining(keyword, keyword, pageable);


        // 페이지 객체 리스트 타입 변경
        Page<UserFAQDto> pageDtoList = faqPage.map(faq -> new UserFAQDto(
                faq.getId(),
                faq.getQuestion(),
                faq.getAnswer(),
                faq.getFaqCategory()
        ));
        return createPageResponse(pageDtoList);

    }

}
