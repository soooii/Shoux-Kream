package com.shoux_kream.checkout.repository;

import com.shoux_kream.checkout.entity.CheckOutItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckOutItemRepository extends JpaRepository<CheckOutItem, Long> {
}