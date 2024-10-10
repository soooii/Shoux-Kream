package com.shoux_kream.admin.controller;

import com.shoux_kream.admin.dto.response.UserLogResponse;
import com.shoux_kream.admin.service.UserLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class UserLogApiController {

    private final UserLogService userLogService;
    @GetMapping("/user-log")
    public ResponseEntity<List<UserLogResponse>> getUserLog() {
        return userLogService.getUserLogList();
    }
}
