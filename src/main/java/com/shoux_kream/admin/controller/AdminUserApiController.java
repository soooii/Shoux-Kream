package com.shoux_kream.admin.controller;

import com.shoux_kream.admin.dto.request.AdminUserUpdateRequest;
import com.shoux_kream.admin.dto.response.AdminUserResponse;
import com.shoux_kream.admin.service.AdminUserService;
import com.shoux_kream.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
public class AdminUserApiController {

    private final AdminUserService adminUserService;

    @GetMapping()
    public ResponseEntity<List<AdminUserResponse>> getAllUser() {
        return adminUserService.getAllUser();
    }

    @PutMapping()
    public ResponseEntity<Void> updateUser(@RequestBody @Validated AdminUserUpdateRequest adminUserUpdateRequest) {
        return adminUserService.updateUser(adminUserUpdateRequest);
    }
}
