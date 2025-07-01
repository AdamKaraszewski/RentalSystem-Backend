package com.rental_manager.roomie.exceptions.business_logic_exceptions;

import com.rental_manager.roomie.exceptions.ExceptionMessages;

public class RoleIsNotOwnedException extends BusinessLogicConflictException {
    public RoleIsNotOwnedException() {
        super(ExceptionMessages.ROlE_IS_NOT_OWNED);
    }
}
