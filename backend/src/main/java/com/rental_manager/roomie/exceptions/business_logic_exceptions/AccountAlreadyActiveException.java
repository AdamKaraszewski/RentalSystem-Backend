package com.rental_manager.roomie.exceptions.business_logic_exceptions;

import com.rental_manager.roomie.exceptions.ExceptionMessages;

public class AccountAlreadyActiveException extends BusinessLogicConflictException {

    public AccountAlreadyActiveException() {
        super(ExceptionMessages.ACCOUNT_ALREADY_ACTIVE);
    }
}
