package com.shoux_kream.home.controller;

import com.shoux_kream.category.dto.CategoryDto;
import com.shoux_kream.category.service.CategoryService;
import com.shoux_kream.item.dto.response.ItemResponse;
import com.shoux_kream.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final CategoryService categoryService;
    private final ItemService itemService;
    // 홈 화면 이동
    @GetMapping("/")
    public String getHome(Model model) {
        List<CategoryDto> categories = categoryService.getAllCategories();
        List<ItemResponse> items = itemService.findAll();

        model.addAttribute("categories", categories);
        model.addAttribute("items", items);
        return "home/home";
    }
}
