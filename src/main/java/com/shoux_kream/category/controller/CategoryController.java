package com.shoux_kream.category.controller;

import com.shoux_kream.category.dto.CategoryDto;
import com.shoux_kream.category.service.CategoryService;
import com.shoux_kream.config.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/category-add")
    public ResponseEntity<CategoryDto> createCategory(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("themeClass") String themeClass,
            @RequestParam("image") MultipartFile imageFile) throws IOException {

        // DTO로 변환
        CategoryDto categoryDto = new CategoryDto(title, description, themeClass);

        // 카테고리 생성 및 이미지 업로드
        CategoryDto createdCategory = categoryService.createCategory(categoryDto, imageFile);
        return ResponseEntity.ok(createdCategory);
    }

    @GetMapping("/add")
    public String addCategoryPage() {
        return "category-add/form";  // category-add.html 템플릿 파일 반환
    }

    @GetMapping("/page")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Long id,
            @RequestParam("category") CategoryDto categoryDto,
            @RequestParam("image") MultipartFile imageFile) {
        CategoryDto updatedCategory = categoryService.updateCategory(id, categoryDto, imageFile);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
