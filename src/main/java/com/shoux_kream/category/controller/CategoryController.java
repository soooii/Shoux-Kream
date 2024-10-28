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
    private final CategoryService categoryService;

    // URL 변경: GET 메소드는 /category-add 로 호출
    @GetMapping("/add")
    public String addCategoryPage() {
        return "category/category-add"; }// category-add.html 템플릿 파일 반환

    @GetMapping("/edit/{id}")
    public String editCategoryPage(){ return "category/category-edit"; }

    @GetMapping("/category-list") // 모든 카테고리 조회
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}") // 특정 ID에 해당하는 카테고리 조회 기능 추가
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable(name = "id") Long id) {
        CategoryDto categoryDto = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/category-add")
    public ResponseEntity<CategoryDto> createCategory(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("themeClass") String themeClass,
            @RequestParam("image") MultipartFile imageFile) throws IOException {

        // 이미지 파일이 null 인지 확인
        if (imageFile == null || imageFile.isEmpty()) {
            return ResponseEntity.badRequest().body(null); // 적절한 에러 메시지를 반환할 수 있도록 조정
        }

        // DTO 로 변환
        CategoryDto categoryDto = new CategoryDto(title, description, themeClass);

        // 카테고리 생성 및 이미지 업로드
        CategoryDto createdCategory = categoryService.createCategory(categoryDto, imageFile);
        return ResponseEntity.ok(createdCategory);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable(name ="id") Long id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("themeClass") String themeClass,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        // 카테고리 DTO 생성
        CategoryDto categoryDto = new CategoryDto(title, description, themeClass);

        // 카테고리 수정 및 이미지 업로드
        CategoryDto updatedCategory = categoryService.updateCategory(id, categoryDto, imageFile);

        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}") // 삭제 기능
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
