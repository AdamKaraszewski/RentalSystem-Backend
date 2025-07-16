package com.rental_manager.roomie.account_module.repositories;

import com.rental_manager.roomie.IntegrationTestsBase;
import com.rental_manager.roomie.entities.Account;
import com.rental_manager.roomie.entities.ResetPasswordToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.rental_manager.roomie.AccountModuleTestUtility.*;
import static org.junit.jupiter.api.Assertions.*;

class ResetPasswordTokenRepositoryIntegrationTest extends IntegrationTestsBase {

    @Autowired
    private ResetPasswordTokenRepository underTest;

    @Autowired
    private AccountRepository accountRepository;

    @AfterEach
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    void clean() {
        underTest.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    void nativeQueryFindByAccount_emailTest() {
        ResetPasswordToken resetPasswordToken = createResetPasswordToken();
        Account account = resetPasswordToken.getAccount();
        accountRepository.saveAndFlush(account);
        underTest.saveAndFlush(resetPasswordToken);

        var tokenFound = underTest.findByAccount_email(EMAIL);

        assertTrue(tokenFound.isPresent());
        assertEquals(account, tokenFound.get().getAccount());
        assertEquals(RESET_PASSWORD_TOKEN_VALUE, tokenFound.get().getTokenValue());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    void nativeQueryFindByAccount_emailReturnEmptyOptionalTest() {
        ResetPasswordToken resetPasswordToken = createResetPasswordToken();
        Account account = resetPasswordToken.getAccount();
        accountRepository.saveAndFlush(account);
        underTest.saveAndFlush(resetPasswordToken);

        var tokenWhichDoesNotExist = underTest.findByAccount_email("test_email");

        assertFalse(tokenWhichDoesNotExist.isPresent());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    void nativeQueryFindByTokenValueAndExpirationDateAfterTest() {
        ResetPasswordToken resetPasswordToken = createResetPasswordToken();
        Account account = resetPasswordToken.getAccount();
        accountRepository.saveAndFlush(account);
        underTest.saveAndFlush(resetPasswordToken);

        var tokenFound = underTest.findByTokenValueAndExpirationDateAfter(RESET_PASSWORD_TOKEN_VALUE,
                LocalDateTime.now().plusMinutes(5));

        assertTrue(tokenFound.isPresent());
        assertEquals(RESET_PASSWORD_TOKEN_VALUE, tokenFound.get().getTokenValue());
        assertEquals(account, tokenFound.get().getAccount());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    void nativeQueryFindByTokenValueAndExpirationDateAfterReturnEmptyOptionalWhenTokenValueDoesNotExistTest() {
        ResetPasswordToken resetPasswordToken = createResetPasswordToken();
        Account account = resetPasswordToken.getAccount();
        accountRepository.saveAndFlush(account);
        underTest.saveAndFlush(resetPasswordToken);

        var tokenFound = underTest.findByTokenValueAndExpirationDateAfter("EX1Mple",
                LocalDateTime.now().plusMinutes(5));

        assertFalse(tokenFound.isPresent());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    void nativeQueryFindByTokenValueAndExpirationDateAfterReturnEmptyOptionalWhenTokenIsExpired() {
        ResetPasswordToken resetPasswordToken = createResetPasswordToken();
        Account account = resetPasswordToken.getAccount();
        accountRepository.saveAndFlush(account);
        underTest.saveAndFlush(resetPasswordToken);

        var tokenFound = underTest.findByTokenValueAndExpirationDateAfter(RESET_PASSWORD_TOKEN_VALUE,
                LocalDateTime.now().plusMinutes(16));

        assertFalse(tokenFound.isPresent());
    }
}