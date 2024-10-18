package com.shoux_kream.common.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.LinkedList;

@Component
public class ExcelUtils implements ExcelUtilMethodFactory {

    @Override
    public <T> void downloadExcel(List<T> data, HttpServletResponse response) {
        // 엑셀파일(Workbook) 객체 생성
        Workbook workbook = new XSSFWorkbook();

        // 엑셀파일 sheet를 만들고, sheet의 이름을 지정해 줄 수 있다.
        Sheet sheet = workbook.createSheet("sheet1");

        // 엑셀의 열에 해당하는 Cell 객체 생성
        Cell cell = null;

        // 엑셀의 행에 해당하는 Row 객체 생성
        Row row = null;

        // Header의 내용을 List로 반환 받는다(엑셀의 Cell의 첫줄이 된다.)
        List<String> excelHeaderList = getHeaderName(getClass(data));


        // Header - 열의 첫줄(컬럼 이름들)을 그리는 작업
        // 첫 행을 생성해준다.
        row = sheet.createRow(0);

        // 헤더의 수(컬럼 이름의 수)만큼 반복해서 행을 생성한다.
        for (int i = 0; i < excelHeaderList.size(); i++) {
            // 열을 만들어준다.
            cell = row.createCell(i);
            // 열에 헤더이름(컬럼 이름)을 넣어준다.
            cell.setCellValue(excelHeaderList.get(i));
        }

        // Body
        // 헤더 밑의 엑셀 파일 내용부분에 들어갈 내용을 그리는 작업
        renderExcelBody(data, sheet, row, cell);

        // DownLoad
        // 엑셀 파일이 완성 되면 파일 다운로드를 위해 content-type과 Header를 설정해준다.
        // filename=파일이름.xlsx 가 파일의 이름이 된다.
        // TODO 파일이름은 dto에서 가져오도록 수정
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=shoux-kream.xlsx");

        try {
            // 엑셀 파일을 다운로드 하기 위해 write() 메서드를 사용한다.
            workbook.write(response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 파일 입출력 스트림을 사용한 후에는 예외 발생 여부와 관계없이 반드시 닫아 주어야 한다.
            closeWorkBook(workbook);
        }
    }

    @Override
    public <T> void renderExcelBody(List<T> data, Sheet sheet, Row row, Cell cell) {
        // Header를 그리고 시작했으므로 1부터 시작
        int rowCount = 1;

        // 조회해온 데이터 리스트의 크기만큼 반복문을 실행한다.
        for (T item : data) {

            // 헤더를 설정할때 0번 인덱스가 사용 되었으므로, i값에 1을 더해서 1번 로우(행)부터 생성한다.
            row = sheet.createRow(rowCount++);


            Class<?> currentClass = item.getClass();
            Field[] fields = currentClass.getDeclaredFields();

            int cellNum = 0;
            for (Field field : fields) {
                if (field.isAnnotationPresent(ExcelColumn.class)) {
                    ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
                    try {
                        // 필드 접근을 허용합니다.
                        field.setAccessible(true); // private 필드에 접근하기 위해

                        // 필드의 값을 가져옵니다.
                        Object value = field.get(item); // 필드 값 가져오기

                        // 셀을 생성하고 값을 설정
                        cell = row.createCell(cellNum++);
                        if (value instanceof String) {
                            cell.setCellValue((String) value);
                        } else if (value instanceof Integer) {
                            cell.setCellValue((Integer) value);
                        } else if (value instanceof LocalDateTime) {
                            cell.setCellValue(value.toString()); // LocalDateTime은 문자열로 변환
                        }
                        // 필요한 경우 다른 타입도 추가 처리
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // 리플렉션을 통해 모든 메서드를 가져옴
            // value 값을 뽑아서 cell에 값을 저장할때 엑셀 헤더 컬럼과 일치하지 않는 데이터를 뽑아옴
//            Method[] methods = item.getClass().getMethods();
//            for (Method method : methods) {
//                if (method.getName().startsWith("get")) { // getter 메서드만 호출
//                    try {
//                        Object value = method.invoke(item); // getter 메서드 실행
//
//                        // 셀을 생성하고 값을 설정
//                        cell = row.createCell(cellNum++);
//                        if (value instanceof String) {
//                            cell.setCellValue((String) value);
//                        } else if (value instanceof Integer) {
//                            cell.setCellValue((Integer) value);
//                        } else if (value instanceof LocalDateTime) {
//                            cell.setCellValue(value.toString()); // LocalDateTime은 문자열로 변환
//                        }
//                        // 필요한 경우 다른 타입도 추가 처리
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
        }
    }

    // List(데이터 리스트)에 담긴 DTO의 클래스 정보를 반환하는 메서드
    private Class<?> getClass(List<?> data) {
        // List가 비어있지 않다면 List가 가지고 있는 모든 DTO는 같은 필드를 가지고 있으므로,
        // 맨 마지막 DTO만 빼서 클래스 정보를 반환한다.
        if (!CollectionUtils.isEmpty(data)) {
            return data.get(data.size() - 1).getClass();
        } else {
            throw new IllegalStateException("조회된 리스트가 비어 있습니다. 확인 후 다시 진행해주시기 바랍니다.");
        }
    }

    private List<String> getHeaderName(Class<?> type) {

        // 스트림으로 엑셀 헤더 이름들을 리스트로 반환
        // 1. 매개변수로 전달된 클래스의 필드들을 배열로 받아, 스트림을 생성
        // 2. @ExcelColumn 애너테이션이 붙은 필드만 수집
        // 3. @ExcelColumn 애너테이션이 붙은 필드에서 애너테이션의 값을 매핑
        // 4. LinkedList로 반환
        List<String> excelHeaderNameList = Arrays.stream(type.getDeclaredFields())
                .filter(s -> s.isAnnotationPresent(ExcelColumn.class))
                .map(s -> s.getAnnotation(ExcelColumn.class).headerName())
                .collect(Collectors.toCollection(LinkedList::new));

        // 헤더의 이름을 담은 List가 비어있을 경우, 헤더 이름이 지정되지 않은 것이므로, 예외를 발생시킨다.
        if (CollectionUtils.isEmpty(excelHeaderNameList)) {
            throw new IllegalStateException("헤더 이름이 없습니다.");
        }

        return excelHeaderNameList;
    }

    private void closeWorkBook(Workbook workbook) {
        try {
            if (workbook != null) {
                workbook.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}