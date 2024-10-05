package com.shoux_kream.cart.repository;

import com.shoux_kream.cart.entity.Cart;
import com.shoux_kream.item.entity.Item;
import com.shoux_kream.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    void deleteById(Long cartId);
    List<Cart> findByUserId(Long userId);
    Optional<Cart> findByItemAndUser(Item item, User user);
}
