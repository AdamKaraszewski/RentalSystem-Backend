package com.rental_manager.roomie.account_module.repositories;

import com.rental_manager.roomie.IntegrationTestsBase;
import com.rental_manager.roomie.entities.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.rental_manager.roomie.AccountModuleTestUtility.createNotVerifiedAccountWithClientRole;
import static org.junit.jupiter.api.Assertions.*;

class AccountRepositoryIntegrationTest extends IntegrationTestsBase {

    @Autowired
    private AccountRepository underTest;

    private final Account accountNo1 = createNotVerifiedAccountWithClientRole("username_no_1", "email.no1@example.com",
            "first_name_no_1", "last_name_no_1");
    private final Account accountNo2 = createNotVerifiedAccountWithClientRole("username_no_2", "email.no2@example.com",
            "first_name_no_2", "last_name_no_2");
    private final Account accountNo3 = createNotVerifiedAccountWithClientRole("username_no_3", "email.no3@example.com",
            "first_name_no_3", "last_name_no_3");
    private final Account accountNo4 = createNotVerifiedAccountWithClientRole("username_no_4", "email.no4@example.com",
            "first_name_no_4", "last_name_no_4");

    @BeforeEach
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    void setup() {
        underTest.saveAndFlush(accountNo1);
        underTest.saveAndFlush(accountNo2);
        underTest.saveAndFlush(accountNo3);
        underTest.saveAndFlush(accountNo4);
    }

    @AfterEach
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    void clean() {
        underTest.deleteAll();
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    void nativeQueryFindByEmailTest() {
        var accountWithExistingEmail = underTest.findByEmail("email.no2@example.com");

        assertTrue(accountWithExistingEmail.isPresent());
        assertEquals("email.no2@example.com", accountWithExistingEmail.get().getEmail());
        assertEquals("username_no_2", accountWithExistingEmail.get().getUsername());
        assertEquals("first_name_no_2", accountWithExistingEmail.get().getFirstName());
        assertEquals("last_name_no_2", accountWithExistingEmail.get().getLastName());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    void nativeQueryFindByEmailReturnOptionalTest() {
        var accountWithNotExistingEmail = underTest.findByEmail("test");

        assertFalse(accountWithNotExistingEmail.isPresent());
    }
}