package com.shoux_kream.common.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public interface ExcelUtilMethodFactory {

    <T> void downloadExcel(List<T> data, HttpServletResponse response);
    <T> void renderExcelBody(List<T> data, Sheet sheet, Row row, Cell cell);
}