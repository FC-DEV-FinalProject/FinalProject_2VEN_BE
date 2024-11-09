package com.sysmatic2.finalbe.strategy.repository;

import com.sysmatic2.finalbe.strategy.entity.InvestmentAssetClassesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvestmentAssetClassesRepository extends JpaRepository<InvestmentAssetClassesEntity, Integer> {
    //Order 존재하는지 확인
    boolean existsByOrder(Integer order);

    //order 최대값 구하기
    @Query("SELECT MAX(i.order) FROM InvestmentAssetClassesEntity i")
    Optional<Integer> getMaxOrder();
}
