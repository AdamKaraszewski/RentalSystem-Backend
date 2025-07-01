package com.rental_manager.roomie.exceptions.validation_exceptions;

import com.rental_manager.roomie.exceptions.ExceptionMessages;

public class RoleDoesNotExistException extends ValidationException {
    public RoleDoesNotExistException() {
        super(ExceptionMessages.SPECIFIED_ROLE_DOES_NOT_EXIST);
    }
}
