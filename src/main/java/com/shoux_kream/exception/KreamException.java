package com.shoux_kream.exception;

public class KreamException extends RuntimeException {
    private final ErrorCode errorCode;

    public KreamException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return errorCode.getDescription();
    }
}
