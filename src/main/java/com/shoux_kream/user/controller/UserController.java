package com.shoux_kream.user.controller;

import com.shoux_kream.user.dto.request.UserRequest;
import com.shoux_kream.user.dto.response.UserAddressDto;
import com.shoux_kream.user.dto.response.UserResponse;
import com.shoux_kream.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody UserRequest userRequest) {
        Long userId = userService.signup(userRequest);
        UserResponse userResponse = new UserResponse(userId, userRequest.getEmail(), userRequest.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    //계정 관리 페이지
    @PatchMapping("/me/profile")
    public ResponseEntity<UserResponse> updateProfile(@AuthenticationPrincipal User principal, @RequestBody UserRequest userRequest) {
        log.info(principal.getUsername());
        UserResponse userResponse = userService.updateProfile(principal.getUsername(), userRequest);
        return ResponseEntity.ok(userResponse);
    }

    //회원 탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<String> delete(@AuthenticationPrincipal User principal) {
        userService.deleteUser(principal.getUsername());
        return ResponseEntity.ok("탈퇴가 완료되었습니다.");
    }


    //마이페이지
    @GetMapping({"/me"})
    public ResponseEntity<UserResponse> myPage(@AuthenticationPrincipal User principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = principal.getUsername();
        UserResponse userResponse = userService.getUser(email);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/userAddress")
    public ResponseEntity<List<UserAddressDto>> getUserAddress(@AuthenticationPrincipal User principal){
        //recipientName, recipientPhone, postalCode, address1, address2 user에서 얻어오기
        String email = principal.getUsername();
        List<UserAddressDto> userAddresses = userService.getUserAddresses(email);
        return ResponseEntity.ok(userAddresses);
    }

}
