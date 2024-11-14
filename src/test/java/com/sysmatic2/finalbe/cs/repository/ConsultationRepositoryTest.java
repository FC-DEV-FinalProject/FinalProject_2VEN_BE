package com.sysmatic2.finalbe.cs.repository;

import com.sysmatic2.finalbe.cs.entity.Consultation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConsultationRepositoryTest {

  @Autowired
  private ConsultationRepository consultationRepository;

  @BeforeEach
  void setUp() {
    consultationRepository.deleteAll();
    Consultation consultation1 = Consultation.builder()
            .senderId(1L)
            .receiverId(2L)
            .strategyId(1L)
            .title("First Consultation")
            .content("Content for first consultation")
            .sentAt(LocalDateTime.now())
            .isRead(false)
            .isAnswered(false)
            .build();

    Consultation consultation2 = Consultation.builder()
            .senderId(2L)
            .receiverId(1L)
            .strategyId(2L)
            .title("Second Consultation")
            .content("Content for second consultation")
            .sentAt(LocalDateTime.now())
            .isRead(true)
            .isAnswered(true)
            .build();

    consultationRepository.save(consultation1);
    consultationRepository.save(consultation2);
  }

  @Test
  void testFindBySenderIdOrReceiverId() {
    List<Consultation> consultations = consultationRepository.findBySenderIdOrReceiverId(1L, 1L);

    assertNotNull(consultations);
    assertEquals(2, consultations.size());
    assertTrue(consultations.stream().anyMatch(c -> c.getTitle().equals("First Consultation")));
    assertTrue(consultations.stream().anyMatch(c -> c.getTitle().equals("Second Consultation")));
  }

  @Test
  void testFindByStrategyId() {
    List<Consultation> consultations = consultationRepository.findByStrategyId(1L);

    assertNotNull(consultations);
    assertEquals(1, consultations.size());
    assertEquals("First Consultation", consultations.get(0).getTitle());
  }
}
