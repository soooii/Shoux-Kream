package com.shoux_kream.category.controller;

import com.shoux_kream.category.dto.CategoryDto;
import com.shoux_kream.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/categories")

public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping // 카테고리 추가
    public ResponseEntity<CategoryDto> createCategory(
            @RequestParam("category") CategoryDto categoryDto,
            @RequestParam("image") MultipartFile imageFile) {
        CategoryDto createdCategory = categoryService.createCategory(categoryDto, imageFile);
        return ResponseEntity.ok(createdCategory);
    }

    @GetMapping // 카테고리 목록 조회
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}") // 카테고리 수정
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Long id,
            @RequestParam("category") CategoryDto categoryDto,
            @RequestParam("image") MultipartFile imageFile) {
        CategoryDto updatedCategory = categoryService.updateCategory(id, categoryDto, imageFile);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}") // 카테고리 삭제
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
