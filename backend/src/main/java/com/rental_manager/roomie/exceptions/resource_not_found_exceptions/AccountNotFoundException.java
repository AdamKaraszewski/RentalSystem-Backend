package com.rental_manager.roomie.exceptions.resource_not_found_exceptions;

import com.rental_manager.roomie.exceptions.ExceptionMessages;

public class AccountNotFoundException extends ResourceNotFoundException {
    public AccountNotFoundException() {
        super(ExceptionMessages.ACCOUNT_NOT_FOUND);
    }
}
