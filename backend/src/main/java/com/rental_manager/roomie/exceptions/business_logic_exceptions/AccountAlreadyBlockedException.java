package com.rental_manager.roomie.exceptions.business_logic_exceptions;

import com.rental_manager.roomie.exceptions.ExceptionMessages;

public class AccountAlreadyBlockedException extends BusinessLogicConflictException {

    public AccountAlreadyBlockedException() {
        super(ExceptionMessages.ACCOUNT_ALREADY_BLOCKED);
    }
}
