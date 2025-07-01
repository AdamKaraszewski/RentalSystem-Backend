package com.rental_manager.roomie.exceptions.business_logic_exceptions;

import com.rental_manager.roomie.exceptions.ExceptionMessages;

public class AccountDoesNotOweAnyRoleException extends BusinessLogicConflictException {
    public AccountDoesNotOweAnyRoleException() {
        super(ExceptionMessages.ACCOUNT_DOES_NOT_OWE_ANY_ROLE);
    }
}
