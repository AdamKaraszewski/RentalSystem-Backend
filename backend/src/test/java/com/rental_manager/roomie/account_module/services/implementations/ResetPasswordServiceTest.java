package com.rental_manager.roomie.account_module.services.implementations;

import com.rental_manager.roomie.account_module.repositories.AccountRepository;
import com.rental_manager.roomie.account_module.repositories.ResetPasswordTokenRepository;
import com.rental_manager.roomie.exceptions.ExceptionMessages;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.AccountNotFoundException;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.ResetPasswordTokenDoesNotMatchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.rental_manager.roomie.AccountModuleTestUtility.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResetPasswordServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ResetPasswordTokenRepository resetPasswordTokenRepository;

    private ResetPasswordService underTest;

    @BeforeEach
    void setUp() {
        underTest = new ResetPasswordService(REST_PASSWORD_TOKEN_LIFE_TIME, accountRepository, resetPasswordTokenRepository);
    }

    @Test
    void generateResetPasswordTokenThrowsAccountNotFoundException() {
        when(accountRepository.findByEmail(eq(EMAIL))).thenReturn(Optional.empty());

        var exceptionThrown = assertThrows(AccountNotFoundException.class, () -> {
            underTest.generateResetPasswordToken(EMAIL);
        });
    }

    @Test
    void resetPasswordTest() {
        var resetPasswordToken = createResetPasswordToken();
        var account = resetPasswordToken.getAccount();
        when(resetPasswordTokenRepository.findByTokenValueAndExpirationDateAfter(eq(RESET_PASSWORD_TOKEN_VALUE), any(LocalDateTime.class))).thenReturn(Optional.of(resetPasswordToken));
        when(accountRepository.saveAndFlush(account)).thenReturn(account);
        doNothing().when(resetPasswordTokenRepository).delete(resetPasswordToken);

        underTest.resetPassword(RESET_PASSWORD_TOKEN_VALUE, NEW_PASSWORD, NEW_PASSWORD);

        assertEquals(NEW_PASSWORD, account.getPassword());
    }

    @Test
    void resetPasswordThrowsResetPasswordTokenDoesNotMatchException() {
        when(resetPasswordTokenRepository.findByTokenValueAndExpirationDateAfter(eq(RESET_PASSWORD_TOKEN_VALUE), any(LocalDateTime.class))).thenReturn(Optional.empty());

        var exceptionThrown = assertThrows(ResetPasswordTokenDoesNotMatchException.class, () -> {
            underTest.resetPassword(RESET_PASSWORD_TOKEN_VALUE, NEW_PASSWORD, NEW_PASSWORD);
        });

        assertEquals(ExceptionMessages.RESET_PASSWORD_TOKEN_DOES_NOT_MATCH, exceptionThrown.getMessage());
    }
}