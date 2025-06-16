package com.rental_manager.roomie.exceptions.resource_not_found_exceptions;

import com.rental_manager.roomie.exceptions.ExceptionMessages;

public class VerificationTokenDoesNotMatchException extends ResourceNotFoundException {
    public VerificationTokenDoesNotMatchException() {
        super(ExceptionMessages.VERIFICATION_TOKEN_DOES_NOT_MATCH);
    }
}
