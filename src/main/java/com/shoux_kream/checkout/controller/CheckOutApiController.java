package com.shoux_kream.checkout.controller;

import com.shoux_kream.cart.dto.CartResponseDto;
import com.shoux_kream.cart.service.CartService;
import com.shoux_kream.checkout.dto.CheckOutItemRequestDto;
import com.shoux_kream.checkout.dto.CheckOutRequestDto;
import com.shoux_kream.checkout.dto.CheckOutResponseDto;
import com.shoux_kream.checkout.dto.UserDeliveryInfoRequestDto;
import com.shoux_kream.checkout.service.CheckOutService;
import com.shoux_kream.user.entity.User;
import com.shoux_kream.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
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

// TODO cart selected api 구현
    @GetMapping("/cart/selected")
    public ResponseEntity<String> getCheckOutSummary(@AuthenticationPrincipal User principal){
        // TODO stream처리로 각자 titles, quantities, selectedid 배열 반환
        return ResponseEntity.ok("void");
    }

    @GetMapping("/users/userAddress")
    public ResponseEntity<String> getUserAddress(@AuthenticationPrincipal User principal){
        //TODO  recipientName, recipientPhone, postalCode, address1, address2 user에서 얻어오기
        return ResponseEntity.ok("void");
    }
    // 전체 주문 등록
    /*
       const checkoutData = await Api.post("/api/checkout", {
      summaryTitle,
      totalPrice,
      address,
      //배송요청사항
      request,
    });

     */
    @PostMapping("/checkout") //TODO userId로 체크아웃 정보를 저장함, 저장한 entity를 id값을 포함해 반환
    public ResponseEntity<String> processCheckOut(@AuthenticationPrincipal User principal,@RequestBody CheckOutRequestDto checkOutRequestDto) {
        return ResponseEntity.ok("void");
    }
    @GetMapping("/checkouts") //userId로 체크아웃 정보를 전부 받음
    public ResponseEntity<String> getCheckOuts(@AuthenticationPrincipal User principal) {
        return ResponseEntity.ok("void");
    }
    @GetMapping("/checkoutdetail") //TODO param에 정보를 받을 checkoutdetail 번호를 입력받아야함, user의 토큰 권한도 확인
    public ResponseEntity<String> getCheckOut(@RequestParam Long checkoutId) {
        return ResponseEntity.ok("void");
    }
    @PatchMapping("/checkoutdetail") //TODO param에 수정할 checkoutdetail 번호를 입력받아야함, user의 토큰 권한도 확인
    public ResponseEntity<String> updateCheckOut(@RequestParam Long checkoutId) {
        return ResponseEntity.ok("void");
    }
    @DeleteMapping("/checkout") //TODO param에 checkout 번호를 입력받아야함, user의 토큰 권한도 확인
    public ResponseEntity<String> deleteCheckOut(@RequestParam Long checkoutId) {
        return ResponseEntity.ok("void");
    }

    // 제품별 주문 아이템 등록
    /*
    await Api.post("/api/checkoutitem", {
        checkoutId,
        itemId,
        quantity,
        totalPrice,
      });
     */
    @PostMapping("/checkoutitem") //checkoutItem의 checkoutId로 checkout과 1:N연관관계를 맺음
    public ResponseEntity<String> addCheckoutItem(@RequestBody CheckOutItemRequestDto checkOutItemRequestDto) {
        try {
            checkOutService.addCheckOutItem(checkOutItemRequestDto);
            return ResponseEntity.ok("주문 아이템이 정상적으로 등록되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("주문 아이템 등록 중 문제가 발생했습니다: " + e.getMessage());
        }
    }

    // 장바구니 또는 체크아웃 데이터 가져오기 (getFromDb 대체)
    /*
    // 입력된 배송지정보를 유저db에 등록함
    const data = {
      phoneNumber: receiverPhoneNumber,
      address: {
        postalCode,
        address1,
        address2,
      },
    };
    //TODO 최근주소지 정보에 등록하는 API로 변경 필요
    await Api.post("/api/user/recentdelivery", data);
     */
    @PostMapping("/user/recentdelivery")
    public ResponseEntity<String> getCartData(@AuthenticationPrincipal User principal) {
        return ResponseEntity.ok("void");
    }
}
