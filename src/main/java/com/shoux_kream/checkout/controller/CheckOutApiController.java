package com.shoux_kream.checkout.controller;

import com.shoux_kream.cart.dto.CartResponseDto;
import com.shoux_kream.cart.service.CartService;
import com.shoux_kream.checkout.dto.CheckOutItemRequestDto;
import com.shoux_kream.checkout.dto.CheckOutRequestDto;
import com.shoux_kream.checkout.dto.CheckOutResponseDto;
import com.shoux_kream.checkout.entity.CheckOut;
import com.shoux_kream.checkout.service.CheckOutService;
import com.shoux_kream.user.dto.response.UserAddressDto;
import com.shoux_kream.user.dto.response.UserResponse;
import org.springframework.security.core.userdetails.User;
import com.shoux_kream.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/api/checkout")
@RequestMapping("/api")
@RequiredArgsConstructor
public class CheckOutApiController {
    
    private final CheckOutService checkOutService;
    private final UserService userService;

    /*
    //TODO 최근주소지 정보에 등록하는 API=> checkout에 통합
    await Api.post("/api/user/recentdelivery", data);
     */

    //TODO 검증 필요!
    @PostMapping("/checkout") //TODO userId로 체크아웃 정보를 저장함, 저장한 entity를 id값을 포함해 반환
    public ResponseEntity<CheckOutResponseDto> processCheckOut(@AuthenticationPrincipal User principal,@RequestBody CheckOutRequestDto checkOutRequestDto) {
        String email = principal.getUsername();
        UserResponse userResponse = userService.getUser(email);
        CheckOutResponseDto checkOutResponseDto = checkOutService.createCheckout(userResponse.getUserId(), checkOutRequestDto);
        return ResponseEntity.ok(checkOutResponseDto);
    }
    @GetMapping("/checkouts") //userEmail로 checkout 정보 받기
    public ResponseEntity<List<CheckOut>> getCheckOuts(@AuthenticationPrincipal User principal) {
        String email = principal.getUsername();
        UserResponse userResponse = userService.getUser(email);
        List<CheckOut> checkOuts = checkOutService.getCheckOuts(userResponse.getUserId());
        return ResponseEntity.ok(checkOuts);
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

    //TODO 검증 필요!
    @PostMapping("/checkoutitem") //checkoutItem의 checkoutId로 checkout과 1:N연관관계를 맺음
    public ResponseEntity addCheckoutItem(@RequestBody CheckOutItemRequestDto checkOutItemRequestDto) {
        try {
            checkOutService.addCheckOutItem(checkOutItemRequestDto);
            return ResponseEntity.ok().body(checkOutItemRequestDto.getCheckOutId());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("주문 아이템 오류" + e.getMessage());
        }
    }
}
