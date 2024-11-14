package com.sysmatic2.finalbe.cs.repository;

import com.sysmatic2.finalbe.cs.entity.FAQ;
import com.sysmatic2.finalbe.cs.entity.FAQCategory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestPropertySource;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FAQCategoryRepositoryTest {

    @Autowired
    private FAQCategoryRepository fAQCategoryRepository;

    @Autowired
    private FAQRepository faqRepository;

    @BeforeEach
    public void setUp(){
        faqRepository.deleteAll();
        fAQCategoryRepository.deleteAll();
    }

    @Test
    public void whenSaveFAQCategory_thenSuccess(){
        // given
        FAQCategory faqCategory = new FAQCategory();
        faqCategory.setName("register");
        faqCategory.setDescription("About registering");

        // when
        FAQCategory savedFaqCategory = fAQCategoryRepository.save(faqCategory);

        // then
        assertNotNull(savedFaqCategory);
        assertEquals(savedFaqCategory.getName(), faqCategory.getName());
        assertEquals(savedFaqCategory.getDescription(), faqCategory.getDescription());
    }

    @Test
    public void whenSaveFAQCategory_thenFindById(){
        // given
        for(int i=1; i<=10; i++){
            FAQCategory faqCategory = new FAQCategory();
            faqCategory.setName("register" + i);
            faqCategory.setDescription("About registering" + i);
            fAQCategoryRepository.save(faqCategory);
        }

        // When
        List<FAQCategory> faqCategories = fAQCategoryRepository.findAll();
        assertEquals(10, faqCategories.size());
        Optional<FAQCategory> findedFaqCategory = fAQCategoryRepository.findById(faqCategories.get(0).getId());

        // then
        assertTrue(findedFaqCategory.isPresent());
        assertEquals(faqCategories.get(0).getName(), findedFaqCategory.get().getName());
        assertEquals(faqCategories.get(0).getDescription(), findedFaqCategory.get().getDescription());
    }

    @Test
    public void whenSaveFAQCategory_thenFindAll(){
        // given
        for(int i=1; i<=10; i++){
            FAQCategory faqCategory = new FAQCategory();
            faqCategory.setName("register" + i);
            faqCategory.setDescription("About registering" + i);
            fAQCategoryRepository.save(faqCategory);
        }

        // when & then
        List<FAQCategory> findedFaqCategories = fAQCategoryRepository.findAll();
        assertEquals(10, findedFaqCategories.size());
        assertEquals("register1", findedFaqCategories.get(0).getName());
    }

    @Test
    public void whenUpdateByIdFAQCategory_thenSuccess(){
        // given
        for(int i=1; i<=10; i++){
            FAQCategory faqCategory = new FAQCategory();
            faqCategory.setName("register" + i);
            faqCategory.setDescription("About registering" + i);
            fAQCategoryRepository.save(faqCategory);
        }

        // When
        List<FAQCategory> faqCategories = fAQCategoryRepository.findAll();
        assertEquals(10, faqCategories.size());

        faqCategories.get(0).setName("update");
        faqCategories.get(0).setDescription("updated");
        fAQCategoryRepository.save(faqCategories.get(0));
        Optional<FAQCategory> faqCategory = fAQCategoryRepository.findById(faqCategories.get(0).getId());

        // then
        assertTrue(faqCategory.isPresent());
        assertEquals(faqCategories.get(0).getName(), faqCategory.get().getName());
        assertEquals(faqCategories.get(0).getDescription(), faqCategory.get().getDescription());
    }

    @Test
    public void whenDeleteByIdFAQCategory_thenSuccess(){
        // given
        for(int i=1; i<=10; i++){
            FAQCategory faqCategory = new FAQCategory();
            faqCategory.setName("register" + i);
            faqCategory.setDescription("About registering" + i);
            fAQCategoryRepository.save(faqCategory);
        }

        // when
        List<FAQCategory> faqCategories = fAQCategoryRepository.findAll();
        assertEquals(10, faqCategories.size());
        fAQCategoryRepository.deleteById(faqCategories.get(0).getId());

        // then
        List<FAQCategory> faqCategories2 = fAQCategoryRepository.findAll();
        assertEquals(9, faqCategories2.size());
        Optional<FAQCategory> findedFaqCategory = fAQCategoryRepository.findById(faqCategories.get(0).getId());
        assertFalse(findedFaqCategory.isPresent());
    }
}