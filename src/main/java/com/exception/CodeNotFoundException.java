package com.exception;

public class CodeNotFoundException extends RuntimeException {
    public CodeNotFoundException(String message) {
        super(message);
    }
}
