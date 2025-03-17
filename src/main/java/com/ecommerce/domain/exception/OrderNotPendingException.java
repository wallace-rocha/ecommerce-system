package com.ecommerce.domain.exception;

public class OrderNotPendingException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public OrderNotPendingException(String message) {
        super(message);
    }

}