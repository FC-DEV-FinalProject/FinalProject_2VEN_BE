package com.sysmatic2.finalbe.member.repository;

import com.sysmatic2.finalbe.member.entity.MemberEntity;
import com.sysmatic2.finalbe.member.entity.MemberTermEntity;
import com.sysmatic2.finalbe.member.entity.TermEntity;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@Transactional
class MemberTermRepositoryTest {

    @Autowired
    private MemberTermRepository memberTermRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TermRepository termRepository;

    private MemberEntity testMember;
    private TermEntity testTerm;

    @BeforeEach
    void setUp() {
        memberTermRepository.deleteAll();
        memberRepository.deleteAll();
        termRepository.deleteAll();

        // 테스트용 MemberEntity 생성 및 저장
        testMember = new MemberEntity();
        testMember.setMemberGradeCode("MEMBER_ROLE_ADMIN");
        testMember.setMemberStatusCode("MEMBER_STATUS_ACTIVE");
        testMember.setEmail("test@example.com");
        testMember.setPassword("password");
        testMember.setNickname("testuser");
        testMember.setPhoneNumber("123-456-7890");
        testMember = memberRepository.save(testMember);

        // 테스트용 TermEntity 생성 및 저장
        testTerm = new TermEntity();
        testTerm.setTermTitle("Privacy Policy");
        testTerm.setTermContent("Content of privacy policy");
        testTerm.setIsRequired("Y");
        testTerm.setTargetMemberGradeCode("TARGET_TYPE_TRADER");
        testTerm.setIsActive("Y");
        testTerm = termRepository.save(testTerm);
    }

    @Test
    @DisplayName("MemberTerm Create 테스트")
    void testCreateMemberTerm() {
        MemberTermEntity memberTerm = new MemberTermEntity();
        memberTerm.setMember(testMember);
        memberTerm.setTerm(testTerm);
        memberTerm.setIsTermAgreed("Y");
        memberTerm.setAgreedAt(LocalDateTime.now());
        memberTerm = memberTermRepository.save(memberTerm);

        assertNotNull(memberTerm.getMemberTermId());
        assertEquals("Y", memberTerm.getIsTermAgreed());
    }

    @Test
    @DisplayName("MemberTerm Read 테스트")
    void testReadMemberTerm() {
        // 데이터 생성
        MemberTermEntity memberTerm = new MemberTermEntity();
        memberTerm.setMember(testMember);
        memberTerm.setTerm(testTerm);
        memberTerm.setIsTermAgreed("Y");
        memberTerm.setAgreedAt(LocalDateTime.now());
        memberTerm = memberTermRepository.save(memberTerm);

        // Read 테스트
        Optional<MemberTermEntity> foundMemberTerm = memberTermRepository.findById(memberTerm.getMemberTermId());
        assertTrue(foundMemberTerm.isPresent());
        assertEquals("Y", foundMemberTerm.get().getIsTermAgreed());
    }

    @Test
    @DisplayName("MemberTerm Update 테스트")
    void testUpdateMemberTerm() {
        // 데이터 생성
        MemberTermEntity memberTerm = new MemberTermEntity();
        memberTerm.setMember(testMember);
        memberTerm.setTerm(testTerm);
        memberTerm.setIsTermAgreed("Y");
        memberTerm = memberTermRepository.save(memberTerm);

        // Update 테스트
        memberTerm.setIsTermAgreed("N");
        memberTermRepository.save(memberTerm);
        Optional<MemberTermEntity> updatedMemberTerm = memberTermRepository.findById(memberTerm.getMemberTermId());
        assertTrue(updatedMemberTerm.isPresent());
        assertEquals("N", updatedMemberTerm.get().getIsTermAgreed());
    }

    @Test
    @DisplayName("MemberTerm Delete 테스트")
    void testDeleteMemberTerm() {
        // 데이터 생성
        MemberTermEntity memberTerm = new MemberTermEntity();
        memberTerm.setMember(testMember);
        memberTerm.setTerm(testTerm);
        memberTerm.setIsTermAgreed("Y");
        memberTerm = memberTermRepository.save(memberTerm);

        // Delete 테스트
        memberTermRepository.delete(memberTerm);
        assertFalse(memberTermRepository.findById(memberTerm.getMemberTermId()).isPresent());
    }

