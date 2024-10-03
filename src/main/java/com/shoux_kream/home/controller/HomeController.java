package com.shoux_kream.home.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    // 홈 화면이동
    @GetMapping("/")
    public String getHome(Model model) {
        String staticPath = "/static/home";
        model.addAttribute("staticPath", staticPath);
        return "home/home";
    }
}
