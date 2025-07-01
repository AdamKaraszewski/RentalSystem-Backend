package com.rental_manager.roomie.exceptions.business_logic_exceptions;

import com.rental_manager.roomie.exceptions.ExceptionMessages;

public class RoleDoesNotExistException extends BusinessLogicConflictException {
    public RoleDoesNotExistException() {
        super(ExceptionMessages.SPECIFIED_ROLE_DOES_NOT_EXIST);
    }
}
