package com.shoux_kream.user.controller;

import com.shoux_kream.user.dto.request.UserRequest;
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

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody UserRequest userRequest) {
        Long userId = userService.signup(userRequest);
        UserResponse userResponse = new UserResponse(userId, userRequest.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<String> me(@AuthenticationPrincipal User principal) {
        return ResponseEntity.ok("my userEmail is " + principal.getUsername());
    }

    @PatchMapping("/me/profile")
    public ResponseEntity<UserResponse> updateProfile(@AuthenticationPrincipal User principal, @RequestBody UserRequest userRequest) {
        log.info(principal.getUsername());
        UserResponse userResponse = userService.updateProfile(principal.getUsername(), userRequest);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> delete(@AuthenticationPrincipal User principal) {
        userService.deleteUser(principal.getUsername());
        return ResponseEntity.ok("탈퇴가 완료되었습니다.");
    }





}
