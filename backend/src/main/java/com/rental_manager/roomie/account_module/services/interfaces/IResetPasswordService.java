package com.rental_manager.roomie.account_module.services.interfaces;

import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.AccountNotFoundException;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.ResetPasswordTokenDoesNotMatchException;
import com.rental_manager.roomie.exceptions.validation_exceptions.PasswordDoesNotMatchException;

public interface IResetPasswordService {

    void generateResetPasswordToken(String email) throws AccountNotFoundException;

    void resetPassword(String tokenValue, String newPassword, String repeatNewPassword)
            throws ResetPasswordTokenDoesNotMatchException, PasswordDoesNotMatchException;
}
