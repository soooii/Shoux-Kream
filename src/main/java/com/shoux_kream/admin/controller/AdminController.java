package com.shoux_kream.admin.controller;

import com.shoux_kream.admin.dto.excel.AdminCheckOutExcelRequest;
import com.shoux_kream.common.utils.ExcelUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ExcelUtils excelUtils;

    // 어드민 메인 화면 이동
    @GetMapping()
    public String getAdminHome(Model model) {
        return "admin/admin";
    }
    @GetMapping("/check")
    public ResponseEntity<String> checkAdmin(@AuthenticationPrincipal User principal) {
        String role = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(null);
        return ResponseEntity.ok(role);
    }

    // 주문 관리 엑셀 저장
    @PostMapping("/checkout/excel/download")
    public void excelDownLoad(HttpServletResponse response, @RequestBody List<AdminCheckOutExcelRequest> adminCheckOutExcelRequest) {
        // 엑셀 다운로드 로직 실행
        excelUtils.downloadExcel(adminCheckOutExcelRequest, response);
    }
}
