package com.shoux_kream.checkout.repository;

import com.shoux_kream.checkout.entity.CheckOut;
import com.shoux_kream.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CheckOutRepository extends JpaRepository<CheckOut, Long> {
    List<CheckOut> findByUserId(Long userId);

    CheckOut findByUserAndId(User user, Long detailId);

    List<CheckOut> findByAddressId(Long addressId);
}
