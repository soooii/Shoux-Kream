package com.shoux_kream.checkout.controller;

import com.shoux_kream.cart.dto.CartResponseDto;
import com.shoux_kream.cart.service.CartService;
import com.shoux_kream.checkout.dto.CheckOutItemRequestDto;
import com.shoux_kream.checkout.dto.CheckOutRequestDto;
import com.shoux_kream.checkout.dto.CheckOutResponseDto;
import com.shoux_kream.checkout.dto.UserDeliveryInfoRequestDto;
import com.shoux_kream.checkout.service.CheckOutService;
import com.shoux_kream.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
//@RequestMapping("/api/checkout")
@RequestMapping("/api")
@RequiredArgsConstructor
public class CheckOutApiController {
    
    private final CheckOutService checkOutService;
    private final UserService userService;
    private final CartService cartService;

//    //TODO 결제 요약 정보 삽입
//    @GetMapping("/checkout/summary")
//    public ResponseEntity<Map<String, Object>> getcheckOutSummary() {
//        try {
//            Map<String, Object> checkOutSummary = checkOutService.getcheckOutSummary();
//
//            if (checkOutSummary == null || checkOutSummary.get("ids").isEmpty()) {
//                return ResponseEntity.badRequest().body(Map.of("message", "구매할 제품이 없습니다."));
//            }
//
//            return ResponseEntity.ok(checkOutSummary);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(Map.of("message", e.getMessage()));
//        }
//    }

    // 전체 주문 등록
    @PostMapping("/checkout")
    public ResponseEntity<String> processCheckout(@RequestBody CheckOutRequestDto checkOutRequestDto) {
        try {
            Long checkoutId = checkOutService.createCheckout(checkOutRequestDto);
            return ResponseEntity.ok("결제 및 주문이 완료되었습니다. Checkout ID: " + checkoutId);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("결제 중 문제가 발생했습니다: " + e.getMessage());
        }
    }

    // 제품별 주문 아이템 등록
    @PostMapping("/checkoutitem")
    public ResponseEntity<String> addCheckoutItem(@RequestBody CheckOutItemRequestDto checkOutItemRequestDto) {
        try {
            checkOutService.addCheckOutItem(checkOutItemRequestDto);
            return ResponseEntity.ok("주문 아이템이 정상적으로 등록되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("주문 아이템 등록 중 문제가 발생했습니다: " + e.getMessage());
        }
    }

    // 배송지 정보 등록
    @PostMapping("/user/deliveryinfo")
    public ResponseEntity<String> saveUserDeliveryInfo(@RequestBody UserDeliveryInfoRequestDto deliveryInfo) {
        try {
//            userService.updateDeliveryInfo(deliveryInfo);
            return ResponseEntity.ok("배송지 정보가 정상적으로 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("배송지 정보 저장 중 문제가 발생했습니다: " + e.getMessage());
        }
    }

    // TODO
    // 장바구니에서 제품 제거 (deleteFromDb 대체)
//    @DeleteMapping("/cart/{productId}")
//    public ResponseEntity<String> removeFromCart(@PathVariable Long itemId) {
//        try {
//            cartService.deleteCart(itemId);
//            return ResponseEntity.ok("제품이 장바구니에서 제거되었습니다.");
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("장바구니에서 제품 제거 중 문제가 발생했습니다: " + e.getMessage());
//        }
//    }

    // 장바구니 또는 체크아웃 데이터 가져오기 (getFromDb 대체)
    @GetMapping("/cart/{id}")
    public ResponseEntity<List<CartResponseDto>> getCartData(@PathVariable Long id) {
        try {
            //TODO Map<String, Object> json에 담을거면 이렇게 만들자
            List<CartResponseDto> data = cartService.allCarts(id);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    //TODO
//    // 장바구니 또는 체크아웃 데이터 업데이트 (putToDb 대체)
//    @PutMapping("/checkout/summary")
//    public ResponseEntity<String> updateCheckOutSummary(@RequestBody checkOutSummaryUpdateRequest updateRequest) {
//        try {
//            checkOutService.updateCheckOutSummary(updateRequest);
//            return ResponseEntity.ok("체크아웃 요약이 업데이트되었습니다.");
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("체크아웃 요약 업데이트 중 문제가 발생했습니다: " + e.getMessage());
//        }
//    }
}
