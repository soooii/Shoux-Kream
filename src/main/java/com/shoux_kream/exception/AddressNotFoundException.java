package com.shoux_kream.exception;

public class AddressNotFoundException extends RuntimeException {
  public AddressNotFoundException(String message) {
    super(message);
  }
}
