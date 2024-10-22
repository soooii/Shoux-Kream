package com.shoux_kream.user.controller;

import com.shoux_kream.exception.InvalidPasswordException;
import com.shoux_kream.user.dto.request.AccountRequest;
import com.shoux_kream.user.dto.request.UserAddressRequest;
import com.shoux_kream.user.dto.request.UserRequest;
import com.shoux_kream.user.dto.response.UserAddressDto;
import com.shoux_kream.user.dto.response.UserResponse;
import com.shoux_kream.user.entity.UserAddress;
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

    //배송지 목록 가져오기
    @GetMapping("/userAddress")
    public ResponseEntity<List<UserAddressDto>> getUserAddress(){
        List<UserAddressDto> userAddresses = userService.getAddresses();
        return ResponseEntity.ok(userAddresses);
    }

    //특정 배송지 가져오기
    @GetMapping("/userAddress/{addressId}")
    public ResponseEntity<UserAddressDto> getAddress(@PathVariable Long addressId) {
        UserAddressDto dto = userService.getAddressById(addressId);
        return ResponseEntity.ok(dto);
    }

    //배송지 추가
    @PostMapping("/userAddress")
    public ResponseEntity<UserAddressRequest> addUserAddress(@RequestBody UserAddressRequest userAddressRequest) {
        userService.addAddress(userAddressRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userAddressRequest);
    }

    //배송지 수정
    @PatchMapping("userAddress/{addressId}")
    public ResponseEntity<UserAddressDto> updateAddress(@PathVariable Long addressId, @RequestBody UserAddressDto userAddressDto) {
        UserAddressDto updatedAddress = userService.updateAddress(addressId, userAddressDto);
        return ResponseEntity.ok(updatedAddress);
    }

    //배송지 삭제
    @DeleteMapping("/userAddress/{addressId}")
    public ResponseEntity<Void> deleteUserAddress(@PathVariable Long addressId) {
        userService.deleteAddress(addressId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 삭제 후 204 응답
    }


    //Admin 확인
    @GetMapping("/admin-check")
    public ResponseEntity<String> adminCheck() {
        return ResponseEntity.ok("권한이 인증되었습니다.");
    }




}
