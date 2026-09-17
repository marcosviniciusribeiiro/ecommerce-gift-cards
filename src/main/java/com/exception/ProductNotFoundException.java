package com.exception;

public class ProductNotFoundException
        extends RuntimeException{
    public ProductNotFoundException(String mensagem){
        super(mensagem);
    }
}
