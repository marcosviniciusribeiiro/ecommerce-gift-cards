package com.exception;

public class ProdutoEmUsoException extends RuntimeException {
    public ProdutoEmUsoException(String message) {
        super(message);
    }
}
