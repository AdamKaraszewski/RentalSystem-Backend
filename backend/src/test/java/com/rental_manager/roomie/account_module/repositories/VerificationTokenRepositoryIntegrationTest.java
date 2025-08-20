package com.rental_manager.roomie.account_module.repositories;
import com.rental_manager.roomie.IntegrationTestsBase;
import com.rental_manager.roomie.config.database.TransactionManagersIds;
import com.rental_manager.roomie.entities.Account;
import com.rental_manager.roomie.entities.VerificationToken;
import com.rental_manager.roomie.entities.roles.RolesEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static com.rental_manager.roomie.AccountModuleTestUtility.*;

class VerificationTokenRepositoryIntegrationTest extends IntegrationTestsBase {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private VerificationTokenRepository underTest;

    private final Account account = createAccount(
            FIRST_NAME_NO_1,
            LAST_NAME_NO_1,
            USERNAME_NO_1,
            EMAIL_NO_1,
            true,
            true,
            new ArrayList<>(List.of(RolesEnum.CLIENT))
    );

    private final VerificationToken verificationToken = createVerificationToken(
            account,
            VERIFICATION_TOKEN_VALUE
    );

    @BeforeEach
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = TransactionManagersIds.ACCOUNT_MODULE_TX_MANAGER)
    void setup() {
        accountRepository.saveAndFlush(account);
        underTest.saveAndFlush(verificationToken);
    }

    @AfterEach
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = TransactionManagersIds.ACCOUNT_MODULE_TX_MANAGER)
    void clear() {
        underTest.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = TransactionManagersIds.ACCOUNT_MODULE_TX_MANAGER)
    void derivedQueryFindByTokenValueAndExpirationDateAfterTest() {
        var tokenFound = underTest.findByTokenValueAndExpirationDateAfter(VERIFICATION_TOKEN_VALUE, LocalDateTime.now());

        assertTrue(tokenFound.isPresent());
        assertEquals(account, tokenFound.get().getAccount());
        assertEquals(VERIFICATION_TOKEN_VALUE, tokenFound.get().getTokenValue());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = TransactionManagersIds.ACCOUNT_MODULE_TX_MANAGER)
    void derivedQueryFindByTokenValueAndExpirationDateAfterReturnEmptyOptionalWhenTokenValueDoesNotExistTest() {
        var tokenFound = underTest.findByTokenValueAndExpirationDateAfter("ttest", LocalDateTime.now());

        assertFalse(tokenFound.isPresent());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = TransactionManagersIds.ACCOUNT_MODULE_TX_MANAGER)
    void derivedQueryFindByTokenValueAndExpirationDateAfterReturnEmptyOptionalWhenTokenIsExpiredTest() {
        var tokenFound = underTest.findByTokenValueAndExpirationDateAfter(VERIFICATION_TOKEN_VALUE, LocalDateTime.now().plusMinutes(16));

        assertTrue(tokenFound.isPresent());
    }

}