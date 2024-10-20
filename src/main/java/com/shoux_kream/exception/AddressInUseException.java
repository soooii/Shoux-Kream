package com.shoux_kream.exception;

public class AddressInUseException extends RuntimeException {
    public AddressInUseException(String message) {
        super(message);
    }
}
