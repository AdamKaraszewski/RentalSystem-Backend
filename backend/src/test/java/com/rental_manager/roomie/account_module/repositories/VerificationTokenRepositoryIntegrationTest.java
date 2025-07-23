package com.rental_manager.roomie.account_module.repositories;
import com.rental_manager.roomie.IntegrationTestsBase;
import com.rental_manager.roomie.entities.Account;
import com.rental_manager.roomie.entities.VerificationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static com.rental_manager.roomie.AccountModuleTestUtility.*;

class VerificationTokenRepositoryIntegrationTest extends IntegrationTestsBase {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private VerificationTokenRepository underTest;

    @BeforeEach
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    void clean() {
        underTest.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    void nativeQueryFindByTokenValueAndExpirationDateAfterTest() {
        VerificationToken verificationTokenNo1 = createVerificationTokenLinkedToNotVerifiedAccountWithClientRole();
        Account accountNo1 = verificationTokenNo1.getAccount();
        accountRepository.saveAndFlush(accountNo1);
        underTest.saveAndFlush(verificationTokenNo1);

        var tokenFound = underTest.findByTokenValueAndExpirationDateAfter(VERIFICATION_TOKEN_VALUE, LocalDateTime.now());

        assertTrue(tokenFound.isPresent());
        assertEquals(accountNo1, tokenFound.get().getAccount());
        assertEquals(VERIFICATION_TOKEN_VALUE, tokenFound.get().getTokenValue());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    void nativeQueryFindByTokenValueAndExpirationDateAfterReturnEmptyOptionalWhenTokenValueDoesNotExistTest() {
        var tokenFound = underTest.findByTokenValueAndExpirationDateAfter("ttest", LocalDateTime.now());

        assertFalse(tokenFound.isPresent());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    void nativeQueryFindByTokenValueAndExpirationDateAfterReturnEmptyOptionalWhenTokenIsExpiredTest() {
        VerificationToken verificationTokenNo1 = createVerificationTokenLinkedToNotVerifiedAccountWithClientRole();
        Account accountNo1 = verificationTokenNo1.getAccount();
        accountRepository.saveAndFlush(accountNo1);
        underTest.saveAndFlush(verificationTokenNo1);

        var tokenFound = underTest.findByTokenValueAndExpirationDateAfter(VERIFICATION_TOKEN_VALUE, LocalDateTime.now().plusMinutes(16));

        assertTrue(tokenFound.isPresent());
    }

}