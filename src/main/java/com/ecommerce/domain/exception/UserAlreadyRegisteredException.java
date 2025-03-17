package com.ecommerce.domain.exception;

public class UserAlreadyRegisteredException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public UserAlreadyRegisteredException(String message) {
        super(message);
    }

}