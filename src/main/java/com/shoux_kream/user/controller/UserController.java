package com.shoux_kream.user.controller;

import com.shoux_kream.exception.InvalidPasswordException;
import com.shoux_kream.user.dto.request.AccountRequest;
import com.shoux_kream.user.dto.request.UserRequest;
import com.shoux_kream.user.dto.response.UserAddressDto;
import com.shoux_kream.user.dto.response.UserResponse;
import com.shoux_kream.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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

    //회원정보 불러오기
    @GetMapping("/me")
    public ResponseEntity<UserResponse> myPage() {
        UserResponse userResponse = userService.getUser();
        return ResponseEntity.ok(userResponse);
    }

    //입력한 기존 비밀번호가 db 비밀번호와 다를 때 발생하는 Exception
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<String> handleInvalidPasswordException(InvalidPasswordException ex) {
        return ResponseEntity.status(418).body(ex.getMessage());
    }

    //회원정보 수정
    @PatchMapping({"/me"})
    public ResponseEntity<String> updateProfile(@RequestBody AccountRequest accountRequest) {
        userService.updateProfile(accountRequest);
        return ResponseEntity.ok("수정이 완료되었습니다.");
    }

    //회원 탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<String> delete() {
        userService.deleteUser();
        return ResponseEntity.ok("탈퇴가 완료되었습니다.");
    }


    @GetMapping("/userAddress")
    public ResponseEntity<List<UserAddressDto>> getUserAddress(@AuthenticationPrincipal User principal){
        //recipientName, recipientPhone, postalCode, address1, address2 user에서 얻어오기
        String email = principal.getUsername();
        List<UserAddressDto> userAddresses = userService.getUserAddresses(email);
        return ResponseEntity.ok(userAddresses);
    }

    //Admin 확인
    @GetMapping("/admin-check")
    public ResponseEntity<String> adminCheck() {
        return ResponseEntity.ok("권한이 인증되었습니다.");
    }




}
