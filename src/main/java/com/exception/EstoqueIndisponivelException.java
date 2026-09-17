package com.exception;

public class EstoqueIndisponivelException
        extends RuntimeException {
    public EstoqueIndisponivelException(String message) {
        super(message);
    }
}
