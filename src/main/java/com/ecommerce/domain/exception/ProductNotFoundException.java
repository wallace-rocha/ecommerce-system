package com.ecommerce.domain.exception;

public class ProductNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public ProductNotFoundException(String message) {
        super(message);
    }

}