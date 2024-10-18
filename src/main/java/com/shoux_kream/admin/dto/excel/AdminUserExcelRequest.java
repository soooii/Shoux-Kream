package com.shoux_kream.admin.dto.excel;

import com.shoux_kream.common.utils.ExcelColumn;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminUserExcelRequest {
    @ExcelColumn(headerName = "이름", sort=1)
    private String userName;
    @ExcelColumn(headerName = "아이디", sort=2)
    private String userEmail;
    @ExcelColumn(headerName = "권한", sort=3)
    private String userRole;
    @ExcelColumn(headerName = "닉네임", sort=4)
    private String userNickName;
    @ExcelColumn(headerName = "생성 시간", sort=5)
    private LocalDateTime createAt;
    @ExcelColumn(headerName = "수정 시간", sort=6)
    private LocalDateTime updateAt;
}
