package com.shoux_kream.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionAdvice {

    // 400 예외처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity badRequestHandle(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        List<ErrorResult.FieldError> errorLogs
                = fieldErrors.stream()
                .map(error -> new ErrorResult.FieldError(
                        error.getField(),
                        error.getRejectedValue(),
                        error.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        return new ResponseEntity<>(errorLogs, HttpStatus.BAD_REQUEST);
    }

}