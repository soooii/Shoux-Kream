package com.shoux_kream.cart.controller;

import com.shoux_kream.cart.dto.CartRequestDto;
import com.shoux_kream.cart.dto.CartResponseDto;
import com.shoux_kream.cart.service.CartService;
import com.shoux_kream.user.dto.response.UserResponse;
import com.shoux_kream.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartApiController {

    private final CartService cartService;
    private final UserService userService;

    // 장바구니 담기
    @PostMapping("/add/{itemId}")
    public ResponseEntity addCart(@Valid @RequestBody CartRequestDto cartRequestDto, @PathVariable("itemId") Long itemId) {
        UserResponse userResponse = userService.getUser();

        Long cartId = cartService.addCart(userResponse.getUserId(), cartRequestDto, itemId);

        return ResponseEntity.ok()
                .body(cartId);
    }

    // 장바구니 조회
    @GetMapping("/summary")
    public ResponseEntity<List<CartResponseDto>> allCarts() {
        UserResponse userResponse = userService.getUser();
        List<CartResponseDto> carts = cartService.allCarts(userResponse.getUserId());

        return ResponseEntity.ok(carts);
    }

    // 선택한 cart의 값 조회
    @GetMapping("/selected")
    //principal user는 userDetail의 user!
    public ResponseEntity<List<CartResponseDto>> getSelectedCarts(@AuthenticationPrincipal User principal){
        // principal의 unique값인 이메일로 특정
        //String email = principal.getUsername();
        //UserResponse userResponse = userService.getUser(email);
        UserResponse userResponse = userService.getUser();
        List<CartResponseDto> selectedCarts = cartService.selectedCarts(userResponse.getUserId());

        return ResponseEntity.ok(selectedCarts);
    }
    
    // 구매하기 위해 선택한 상품 확인
    @PatchMapping("/selected")
    public ResponseEntity updateCartSelected(@RequestBody List<Long> cartIds, @AuthenticationPrincipal User principal) {
        // 체크된 cart Id 값만 true로 변경
        //String email = principal.getUsername();
        //UserResponse userResponse = userService.getUser(email);
        UserResponse userResponse = userService.getUser();
        cartService.updateCartSelected(cartIds, userResponse.getUserId());

        return ResponseEntity.ok()
                .build();
    }

    // 장바구니 수정 -> 장바구니에서 수량 및 옵션 수정으로 사용
    @PatchMapping("/edit/{cartId}")
    public ResponseEntity updateCart(@Valid @RequestBody CartRequestDto cartRequestDto, @PathVariable("cartId") Long cartId, @AuthenticationPrincipal User principal) {
        String email = principal.getUsername();

        if(!cartService.validateCartItem(cartId, email)){
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        CartResponseDto cart = cartService.updateCart(cartRequestDto, cartId);

        return ResponseEntity.ok(cart);

    }
    
    // 장바구니 삭제
    // 장바구니 일괄 삭제
    @DeleteMapping("/")
    public ResponseEntity deleteCarts(@RequestBody List<Long> cartIds, @AuthenticationPrincipal User principal) {
        //String email = principal.getUsername();
        //UserResponse userResponse = userService.getUser(email);
        UserResponse userResponse = userService.getUser();
        cartService.deleteCarts(cartIds, userResponse.getUserId());

        return ResponseEntity.ok()
                .build();
    }
    
    // 장바구니 개별 삭제
    @DeleteMapping("/{cartId}")
    public ResponseEntity deleteCart(@PathVariable("cartId") Long cartId, @AuthenticationPrincipal User principal) {
        String email = principal.getUsername();

        if(!cartService.validateCartItem(cartId, email)){
            return new ResponseEntity<String>("삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.deleteCart(cartId);

        return ResponseEntity.ok(cartId);
    }
}
