package com.shoux_kream.cart.repository;

import com.shoux_kream.cart.entity.Cart;
import com.shoux_kream.item.entity.Item;
import com.shoux_kream.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    void deleteById(Long cartId);
    List<Cart> findByUserId(Long userId);
    Optional<Cart> findByItemAndUser(Item item, User user);
    List<Cart> findBySelectedTrueAndUserId(Long userId);
    
    // Cart에서 구매하기로 선택한 상품들 확인
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Cart c SET c.selected = true WHERE c.user.id = :userId AND c.id IN :cartIds")
    void updateCartSelected(@Param("userId") Long userId, @Param("cartIds") List<Long> cartIds);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Cart c WHERE c.user.id = :userId AND c.id IN :cartIds")
    void deleteCartsSelected(@Param("userId") Long userId, @Param("cartIds") List<Long> cartIds);

}
