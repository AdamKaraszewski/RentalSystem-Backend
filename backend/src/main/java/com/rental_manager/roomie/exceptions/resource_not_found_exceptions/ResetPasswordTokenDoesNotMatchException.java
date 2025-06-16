package com.rental_manager.roomie.exceptions.resource_not_found_exceptions;

import com.rental_manager.roomie.exceptions.ExceptionMessages;

public class ResetPasswordTokenDoesNotMatchException extends ResourceNotFoundException {

    public ResetPasswordTokenDoesNotMatchException() {
        super(ExceptionMessages.RESET_PASSWORD_TOKEN_DOES_NOT_MATCH);
    }
}
