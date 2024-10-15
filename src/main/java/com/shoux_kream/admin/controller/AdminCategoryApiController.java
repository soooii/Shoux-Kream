package com.shoux_kream.admin.controller;

import com.shoux_kream.category.dto.CategoryDto;
import com.shoux_kream.category.service.CategoryService;
import com.shoux_kream.item.dto.response.ItemResponse;
import com.shoux_kream.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/category")
@RequiredArgsConstructor
public class AdminCategoryApiController {
    private final CategoryService categoryService;

    @GetMapping()
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
}
