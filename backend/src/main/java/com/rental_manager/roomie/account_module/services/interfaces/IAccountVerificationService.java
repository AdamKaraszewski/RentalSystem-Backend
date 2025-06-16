package com.rental_manager.roomie.account_module.services.interfaces;

import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.VerificationTokenDoesNotMatchException;

public interface IAccountVerificationService {

    void verifyAccountUsingVerificationToken(String verificationTokenValue) throws VerificationTokenDoesNotMatchException;
}
