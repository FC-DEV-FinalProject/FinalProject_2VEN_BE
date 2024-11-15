//package com.sysmatic2.finalbe.strategy.repository;
//
//import com.sysmatic2.finalbe.admin.entity.InvestmentAssetClassesEntity;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//public class InvestAssetClassesRepositoryTest {
//    @Autowired
//    InvestmentAssetClassesRepository iacRepo;
//
//    //Test Data 넣기
//    @BeforeEach
//    void setUp() {
//        iacRepo.deleteAll();
//
//        for(Integer i = 1; i <= 25; i++){
//            InvestmentAssetClassesEntity iac = new InvestmentAssetClassesEntity();
////            iac.setInvestmentAssetClassesId(i); //auto-increment 될텐데
//            iac.setOrder(i);
//            iac.setInvestmentAssetClassesName("Name" + i);
//            iac.setInvestmentAssetClassesIcon("Demo-File" + i);
//            if(i % 2 == 0){
//                iac.setIsActive("N");
//            }else{
//                iac.setIsActive("Y");
//            }
//
//            Long longId = Long.valueOf(i);
//            iac.setCreatedBy(longId);
//            iac.setCreatedAt(LocalDateTime.now());
//            iac.setModifiedBy(longId);
//            iac.setModifiedAt(LocalDateTime.now());
//
//            iacRepo.save(iac);
//        }
//    }
//
//    //1. 조회 - find()
//    //1-1. 모두 조회
//    @Test
//    public void findAll() {
//        List<InvestmentAssetClassesEntity> list = iacRepo.findAll();
//        System.out.println("list = " + list);
//
//        assertEquals(10, list.size());
//    }
//
//    //1-2. findById()
//    @Test
//    public void findById() {
//        Integer testNum = 4;
//        Optional<InvestmentAssetClassesEntity> optionalInvestmentAssetClasses = iacRepo.findById(testNum);
//        System.out.println("optionalInvestmentAssetClasses = " + optionalInvestmentAssetClasses);
//
//        InvestmentAssetClassesEntity iacTemp = optionalInvestmentAssetClasses.get();
//        System.out.println("iacTemp = " + iacTemp);
//
//        assertEquals(testNum, iacTemp.getInvestmentAssetClassesId());
//    }
//
//    //2. 삭제
//    @Test
//    public void delete(){
//        Integer testNum = 2;
//        //1)해당 테스트 넘버로 있는지 찾아본다.
//        Optional<InvestmentAssetClassesEntity> optionalInvestmentAssetClasses = iacRepo.findById(testNum);
//        assertEquals(testNum, optionalInvestmentAssetClasses.get().getInvestmentAssetClassesId());
//
//        //2)모두 찾은 데이터의 길이, 리스트 저장
//        List<InvestmentAssetClassesEntity> beforeList = iacRepo.findAll();
//        int listSize = beforeList.size();
//
//        //3)삭제수행
//        iacRepo.deleteById(testNum);
//
//        //4)해당 테스트 넘버의 객체 있는지 확인
//        Optional<InvestmentAssetClassesEntity> optionalInvestmentAssetClasses2 = iacRepo.findById(testNum);
//        System.out.println("optionalInvestmentAssetClasses2 = " + optionalInvestmentAssetClasses2);
//        assertTrue(optionalInvestmentAssetClasses2.isEmpty());
//
//        //5)모두 찾은 데이터의 길이, 리스트 저장
//        List<InvestmentAssetClassesEntity> afterList = iacRepo.findAll();
//        int listSize2 = afterList.size();
//
//        //6)비교
//        assertFalse(listSize2 == listSize);
//    }
//
//    //3. 수정
//    @Test
//    public void update(){
//        Integer testNum = 2;
//        List<InvestmentAssetClassesEntity> beforeList = iacRepo.findAll();
//        int listSize1 = beforeList.size();
//
//        //1) 해당 아이디의 객체를 가져온다.
//        Optional<InvestmentAssetClassesEntity> optionalInvestmentAssetClasses = iacRepo.findById(testNum);
//        assertEquals(testNum, optionalInvestmentAssetClasses.get().getInvestmentAssetClassesId());
//
//        //2) 해당 아이디의 객체의 제목을 변경한다.
//        InvestmentAssetClassesEntity inner = optionalInvestmentAssetClasses.get();
//        System.out.println("inner = " + inner);
//
//        String updateName = "testing"+testNum;
//        inner.setInvestmentAssetClassesName(updateName);
//        iacRepo.save(inner);
//
//        //3) 객체 가져와서 변경된 제목과 같은지 확인
//        Optional<InvestmentAssetClassesEntity> optionalInvestmentAssetClasses2 = iacRepo.findById(testNum);
//        InvestmentAssetClassesEntity inner2 = optionalInvestmentAssetClasses2.get();
//
//        assertEquals(inner2.getInvestmentAssetClassesName(), updateName);
//
//        //4) 전체 갯수 확인
//        List<InvestmentAssetClassesEntity> afterList = iacRepo.findAll();
//        int listSize2 = afterList.size();
//
//        assertTrue(listSize2 == listSize1);
//
//    }
//
//}
