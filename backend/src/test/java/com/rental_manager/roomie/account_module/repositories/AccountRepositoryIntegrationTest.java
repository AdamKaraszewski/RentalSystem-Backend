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
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.rental_manager.roomie.account_module.repositories.specifications.AccountSpecification.usernameOrFirstNameOrLastNameMatches;
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
    void specificationQueryFindAllMatchingUsernameOrFirstNameOrLastNameReturnAllSortByUsernameAsc() {
        List<String> phrases = new ArrayList<>();
        phrases.add("");

        Pageable pageable = PageRequest.of(0, 4, Sort.Direction.ASC, "username");
        Page<Account> resultPage = underTest.findAll(usernameOrFirstNameOrLastNameMatches(phrases), pageable);
        List<Account> accountsOnPage = resultPage.getContent();

        assertEquals(4, accountsOnPage.size());
        assertEquals(6, resultPage.getTotalElements());
        assertEquals(2, resultPage.getTotalPages());

        assertEquals("a_username_no_5", accountsOnPage.getFirst().getUsername());
        assertEquals("username_no_1", accountsOnPage.get(1).getUsername());
        assertEquals("username_no_2", accountsOnPage.get(2).getUsername());
        assertEquals("username_no_3", accountsOnPage.get(3).getUsername());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    void specificationQueryFindAllMatchingUsernameOrFirstNameOrLastNameReturnAllSortByUsernameDesc() {
        List<String> phrases = new ArrayList<>();
        phrases.add("");

        Pageable pageable = PageRequest.of(0, 4, Sort.Direction.DESC, "username");
        Page<Account> resultPage = underTest.findAll(usernameOrFirstNameOrLastNameMatches(phrases), pageable);
        List<Account> accountsOnPage = resultPage.getContent();

        assertEquals(4, accountsOnPage.size());
        assertEquals(6, resultPage.getTotalElements());
        assertEquals(2, resultPage.getTotalPages());

        assertEquals("x_username_no_6", accountsOnPage.getFirst().getUsername());
        assertEquals("username_no_4", accountsOnPage.get(1).getUsername());
        assertEquals("username_no_3", accountsOnPage.get(2).getUsername());
        assertEquals("username_no_2", accountsOnPage.get(3).getUsername());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    void specificationQueryFindAllMatchingUsernameOrFirstNameOrLastNameReturnAllSortByFirstNameAsc() {
        List<String> phrases = new ArrayList<>();
        phrases.add("");

        Pageable pageable = PageRequest.of(0, 4, Sort.Direction.ASC, "firstName");
        Page<Account> resultPage = underTest.findAll(usernameOrFirstNameOrLastNameMatches(phrases), pageable);
        List<Account> accountsOnPage = resultPage.getContent();

        assertEquals(4, accountsOnPage.size());
        assertEquals(6, resultPage.getTotalElements());
        assertEquals(2, resultPage.getTotalPages());

        assertEquals("first_name_no_1", accountsOnPage.getFirst().getFirstName());
        assertEquals("first_name_no_2", accountsOnPage.get(1).getFirstName());
        assertEquals("first_name_no_3", accountsOnPage.get(2).getFirstName());
        assertEquals("first_name_no_4", accountsOnPage.get(3).getFirstName());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    void specificationQueryFindAllMatchingUsernameOrFirstNameOrLastNameReturnAllSortByFirstNameDesc() {
        List<String> phrases = new ArrayList<>();
        phrases.add("");

        Pageable pageable = PageRequest.of(0, 4, Sort.Direction.DESC, "lastName");
        Page<Account> resultPage = underTest.findAll(usernameOrFirstNameOrLastNameMatches(phrases), pageable);
        List<Account> accountsOnPage = resultPage.getContent();

        assertEquals(4, accountsOnPage.size());
        assertEquals(6, resultPage.getTotalElements());
        assertEquals(2, resultPage.getTotalPages());

        assertEquals("first_name_no_6", accountsOnPage.getFirst().getFirstName());
        assertEquals("first_name_no_5", accountsOnPage.get(1).getFirstName());
        assertEquals("first_name_no_4", accountsOnPage.get(2).getFirstName());
        assertEquals("first_name_no_3", accountsOnPage.get(3).getFirstName());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    void specificationQueryFindAllMatchingUsernameOrFirstNameOrLastNameReturnAllSortByLastNameAsc() {
        List<String> phrases = new ArrayList<>();
        phrases.add("");

        Pageable pageable = PageRequest.of(0, 4, Sort.Direction.ASC, "lastName");
        Page<Account> resultPage = underTest.findAll(usernameOrFirstNameOrLastNameMatches(phrases), pageable);
        List<Account> accountsOnPage = resultPage.getContent();

        assertEquals(4, accountsOnPage.size());
        assertEquals(6, resultPage.getTotalElements());
        assertEquals(2, resultPage.getTotalPages());

        assertEquals("last_name_no_1", accountsOnPage.getFirst().getLastName());
        assertEquals("last_name_no_2", accountsOnPage.get(1).getLastName());
        assertEquals("last_name_no_3", accountsOnPage.get(2).getLastName());
        assertEquals("last_name_no_4", accountsOnPage.get(3).getLastName());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    void specificationQueryFindAllMatchingUsernameOrFirstNameOrLastNameReturnAllSortByLastNameDesc() {
        List<String> phrases = new ArrayList<>();
        phrases.add("");

        Pageable pageable = PageRequest.of(0, 4, Sort.Direction.DESC, "lastName");
        Page<Account> resultPage = underTest.findAll(usernameOrFirstNameOrLastNameMatches(phrases), pageable);
        List<Account> accountsOnPage = resultPage.getContent();

        assertEquals(4, accountsOnPage.size());
        assertEquals(6, resultPage.getTotalElements());
        assertEquals(2, resultPage.getTotalPages());

        assertEquals("last_name_no_6", accountsOnPage.getFirst().getLastName());
        assertEquals("last_name_no_5", accountsOnPage.get(1).getLastName());
        assertEquals("last_name_no_4", accountsOnPage.get(2).getLastName());
        assertEquals("last_name_no_3", accountsOnPage.get(3).getLastName());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    void specificationQueryFindAllMatchingUsernameOrFirstNameOrLastNameReturnAllMatchingFirstname() {
        List<String> phrases1 = new ArrayList<>();
        List<String> phrases2 = new ArrayList<>();
        phrases1.add("first_name_no_3");
        phrases2.add("first_name_no_3");
        phrases2.add("first_name_no_4");

        Pageable pageable = PageRequest.of(0, 3, Sort.Direction.ASC, "username");
        Page<Account> resultPage1 = underTest.findAll(usernameOrFirstNameOrLastNameMatches(phrases1), pageable);
        Page<Account> resultPage2 = underTest.findAll(usernameOrFirstNameOrLastNameMatches(phrases2), pageable);

        List<Account> accountsOnPage1 = resultPage1.getContent();
        List<Account> accountsOnPage2 = resultPage2.getContent();

        assertEquals(1, accountsOnPage1.size());
        assertEquals(1, resultPage1.getTotalElements());
        assertEquals(1, resultPage1.getTotalPages());
        assertEquals("first_name_no_3", accountsOnPage1.getFirst().getFirstName());

        assertEquals(2, accountsOnPage2.size());
        assertEquals(2, resultPage2.getTotalElements());
        assertEquals(1, resultPage2.getTotalPages());
        assertEquals("first_name_no_3", accountsOnPage2.getFirst().getFirstName());
        assertEquals("first_name_no_4", accountsOnPage2.getLast().getFirstName());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    void specificationQueryFindAllMatchingUsernameOrFirstNameOrLastNameReturnAllMatchingLastname() {
        List<String> phrases1 = new ArrayList<>();
        List<String> phrases2 = new ArrayList<>();
        phrases1.add("last_name_no_5");
        phrases2.add("last_name_no_5");
        phrases2.add("last_name_no_6");

        Pageable pageable = PageRequest.of(0, 2, Sort.Direction.ASC, "username");
        Page<Account> resultPage1 = underTest.findAll(usernameOrFirstNameOrLastNameMatches(phrases1), pageable);
        Page<Account> resultPage2 = underTest.findAll(usernameOrFirstNameOrLastNameMatches(phrases2), pageable);

        List<Account> accountsOnPage1 = resultPage1.getContent();
        List<Account> accountsOnPage2 = resultPage2.getContent();

        assertEquals(1, accountsOnPage1.size());
        assertEquals(1, resultPage1.getTotalElements());
        assertEquals(1, resultPage1.getTotalPages());
        assertEquals("a_username_no_5", accountsOnPage1.getFirst().getUsername());

        assertEquals(2, accountsOnPage2.size());
        assertEquals(2, resultPage2.getTotalElements());
        assertEquals(1, resultPage2.getTotalPages());
        assertEquals("a_username_no_5", accountsOnPage2.getFirst().getUsername());
        assertEquals("x_username_no_6", accountsOnPage2.getLast().getUsername());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    void specificationQueryFindAllMatchingUsernameOrFirstNameOrLastNameReturnAllMatchingUsername() {
        List<String> phrases1 = new ArrayList<>();
        List<String> phrases2 = new ArrayList<>();
        phrases1.add("a_us");
        phrases2.add("a_us");
        phrases2.add("x_us");

        Pageable pageable = PageRequest.of(0, 3, Sort.Direction.ASC, "username");
        Page<Account> resultPage1 = underTest.findAll(usernameOrFirstNameOrLastNameMatches(phrases1), pageable);
        Page<Account> resultPage2 = underTest.findAll(usernameOrFirstNameOrLastNameMatches(phrases2), pageable);

        List<Account> accountsOnPage1 = resultPage1.getContent();
        List<Account> accountsOnPage2 = resultPage2.getContent();

        assertEquals(1, accountsOnPage1.size());
        assertEquals(1, resultPage1.getTotalElements());
        assertEquals(1, resultPage1.getTotalPages());
        assertEquals("a_username_no_5", accountsOnPage1.getFirst().getUsername());

        assertEquals(2, accountsOnPage2.size());
        assertEquals(2, resultPage2.getTotalElements());
        assertEquals(1, resultPage2.getTotalPages());
        assertEquals("a_username_no_5", accountsOnPage2.getFirst().getUsername());
        assertEquals("x_username_no_6", accountsOnPage2.getLast().getUsername());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    void specificationQueryFindAllMatchingUsernameOrFirstNameOrLastnameReturnEmptyList() {
        List<String> phrases = new ArrayList<>();
        phrases.add("username_which_does_not_exist");

        Pageable pageable = PageRequest.of(0, 3, Sort.Direction.ASC, "username");
        Page<Account> resultPage = underTest.findAll(usernameOrFirstNameOrLastNameMatches(phrases), pageable);

        List<Account> accountsOnPage = resultPage.getContent();

        assertEquals(0, accountsOnPage.size());
        assertEquals(0, resultPage.getTotalElements());
        assertEquals(0, resultPage.getTotalPages());
        assertTrue(resultPage.isEmpty());
    }
}