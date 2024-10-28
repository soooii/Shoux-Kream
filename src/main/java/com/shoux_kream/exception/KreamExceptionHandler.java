package com.shoux_kream.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class KreamExceptionHandler {

    // Logger 설정
    private static final Logger logger = LoggerFactory.getLogger(KreamExceptionHandler.class);

    @ExceptionHandler(KreamException.class)
    public ResponseEntity<ErrorResponse> handleException(KreamException creamException) {
        logger.error("KreamException occurred: {}", creamException.getMessage(), creamException);
        return ResponseEntity.status(creamException.getErrorCode().getHttpStatus())
                .body(ErrorResponse.of(creamException.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();
        logger.warn("Validation error: {}", errorMessage, e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage));
    }

    //입력한 기존 비밀번호가 db 비밀번호와 다를 경우 발생하는 Exception
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<String> handleInvalidPasswordException(InvalidPasswordException ex) {
        return ResponseEntity.status(403).body(ex.getMessage());
    }

    //해당 아이디의 주소가 없을 경우 발생하는 Exception
    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<String> handleAddressNotFoundException(AddressNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }




//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
//        logger.error("Unhandled exception occurred: {}", exception.getMessage(), exception);
//
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "something went wrong on server"));
//    }
}
