package com.ecommerce.domain.exception;

public class OutOfStockFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public OutOfStockFoundException(String message) {
        super(message);
    }

}