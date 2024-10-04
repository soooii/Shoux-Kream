package com.shoux_kream.user.controller;

import com.shoux_kream.user.dto.request.UserRequest;
import com.shoux_kream.user.dto.response.UserResponse;
import com.shoux_kream.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody UserRequest userRequest) {
        Long userId = userService.save(userRequest);
        UserResponse userResponse = new UserResponse(userId,userRequest.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<String> me(@AuthenticationPrincipal User principal) {
        return ResponseEntity.ok("my userEmail is " + principal.getUsername());
    }

    @PatchMapping("/me/profile")
    public ResponseEntity<Boolean> updateProfile(@AuthenticationPrincipal User principal, @RequestBody UserRequest userRequest) {
        Long userId = userService.updateProfile(principal.getUsername(), userRequest);
        return ResponseEntity.ok(userId != null);
    }




}
