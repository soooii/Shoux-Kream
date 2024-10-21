package com.shoux_kream.admin.controller;

import com.shoux_kream.admin.dto.excel.AdminUserLogExcelRequest;
import com.shoux_kream.admin.dto.request.UserLogCreateRequest;
import com.shoux_kream.admin.dto.request.UserLogUpdateRequest;
import com.shoux_kream.admin.dto.response.UserLogResponse;
import com.shoux_kream.admin.service.UserLogService;
import com.shoux_kream.common.utils.ExcelUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user-log")
@RequiredArgsConstructor
public class UserLogApiController {

    private final UserLogService userLogService;
    private final ExcelUtils excelUtils;

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

    // 엑셀 저장
    @PostMapping("/excel/download")
    public void excelDownLoad(HttpServletResponse response, @RequestBody List<AdminUserLogExcelRequest> adminUserLogExcelRequest) {
        // 엑셀 다운로드 로직 실행
        excelUtils.downloadExcel(adminUserLogExcelRequest, response);
    }
}
