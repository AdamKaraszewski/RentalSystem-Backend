package com.rental_manager.roomie.account_module.repositories;

import com.rental_manager.roomie.AccountModuleTestUtility;
import com.rental_manager.roomie.IntegrationTestsBase;
import com.rental_manager.roomie.entities.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountRepositoryIntegrationTest extends IntegrationTestsBase {

    @Autowired
    private AccountRepository underTest;

    private final Account accountNo1 = AccountModuleTestUtility.createNotVerifiedAccount("username_no_1",
            "email.no1@example.com", "first_name_no_1", "last_name_no_1");
    private final Account accountNo2 = AccountModuleTestUtility.createNotVerifiedAccount("username_no_2",
            "email.no2@example.com", "first_name_no_2", "last_name_no_2");
    private final Account accountNo3 = AccountModuleTestUtility.createNotVerifiedAccount("username_no_3",
            "email.no3@example.com", "first_name_no_3", "last_name_no_3");
    private final Account accountNo4 = AccountModuleTestUtility.createNotVerifiedAccount("username_no_4",
            "email.no4@example.com", "first_name_no_4", "last_name_no_4");
    private final Account accountNo5 = AccountModuleTestUtility.createNotVerifiedAccount("a_username_no_5",
            "email.no5@example.com", "first_name_no_5", "last_name_no_5");
    private final Account accountNo6 = AccountModuleTestUtility.createNotVerifiedAccount("x_username_no_6",
            "email.no6@example.com", "first_name_no_6", "last_name_no_6");


    @BeforeEach
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    void setup() {
        underTest.deleteAll();

        underTest.saveAndFlush(accountNo1);
        underTest.saveAndFlush(accountNo2);
        underTest.saveAndFlush(accountNo3);
        underTest.saveAndFlush(accountNo4);
        underTest.saveAndFlush(accountNo5);
        underTest.saveAndFlush(accountNo6);
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
    void nativeQueryFindByEmailReturnEmptyOptionalTest() {
        var accountWithNotExistingEmail = underTest.findByEmail("test");

        assertFalse(accountWithNotExistingEmail.isPresent());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    void nativeQueryFindAllByOrderByUsernameAscTest() {
        Pageable pageable1 = PageRequest.of(0, 4);
        Pageable pageable2 = PageRequest.of(5, 1);
        Pageable pageable3 = PageRequest.of(2, 2);
        Pageable pageable4 = PageRequest.of(10, 1);

        Page<Account> resultPage1 = underTest.findAllByOrderByUsernameAsc(pageable1);
        Page<Account> resultPage2 = underTest.findAllByOrderByUsernameAsc(pageable2);
        Page<Account> resultPage3 = underTest.findAllByOrderByUsernameAsc(pageable3);
        Page<Account> resultPage4 = underTest.findAllByOrderByUsernameAsc(pageable4);

        List<Account> accounts1 = resultPage1.getContent();
        List<Account> accounts2 = resultPage2.getContent();
        List<Account> accounts3 = resultPage3.getContent();
        List<Account> accounts4 = resultPage4.getContent();

        assertEquals(4, accounts1.size());
        assertEquals("a_username_no_5", accounts1.getFirst().getUsername());
        assertEquals("username_no_1", accounts1.get(1).getUsername());
        assertEquals("username_no_2", accounts1.get(2).getUsername());
        assertEquals("username_no_3", accounts1.get(3).getUsername());

        assertEquals(1, accounts2.size());
        assertEquals("x_username_no_6", accounts2.getFirst().getUsername());

        assertEquals(2, accounts3.size());
        assertEquals("username_no_4", accounts3.getFirst().getUsername());
        assertEquals("x_username_no_6", accounts3.get(1).getUsername());

        assertEquals(0, accounts4.size());
        assertTrue(resultPage4.isEmpty());
    }
}