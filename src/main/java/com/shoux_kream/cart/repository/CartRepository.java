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
    void deleteByItemId(Long itemId);
    void deleteById(Long cartId);
    List<Cart> findByUserId(Long userId);
    Optional<Cart> findByItemAndUser(Item item, User user);
    List<Cart> findBySelectedTrueAndUserId(Long userId);

    // 고객이 장바구니로 다시 돌아와 상품을 변경할 경우 selected를 false로 변경
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Cart c SET c.selected = false WHERE c.user.id = :userId AND c.selected = true AND c.id NOT IN :cartIds")
    void deselectOldItems(@Param("userId") Long userId, @Param("cartIds") List<Long> cartIds);

    // Cart에서 구매하기로 선택한 상품들 확인
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Cart c SET c.selected = true WHERE c.user.id = :userId AND c.id IN :cartIds")
    void updateCartSelected(@Param("userId") Long userId, @Param("cartIds") List<Long> cartIds);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Cart c WHERE c.user.id = :userId AND c.id IN :cartIds")
    void deleteCartsSelected(@Param("userId") Long userId, @Param("cartIds") List<Long> cartIds);
}
