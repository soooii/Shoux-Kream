package com.shoux_kream.cart.controller;

import com.shoux_kream.cart.dto.CartRequestDto;
import com.shoux_kream.cart.dto.CartResponseDto;
import com.shoux_kream.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartApiContorller {

    private final CartService cartService;

    // 장바구니 담기
    @PostMapping("/add/{itemId}")
    public ResponseEntity<Long> addCart(@Valid @RequestBody CartRequestDto cartRequestDto, @PathVariable("itemId") Long itemId) {

        Long cartId = cartService.addCart(cartRequestDto, itemId);

        return ResponseEntity.ok()
                .body(cartId);
    }

    // 장바구니 조회
    @GetMapping("/summary/{userId}")
    public ResponseEntity<List<CartResponseDto>> allCarts(@PathVariable("userId") Long userId) {
        List<CartResponseDto> carts = cartService.allCarts(userId);

        return ResponseEntity.ok()
                .body(carts);
    }

    // 장바구니 수정 -> 장바구니에서 수량 및 옵션 수정으로 사용
    @PatchMapping("/edit/{cartId}")
    public ResponseEntity<CartResponseDto> updateCart(@Valid @RequestBody CartRequestDto cartRequestDto, @PathVariable("cartId") Long cartId) {
        CartResponseDto cart = cartService.updateCart(cartRequestDto, cartId);

        return ResponseEntity.ok()
                .body(cart);

    }
    
    // 장바구니 삭제
    // 장바구니 일괄 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCarts(@RequestBody List<Long> cartIds) {
        cartService.deleteAllCarts(cartIds);

        return ResponseEntity.ok()
                .build();
    }
    
    // 장바구니 개별 삭제
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("cartId") Long cartId) {
        cartService.deleteCart(cartId);

        return ResponseEntity.ok()
                .build();
    }
}
