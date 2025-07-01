package com.rental_manager.roomie.exceptions.validation_exceptions;

import com.rental_manager.roomie.exceptions.ExceptionMessages;

public class PasswordDoesNotMatchException extends ValidationException {
    public PasswordDoesNotMatchException() {
        super(ExceptionMessages.PASSWORD_DOES_NOT_MATCH);
    }
}
