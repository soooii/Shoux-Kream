package com.shoux_kream.sale.repository;

import com.shoux_kream.sale.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findByUserId(Long userId); // 사용자가 등록한 상품 목록들 조회
}
