package com.shoux_kream.checkout.repository;

import com.shoux_kream.checkout.entity.CheckOutEach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckOutEachRepository extends JpaRepository<CheckOutEach, Long> {
    CheckOutEach findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
