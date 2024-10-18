package com.shoux_kream.admin.dto.excel;

import com.shoux_kream.common.utils.ExcelColumn;
import lombok.Getter;

@Getter
public class AdminCheckOutExcelRequest {
    @ExcelColumn(headerName = "주문 제목", sort=1)
    private String summaryTitle;
    @ExcelColumn(headerName = "총 가격", sort=2)
    private int totalPrice;
    @ExcelColumn(headerName = "배송 상태", sort=3)
    private String deliveryStatus;
    @ExcelColumn(headerName = "배송 요청 사항", sort=4)
    private String request;
    @ExcelColumn(headerName = "주소", sort=5)
    private String address1;
    @ExcelColumn(headerName = "상세 주소", sort=6)
    private String address2;
}