    @Test
    @DisplayName("count()와 deleteAll() 테스트")
    void testCountAndDeleteAll() {
        // 데이터 삽입
        MemberTermEntity memberTerm1 = new MemberTermEntity();
        memberTerm1.setMember(testMember);
        memberTerm1.setTerm(testTerm);
        memberTerm1.setIsTermAgreed("Y");
        memberTermRepository.save(memberTerm1);

        MemberTermEntity memberTerm2 = new MemberTermEntity();
        memberTerm2.setMember(testMember);
        memberTerm2.setTerm(testTerm);
        memberTerm2.setIsTermAgreed("N");
        memberTermRepository.save(memberTerm2);

        // count() 확인
        assertEquals(2, memberTermRepository.count());

        // deleteAll() 호출 및 확인
        memberTermRepository.deleteAll();
        assertEquals(0, memberTermRepository.count());
    }

    @Test
    @DisplayName("약관으로 MemberTerm 리스트 조회")
    void testFindByTerm() {
        // 데이터 삽입
        MemberTermEntity memberTerm = new MemberTermEntity();
        memberTerm.setMember(testMember);
        memberTerm.setTerm(testTerm);
        memberTerm.setIsTermAgreed("Y");
        memberTermRepository.save(memberTerm);

        List<MemberTermEntity> terms = memberTermRepository.findByTerm(testTerm);
        assertFalse(terms.isEmpty());
        assertEquals(1, terms.size());
        assertEquals("Y", terms.get(0).getIsTermAgreed());
    }

    @Test
    @DisplayName("회원으로 MemberTerm 리스트 조회")
    void testFindByMember() {
        // 데이터 삽입
        MemberTermEntity memberTerm = new MemberTermEntity();
        memberTerm.setMember(testMember);
        memberTerm.setTerm(testTerm);
        memberTerm.setIsTermAgreed("Y");
        memberTermRepository.save(memberTerm);

        List<MemberTermEntity> memberTerms = memberTermRepository.findByMember(testMember);
        assertFalse(memberTerms.isEmpty());
        assertEquals(1, memberTerms.size());
        assertEquals("Y", memberTerms.get(0).getIsTermAgreed());
    }

    @Test
    @DisplayName("예외 상황: 필수 필드 누락으로 인한 DataIntegrityViolationException")
    void testExceptionWhenRequiredFieldIsNull() {
        MemberTermEntity memberTerm = new MemberTermEntity();
        memberTerm.setMember(testMember);
        memberTerm.setTerm(testTerm);
        // isTermAgreed를 설정하지 않음 (nullable = false)

        assertThrows(DataIntegrityViolationException.class, () -> {
            memberTermRepository.saveAndFlush(memberTerm);
        });
    }

    @Test
    @DisplayName("예외 상황: Member가 null인 경우 DataIntegrityViolationException 발생")
    void testExceptionWhenMemberIsNull() {
        MemberTermEntity memberTerm = new MemberTermEntity();
        memberTerm.setTerm(testTerm);
        memberTerm.setIsTermAgreed("Y");
        memberTerm.setAgreedAt(LocalDateTime.now());

        assertThrows(DataIntegrityViolationException.class, () -> {
            memberTermRepository.saveAndFlush(memberTerm);
        });
    }

    @Test
    @DisplayName("예외 상황: Term이 null인 경우 DataIntegrityViolationException 발생")
    void testExceptionWhenTermIsNull() {
        MemberTermEntity memberTerm = new MemberTermEntity();
        memberTerm.setMember(testMember);
        memberTerm.setIsTermAgreed("Y");
        memberTerm.setAgreedAt(LocalDateTime.now());

        assertThrows(DataIntegrityViolationException.class, () -> {
            memberTermRepository.saveAndFlush(memberTerm);
        });
    }

    @Test
    @DisplayName("예외 상황: isTermAgreed 필드에 잘못된 값이 들어간 경우 ConstraintViolationException 발생")
    void testExceptionWhenIsTermAgreedIsInvalid() {
        MemberTermEntity memberTerm = new MemberTermEntity();
        memberTerm.setMember(testMember);
        memberTerm.setTerm(testTerm);
        memberTerm.setIsTermAgreed("Invalid");  // 잘못된 값 설정
        memberTerm.setAgreedAt(LocalDateTime.now());

        assertThrows(ConstraintViolationException.class, () -> {
            memberTermRepository.saveAndFlush(memberTerm);
        });
    }
}