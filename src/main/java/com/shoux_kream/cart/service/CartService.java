package com.shoux_kream.cart.service;

import com.shoux_kream.cart.dto.CartRequestDto;
import com.shoux_kream.cart.dto.CartResponseDto;
import com.shoux_kream.cart.entity.Cart;
import com.shoux_kream.cart.repository.CartRepository;
import com.shoux_kream.exception.CartQuantityOutOfRangeException;
import com.shoux_kream.item.entity.Item;
import com.shoux_kream.item.repository.ItemRepository;
import com.shoux_kream.user.entity.User;
import com.shoux_kream.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    // 장바구니 담기
    @Transactional
    public Long addCart(CartRequestDto cartRequestDto, Long itemId) {
        User user = userRepository.findById(cartRequestDto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item not found. item id : " + itemId));

        Cart cart = cartRepository.findByItemAndUser(item, user).orElse(null);

        if (cart != null) { // 객체가 이미 있을 경우 총 수량 변경(이미 장바구니에 담은 상품인 경우 수량 증가)
            cart.setQuantity(cart.getQuantity() + cartRequestDto.getQuantity());
            cartRepository.save(cart);
            return cart.getId();
        } else {
            Cart newCart = new Cart(user, item, cartRequestDto.getQuantity());

            cartRepository.save(newCart);
            return newCart.getId();
        }
    }

    // 장바구니 조회
    @Transactional(readOnly = true)
    public List<CartResponseDto> allCarts(Long userId) {
        List<Cart> carts = cartRepository.findByUserId(userId);

        return carts.stream()
                .map(cart -> new CartResponseDto(cart))
                .collect(Collectors.toList());
    }

    // 장바구니 수정 -> 장바구니에서 수량 및 옵션 수정으로 사용
    @Transactional
    public CartResponseDto updateCart(CartRequestDto cartRequestDto, Long cartId) {
        // cart Id로 장바구니 물품 조회
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new NoSuchElementException("Cart not found"));

        // 수량이 0이상인지 확인 -> 추후 재고이하만큼 범위를 추가할 것
        if (cartRequestDto.getQuantity() < 1) {
            throw new CartQuantityOutOfRangeException("최소 1개 이상의 수량을 담아주세요.");
        }

        cart.updateQuantity(cartRequestDto.getQuantity());

        return new CartResponseDto(cart);
    }


    // 장바구니 삭제
    // 장바구니 일괄 삭제
    @Transactional
    public void deleteAllCarts(List<Long> cartIds) {
        for (Long cartId : cartIds) {
            cartRepository.deleteById(cartId);
        }
    }

    // 장바구니 개별 삭제
    @Transactional
    public void deleteCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }

}
