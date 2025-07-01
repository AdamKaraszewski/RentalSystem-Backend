package com.rental_manager.roomie.exceptions.business_logic_exceptions;

import com.rental_manager.roomie.exceptions.ExceptionMessages;

public class RoleAlreadyOwnedException extends BusinessLogicConflictException {

    public RoleAlreadyOwnedException() {
        super(ExceptionMessages.ROLE_ALREADY_OWNED);
    }
}
