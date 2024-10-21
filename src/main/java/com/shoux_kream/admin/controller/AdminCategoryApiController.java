package com.shoux_kream.admin.controller;

import com.shoux_kream.admin.dto.excel.AdminCategoryExcelRequest;
import com.shoux_kream.category.dto.CategoryDto;
import com.shoux_kream.category.service.CategoryService;
import com.shoux_kream.common.utils.ExcelUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/category")
@RequiredArgsConstructor
public class AdminCategoryApiController {
    private final CategoryService categoryService;
    private final ExcelUtils excelUtils;

    @GetMapping()
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/excel/download")
    public void excelDownLoad(HttpServletResponse response, @RequestBody List<AdminCategoryExcelRequest> adminCategoryExcelRequest) {
        // 엑셀 다운로드 로직 실행
        excelUtils.downloadExcel(adminCategoryExcelRequest, response);
    }
}
