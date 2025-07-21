package com.rental_manager.roomie.account_module.services.implementations;

import com.rental_manager.roomie.IntegrationTestsBase;
import com.rental_manager.roomie.account_module.repositories.AccountRepository;
import com.rental_manager.roomie.account_module.repositories.VerificationTokenRepository;
import com.rental_manager.roomie.entities.Account;
import com.rental_manager.roomie.entities.roles.RolesEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static com.rental_manager.roomie.AccountModuleTestUtility.*;

class AccountServiceIntegrationTest extends IntegrationTestsBase {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private AccountService underTest;

    @AfterEach
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    void clean() {
        verificationTokenRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    void registerClientTest() {
        Account accountToBeRegistered = createNotVerifiedAccount();

        underTest.registerClient(accountToBeRegistered);
        var registeredAccount = accountRepository.findByEmail(EMAIL);
        var verificationToken = verificationTokenRepository.findAll().getFirst();

        assertTrue(registeredAccount.isPresent());
        assertEquals(FIRST_NAME, registeredAccount.get().getFirstName());
        assertEquals(LAST_NAME, registeredAccount.get().getLastName());
        assertEquals(USERNAME, registeredAccount.get().getUsername());
        assertEquals(EMAIL, registeredAccount.get().getEmail());
        assertEquals(PASSWORD, registeredAccount.get().getPassword());

        assertEquals(1, registeredAccount.get().getRoles().size());

        var clientRole = registeredAccount.get().getRoles().stream().findFirst();
        assertTrue(clientRole.isPresent());
        assertEquals(RolesEnum.CLIENT, clientRole.get().getRole());
        assertTrue(clientRole.get().isActive());

        assertEquals(registeredAccount.get(), verificationToken.getAccount());
    }
}