package com.exception;

public class ItemPedidoNotFoundException extends RuntimeException {
    public ItemPedidoNotFoundException(String message) {
        super(message);
    }
}
