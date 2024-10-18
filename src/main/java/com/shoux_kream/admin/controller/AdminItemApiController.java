package com.shoux_kream.admin.controller;

import com.shoux_kream.admin.dto.excel.AdminItemExcelRequest;
import com.shoux_kream.common.utils.ExcelUtils;
import com.shoux_kream.item.dto.response.ItemResponse;
import com.shoux_kream.item.service.ItemService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/item")
@RequiredArgsConstructor
public class AdminItemApiController {

    private final ItemService itemService;
    private final ExcelUtils excelUtils;

    @GetMapping()
    public ResponseEntity<List<ItemResponse>> getItems() {
        return new ResponseEntity<>(itemService.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/excel/download")
    public void excelDownLoad(HttpServletResponse response, @RequestBody List<AdminItemExcelRequest> adminItemExcelRequest) {
        // 엑셀 다운로드 로직 실행
        excelUtils.downloadExcel(adminItemExcelRequest, response);
    }
}
