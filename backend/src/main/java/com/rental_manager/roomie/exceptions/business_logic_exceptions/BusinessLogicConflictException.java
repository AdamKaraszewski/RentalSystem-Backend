package com.rental_manager.roomie.exceptions.business_logic_exceptions;

public class BusinessLogicConflictException extends RuntimeException {
    public BusinessLogicConflictException(String message) {
        super(message);
    }
}
