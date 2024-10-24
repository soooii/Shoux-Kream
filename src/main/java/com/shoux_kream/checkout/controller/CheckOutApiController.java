package com.shoux_kream.checkout.controller;

import com.shoux_kream.checkout.dto.*;
import com.shoux_kream.checkout.service.CheckOutService;
import com.shoux_kream.user.controller.JwtController;
import com.shoux_kream.user.dto.response.UserAddressDto;
import com.shoux_kream.user.dto.response.UserResponse;
import jakarta.websocket.server.PathParam;
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
    //TODO checkout postmapping => 단건구매 내용을 저장할 checkout proxy entity => 단건구매 누를때마다 porxy 초기화
    // TODO 재고뺴는 로직이 하나도 없음

    @PostMapping("/checkout") //TODO userId로 체크아웃 정보를 저장함, 저장한 entity를 id값을 포함해 반환
    public ResponseEntity<CheckOutResponseDto> processCheckOut(@AuthenticationPrincipal User principal,@RequestBody CheckOutRequestDto checkOutRequestDto) {
        UserResponse userResponse = userService.getUser();
        CheckOutResponseDto checkOutResponseDto = checkOutService.createCheckout(userResponse.getUserId(), checkOutRequestDto);
        // TODO 201 created(URI) 전환

        //isEach true => cart정보를 checkoutporxy에서 가져옴
        return ResponseEntity.ok(checkOutResponseDto);
    }
    //TODO 검증 필요! => controller x , 검증 비즈니스 로직은 서비스에서!
    @GetMapping("/checkout") //userEmail로 checkout 정보 받기
    public ResponseEntity<List<CheckOutResponseDto>> getCheckOuts(@AuthenticationPrincipal User principal) {
        UserResponse userResponse = userService.getUser();
        List<CheckOutResponseDto> checkOuts = checkOutService.getCheckOuts(userResponse.getUserId());
        return ResponseEntity.ok(checkOuts);
    }
    @GetMapping("/checkout/{detailId}") //TODO param에 정보를 받을 checkoutdetail 번호를 입력받아야함, user의 토큰 권한도 확인
    public ResponseEntity<CheckOutResponseDto> getCheckOut(@PathVariable("detailId") Long detailId) {
        CheckOutResponseDto checkOutResponseDto = checkOutService.getCheckOutDetail(detailId);
        return ResponseEntity.ok(checkOutResponseDto);
    }
    @PatchMapping("/checkout/{detailId}") //TODO param에 수정할 checkoutdetail 번호를 입력받아야함, user의 토큰 권한도 확인, 배송지 정보 수정
    public ResponseEntity<CheckOutResponseDto> updateCheckOutAddress(@PathVariable("detailId") Long detailId, @RequestBody CheckOutRequestDto checkOutRequestDto) {
        //TODO 권한 확인 필요
        //TODO 잘 되는데 주소 id번호까지 같이 바뀌어버림; CheckOut을 address랑 분리해야함!
        //service에서 email로 userid를 체크해서 update함
        CheckOutResponseDto checkOutResponseDto = checkOutService.updateCheckOut(detailId, checkOutRequestDto);
        return ResponseEntity.ok(checkOutResponseDto);
    }
    @DeleteMapping("/checkout/{detailId}") //TODO param에 checkout 번호를 입력받아야함, user의 토큰 권한도 확인
    public ResponseEntity<Long> deleteCheckOut(@PathVariable("detailId") Long detailId) {
        Long deletedId = checkOutService.deleteCheckOut(detailId);
        return ResponseEntity.ok(deletedId);
    }
    @GetMapping("/checkout-item/{checkoutId}") // checkoutitem 가져오기
    public ResponseEntity getCheckoutItems(@PathVariable("checkoutId") Long checkoutId){
        return ResponseEntity.ok().body(checkOutService.getCheckOutItem(checkoutId));
    }

    //TODO 검증 필요!
    @PostMapping("/checkout-item/") //checkoutItem의 checkoutId로 checkout과 1:N연관관계를 맺음
    public ResponseEntity addCheckoutItem(@RequestBody CheckOutItemRequestDto checkOutItemRequestDto) {
        try {
            checkOutService.addCheckOutItem(checkOutItemRequestDto);
            return ResponseEntity.ok().body(checkOutItemRequestDto.getCheckOutId());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("주문 아이템 오류" + e.getMessage());
        }
    }


    @GetMapping("/admin/checkout") //userEmail로 checkout 정보 받기
    public ResponseEntity<List<CheckOutResponseDto>> getCheckOutsByAdmin(@AuthenticationPrincipal User principal) {
        //TODO        request의 cookies token값의 ROLE이 admin이 아니라면 작동 X
        //TODO role 어드민일때! 사용가능한 mapping
        // 그냥 모두 조회
        // TODO role authorities => admin일때

        List<CheckOutResponseDto> checkOuts = checkOutService.getAllCheckOuts();
        return ResponseEntity.ok(checkOuts);
    }

    // 어드민 프론트에서 쏴줄거 ⇒ dropdown으로 선택한 status, checkout 번호 > 수정
    @PatchMapping("/admin/checkout/delivery-status/{detailId}")
    public ResponseEntity<CheckOutResponseDto> updateDeliveryStatus (@PathVariable("detailId") Long detailId, @RequestBody DeliveryStatusRequestDto request) {
        //TODO 권한 확인 필요
        //TODO 잘 되는데 주소 id번호까지 같이 바뀌어버림; CheckOut을 address랑 분리해야함!
        //service에서 email로 userid를 체크해서 update함
        CheckOutResponseDto checkOutResponseDto = checkOutService.updateDeliveryStatus(detailId, request.getDeliveryStatus());
        return ResponseEntity.ok(checkOutResponseDto);

        //TODO patch => 업데이트 방법? update
    }

    @DeleteMapping("/admin/checkout/{detailId}")
    public ResponseEntity<Long> deleteCheckOutByAdmin(@PathVariable("detailId") Long detailId) {
        Long deletedId = checkOutService.deleteUserCheckOut(detailId);
        return ResponseEntity.ok(deletedId);
    }

    // 즉시 구매 조회
    @GetMapping("/checkout-each")
    public ResponseEntity<CheckOutEachResponseDto> getCheckOutEach() {
        UserResponse userResponse = userService.getUser();
        CheckOutEachResponseDto checkOutEachResponseDto = checkOutService.getCheckOutEach(userResponse.getUserId());

        return ResponseEntity.ok(checkOutEachResponseDto);
    }

    // 즉시 구매를 위한 정보 임시 저장
    @PostMapping("/checkout-each")
    public ResponseEntity addCheckOutEach(@RequestBody CheckOutEachRequestDto checkOutEachRequestDto) {
        UserResponse userResponse = userService.getUser();
        Long checkOutEachId = checkOutService.addCheckOutEach(userResponse.getUserId(), checkOutEachRequestDto);

        return ResponseEntity.ok(checkOutEachId);
    }

    // 주문완료 후 즉시 구매 삭제
    @DeleteMapping("/checkout-each/{checkOutEachId}")
    public ResponseEntity deleteCheckOutEach(@PathVariable("checkOutEachId") Long checkOutEachId) {
        checkOutService.deleteCheckOutEach(checkOutEachId);

        return ResponseEntity.ok()
                .build();
    }

}
