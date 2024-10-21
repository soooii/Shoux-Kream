package com.shoux_kream.admin.dto.excel;

import com.shoux_kream.common.utils.ExcelColumn;
import lombok.Getter;

@Getter
public class AdminCategoryExcelRequest {
    @ExcelColumn(headerName = "카테고리 제목", sort=1)
    private String title;
    @ExcelColumn(headerName = "카테고리 설명", sort=2)
    private String description;
    @ExcelColumn(headerName = "이미지", sort=3)
    private String imageUrl;
    @ExcelColumn(headerName = "테마", sort=4)
    private String themeClass;
}
