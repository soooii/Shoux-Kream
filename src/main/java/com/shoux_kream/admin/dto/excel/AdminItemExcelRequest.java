package com.shoux_kream.admin.dto.excel;

import com.shoux_kream.common.utils.ExcelColumn;
import lombok.Getter;

@Getter
public class AdminItemExcelRequest {
    @ExcelColumn(headerName = "상품명", sort=1)
    private String title;
    @ExcelColumn(headerName = "제조사", sort=2)
    private String manufacturer;
    @ExcelColumn(headerName = "재고", sort=3)
    private int inventory;
    @ExcelColumn(headerName = "가격", sort=4)
    private int price;
    @ExcelColumn(headerName = "요약 설명", sort=5)
    private String shortDescription;
    @ExcelColumn(headerName = "상세 설명", sort=6)
    private String detailDescription;
}
