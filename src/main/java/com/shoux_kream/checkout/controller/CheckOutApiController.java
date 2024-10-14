package com.shoux_kream.checkout.controller;

import com.shoux_kream.checkout.dto.CheckOutItemRequestDto;
import com.shoux_kream.checkout.dto.CheckOutRequestDto;
import com.shoux_kream.checkout.dto.CheckOutResponseDto;
import com.shoux_kream.checkout.service.CheckOutService;
import com.shoux_kream.user.dto.response.UserAddressDto;
import com.shoux_kream.user.dto.response.UserResponse;
import org.springframework.security.core.userdetails.User;
import com.shoux_kream.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//TODO 값 검증은 CartRequestDto 에서 validator 통해서 체크
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


    @PostMapping("/checkout") //TODO userId로 체크아웃 정보를 저장함, 저장한 entity를 id값을 포함해 반환
    public ResponseEntity<CheckOutResponseDto> processCheckOut(@AuthenticationPrincipal User principal,@RequestBody CheckOutRequestDto checkOutRequestDto) {
        String email = principal.getUsername();
        UserResponse userResponse = userService.getUser(email);
        CheckOutResponseDto checkOutResponseDto = checkOutService.createCheckout(userResponse.getUserId(), checkOutRequestDto);
        // TODO 201 created(URI) 전환
        return ResponseEntity.ok(checkOutResponseDto);
    }
    //TODO 검증 필요! => controller x , 검증 비즈니스 로직은 서비스에서!
    @GetMapping("/checkout") //userEmail로 checkout 정보 받기
    public ResponseEntity<List<CheckOutResponseDto>> getCheckOuts(@AuthenticationPrincipal User principal) {
        // post맨에선 에러가 나옴 => 헤더 토큰정보 문제
        //  private Long id;
        //    private String summaryTitle;
        //    private int totalPrice;
        // 이 정보만 필요.
        String email = principal.getUsername();
        UserResponse userResponse = userService.getUser(email);
        List<CheckOutResponseDto> checkOuts = checkOutService.getCheckOuts(userResponse.getUserId());
        return ResponseEntity.ok(checkOuts);
    }
    @GetMapping("/checkout/{detailId}") //TODO param에 정보를 받을 checkoutdetail 번호를 입력받아야함, user의 토큰 권한도 확인
    public ResponseEntity<CheckOutResponseDto> getCheckOut(@PathVariable("detailId") Long detailId) {
        //TODO 권한 확인 필요
        CheckOutResponseDto checkOutResponseDto = checkOutService.getCheckOutDetail(detailId);
        return ResponseEntity.ok(checkOutResponseDto);
    }
    @PatchMapping("/checkout/{detailId}") //TODO param에 수정할 checkoutdetail 번호를 입력받아야함, user의 토큰 권한도 확인, 배송지 정보 수정
    public ResponseEntity<CheckOutResponseDto> updateCheckOutAddress(@PathVariable("detailId") Long detailId, @RequestBody UserAddressDto userAddressDto) {
        //TODO 권한 확인 필요
        //TODO 잘 되는데 주소 id번호까지 같이 바뀌어버림; CheckOut을 address랑 분리해야함!
        //service에서 email로 userid를 체크해서 update함
        CheckOutResponseDto checkOutResponseDto = checkOutService.updateCheckOut(detailId, userAddressDto);
        return ResponseEntity.ok(checkOutResponseDto);
    }
    @DeleteMapping("/checkout/{detailId}") //TODO param에 checkout 번호를 입력받아야함, user의 토큰 권한도 확인
    public ResponseEntity<Long> deleteCheckOut(@AuthenticationPrincipal User principal, @PathVariable("detailId") Long detailId) {
        Long deletedId = checkOutService.deleteCheckOut(principal.getUsername(), detailId);
        return ResponseEntity.ok(deletedId);
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
