package com.shoux_kream.cart.service;

import com.shoux_kream.cart.dto.CartRequestDto;
import com.shoux_kream.cart.dto.CartResponseDto;
import com.shoux_kream.cart.entity.Cart;
import com.shoux_kream.cart.repository.CartRepository;
import com.shoux_kream.item.entity.Item;
import com.shoux_kream.item.repository.ItemRepository;
import com.shoux_kream.user.entity.User;
import com.shoux_kream.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
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

        if (cart != null) { // 객체가 이미 있을 경우 총 수량 변경
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

    // 장바구니 수정 -> 수량 수정은 추가에서 가능
//    @Transactional
//    public CartResponseDto updateCart(CartRequestDto cartRequestDto, Long cartId) {
//        User user = userRepository.findById(cartRequestDto.getUserId())
//                .orElseThrow(() -> new NoSuchElementException("User not found"));
//
//        Item item = itemRepository.findById(cartRequestDto.getItemId())
//                .orElseThrow(() -> new NoSuchElementException("Item not found. item id : " + cartRequestDto.getItemId()));
//
//        Cart cart = cartRepository.findByItemAndUser(item, user).orElse(null);
//
//        if (cart != null) {
//            cart.setQuantity(cart.getQuantity() + cartRequestDto.getQuantity());
//            cartRepository.save(cart);
//        }
//        return new CartResponseDto(cart);
//    }

    // 장바구니 삭제
    // 장바구니 일괄 삭제
    @Transactional
    public void deleteAllCarts(Long userId) {
        cartRepository.deleteAllByUserId(userId);
    }

    // 장바구니 개별 삭제
    @Transactional
    public void deleteCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }

}
