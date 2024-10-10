package com.shoux_kream.admin.controller;

import com.shoux_kream.admin.dto.request.UserLogCreateRequest;
import com.shoux_kream.admin.dto.request.UserLogUpdateRequest;
import com.shoux_kream.admin.dto.response.UserLogResponse;
import com.shoux_kream.admin.service.UserLogService;
import com.shoux_kream.cart.dto.CartRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user-log")
@RequiredArgsConstructor
public class UserLogApiController {

    private final UserLogService userLogService;
    @GetMapping()
    public ResponseEntity<List<UserLogResponse>> getUserLog() {
        return userLogService.getUserLogList();
    }

    @PostMapping()
    public ResponseEntity<Void> createUserLog(@RequestBody UserLogCreateRequest userLogCreateRequest) {
        return userLogService.createUserLog(userLogCreateRequest);
    }

    @PutMapping()
    public ResponseEntity<Void> updateUserLog(@RequestBody UserLogUpdateRequest userLogUpdateRequest) {
        return userLogService.updateUserLog(userLogUpdateRequest);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteUserLog(@RequestBody List<Long> userLogIds) {
        return userLogService.deleteUserLog(userLogIds);
    }
}
