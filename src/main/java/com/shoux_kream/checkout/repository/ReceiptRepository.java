package com.shoux_kream.checkout.repository;

import com.shoux_kream.checkout.entity.CheckOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReceiptRepository extends JpaRepository<CheckOut, Long> {
    Optional<CheckOut> findById(Long id);
}