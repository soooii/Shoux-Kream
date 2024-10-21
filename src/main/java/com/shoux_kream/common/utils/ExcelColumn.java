package com.shoux_kream.common.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
/*
 *  엑셀 헤더 구분용 Custom Annotation
 * */
public @interface ExcelColumn {

    String headerName() default "";
    int sort() default 0;
}