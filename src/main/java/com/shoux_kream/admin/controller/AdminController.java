package com.shoux_kream.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class AdminController {
    // 어드민 메인 화면 이동
    @GetMapping("/api/admin")
    public String getAdminHome(Model model) {
        return "admin/admin";
    }
}
