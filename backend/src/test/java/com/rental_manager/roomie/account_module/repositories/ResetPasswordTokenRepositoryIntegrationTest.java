package com.rental_manager.roomie.account_module.repositories;

import com.rental_manager.roomie.IntegrationTestsBase;
import com.rental_manager.roomie.config.database.TransactionManagersIds;
import com.rental_manager.roomie.entities.Account;
import com.rental_manager.roomie.entities.roles.RolesEnum;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.rental_manager.roomie.AccountModuleTestUtility.*;
import static org.junit.jupiter.api.Assertions.*;

class ResetPasswordTokenRepositoryIntegrationTest extends IntegrationTestsBase {

    @Autowired
    private ResetPasswordTokenRepository underTest;

    @Autowired
    private AccountRepository accountRepository;

    private final Account account = createAccount(
            FIRST_NAME_NO_1,
            LAST_NAME_NO_1,
            USERNAME_NO_1,
            EMAIL_NO_1,
            false,
            true,
            new ArrayList<>(List.of(RolesEnum.CLIENT)));

    @BeforeEach
    void setup() {
        accountRepository.saveAndFlush(account);
        underTest.saveAndFlush(createResetPasswordToken(account, RESET_PASSWORD_TOKEN_VALUE));
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            transactionManager = TransactionManagersIds.ACCOUNT_MODULE_TX_MANAGER)
    void derivedQueryFindByAccount_emailTest() {
        var tokenFound = underTest.findByAccount_email(account.getEmail());

        assertTrue(tokenFound.isPresent());
        assertEquals(account, tokenFound.get().getAccount());
        assertEquals(RESET_PASSWORD_TOKEN_VALUE, tokenFound.get().getTokenValue());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            transactionManager = TransactionManagersIds.ACCOUNT_MODULE_TX_MANAGER)
    void derivedQueryFindByAccount_emailReturnEmptyOptionalTest() {
        var tokenWhichDoesNotExist = underTest.findByAccount_email("test_email");

        assertFalse(tokenWhichDoesNotExist.isPresent());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            transactionManager = TransactionManagersIds.ACCOUNT_MODULE_TX_MANAGER)
    void derivedQueryFindByTokenValueAndExpirationDateAfterTest() {
        var tokenFound = underTest.findByTokenValueAndExpirationDateAfter(RESET_PASSWORD_TOKEN_VALUE,
                LocalDateTime.now().plusMinutes(5));

        assertTrue(tokenFound.isPresent());
        assertEquals(RESET_PASSWORD_TOKEN_VALUE, tokenFound.get().getTokenValue());
        assertEquals(account, tokenFound.get().getAccount());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            transactionManager = TransactionManagersIds.ACCOUNT_MODULE_TX_MANAGER)
    void derivedQueryFindByTokenValueAndExpirationDateAfterReturnEmptyOptionalWhenTokenValueDoesNotExistTest() {
        var tokenFound = underTest.findByTokenValueAndExpirationDateAfter("EX1Mple",
                LocalDateTime.now().plusMinutes(5));

        assertFalse(tokenFound.isPresent());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            transactionManager = TransactionManagersIds.ACCOUNT_MODULE_TX_MANAGER)
    void derivedQueryFindByTokenValueAndExpirationDateAfterReturnEmptyOptionalWhenTokenIsExpired() {
        var tokenFound = underTest.findByTokenValueAndExpirationDateAfter(RESET_PASSWORD_TOKEN_VALUE,
                LocalDateTime.now().plusMinutes(16));

        assertFalse(tokenFound.isPresent());
    }
}