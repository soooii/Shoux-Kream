package com.shoux_kream.admin.dto.excel;

import com.shoux_kream.common.utils.ExcelColumn;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminUserLogExcelRequest {
    @ExcelColumn(headerName = "아이디", sort=1)
    private String userName;
    @ExcelColumn(headerName = "사용자IP", sort=2)
    private String clientIp;
    @ExcelColumn(headerName = "요청 경로", sort=3)
    private String requestUrl;
    @ExcelColumn(headerName = "요청 메서드", sort=4)
    private String requestMethod;
    @ExcelColumn(headerName = "요청 시간", sort=5)
    private LocalDateTime requestTime;
    @ExcelColumn(headerName = "응답 상태", sort=6)
    private int responseStatus;
    @ExcelColumn(headerName = "응답 시간", sort=7)
    private LocalDateTime responseTime;

}
