package com.rental_manager.roomie.account_module.services.implementations;

import com.rental_manager.roomie.IntegrationTestsBase;
import com.rental_manager.roomie.account_module.repositories.AccountRepository;
import com.rental_manager.roomie.account_module.repositories.VerificationTokenRepository;
import com.rental_manager.roomie.config.database.TransactionManagersIds;
import com.rental_manager.roomie.entities.Account;
import com.rental_manager.roomie.entities.roles.RolesEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = TransactionManagersIds.ACCOUNT_MODULE_TX_MANAGER)
    void clean() {
        verificationTokenRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = TransactionManagersIds.ACCOUNT_MODULE_TX_MANAGER)
    void registerClientTest() {
        Account accountToBeRegistered = createAccount(
                FIRST_NAME_NO_1,
                LAST_NAME_NO_1,
                USERNAME_NO_1,
                EMAIL_NO_1,
                true,
                true,
                new ArrayList<>(List.of(RolesEnum.CLIENT))
        );

        underTest.registerClient(accountToBeRegistered);
        var registeredAccount = accountRepository.findByEmail(EMAIL_NO_1);
        var verificationToken = verificationTokenRepository.findAll().getFirst();

        assertTrue(registeredAccount.isPresent());
        assertEquals(FIRST_NAME_NO_1, registeredAccount.get().getFirstName());
        assertEquals(LAST_NAME_NO_1, registeredAccount.get().getLastName());
        assertEquals(USERNAME_NO_1, registeredAccount.get().getUsername());
        assertEquals(EMAIL_NO_1, registeredAccount.get().getEmail());
        assertEquals(PASSWORD, registeredAccount.get().getPassword());

        assertEquals(1, registeredAccount.get().getRoles().size());

        var clientRole = registeredAccount.get().getRoles().stream().findFirst();
        assertTrue(clientRole.isPresent());
        assertEquals(RolesEnum.CLIENT, clientRole.get().getRole());
        assertTrue(clientRole.get().isActive());

        assertEquals(registeredAccount.get(), verificationToken.getAccount());
    }
}