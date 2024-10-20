package com.shoux_kream.admin.controller;

import com.shoux_kream.admin.dto.excel.AdminUserExcelRequest;
import com.shoux_kream.admin.dto.request.AdminUserUpdateRequest;
import com.shoux_kream.admin.dto.response.AdminUserResponse;
import com.shoux_kream.admin.service.AdminUserService;
import com.shoux_kream.common.utils.ExcelUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
public class AdminUserApiController {

    private final AdminUserService adminUserService;
    private final ExcelUtils excelUtils;

    @GetMapping()
    public ResponseEntity<List<AdminUserResponse>> getAllUser() {
        return adminUserService.getAllUser();
    }

    @PutMapping()
    public ResponseEntity<Void> updateUser(@RequestBody @Validated AdminUserUpdateRequest adminUserUpdateRequest) {
        return adminUserService.updateUser(adminUserUpdateRequest);
    }

    @PostMapping("/excel/download")
    public void excelDownLoad(HttpServletResponse response, @RequestBody List<AdminUserExcelRequest> adminUserExcelRequest) {
        // 엑셀 다운로드 로직 실행
        excelUtils.downloadExcel(adminUserExcelRequest, response);
    }
}
