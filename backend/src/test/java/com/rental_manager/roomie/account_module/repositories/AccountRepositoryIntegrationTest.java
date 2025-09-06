package com.rental_manager.roomie.account_module.repositories;

import com.rental_manager.roomie.IntegrationTestsBase;
import com.rental_manager.roomie.config.database.TransactionManagersIds;
import com.rental_manager.roomie.entities.Account;
import com.rental_manager.roomie.entities.roles.RolesEnum;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.rental_manager.roomie.AccountModuleTestUtility.*;
import static com.rental_manager.roomie.account_module.repositories.specifications.AccountSpecification.usernameOrFirstNameOrLastNameMatches;
import static org.junit.jupiter.api.Assertions.*;

class AccountRepositoryIntegrationTest extends IntegrationTestsBase {

    @Autowired
    private AccountRepository underTest;

    private final Account accountNo1 = createAccount(FIRST_NAME_NO_1, LAST_NAME_NO_1, USERNAME_NO_1, EMAIL_NO_1,
            true, true, new ArrayList<>(List.of(RolesEnum.CLIENT)));
    private final Account accountNo2 = createAccount(FIRST_NAME_NO_2, LAST_NAME_NO_2, USERNAME_NO_2, EMAIL_NO_2,
            true, true, new ArrayList<>(List.of(RolesEnum.CLIENT)));
    private final Account accountNo3 = createAccount(FIRST_NAME_NO_3, LAST_NAME_NO_3, USERNAME_NO_3, EMAIL_NO_3,
            true, true, new ArrayList<>(List.of(RolesEnum.CLIENT)));
    private final Account accountNo4 = createAccount(FIRST_NAME_NO_4, LAST_NAME_NO_4, USERNAME_NO_4, EMAIL_NO_4,
            true, true, new ArrayList<>(List.of(RolesEnum.CLIENT)));
    private final Account accountNo5 = createAccount(FIRST_NAME_NO_5, LAST_NAME_NO_5, USERNAME_NO_5, EMAIL_NO_5,
            true, true, new ArrayList<>(List.of(RolesEnum.CLIENT)));
    private final Account accountNo6 = createAccount(FIRST_NAME_NO_6, LAST_NAME_NO_6, USERNAME_NO_6, EMAIL_NO_6,
            true, true, new ArrayList<>(List.of(RolesEnum.CLIENT)));

    @BeforeEach
    void setup() {
        underTest.saveAndFlush(accountNo1);
        underTest.saveAndFlush(accountNo2);
        underTest.saveAndFlush(accountNo3);
        underTest.saveAndFlush(accountNo4);
        underTest.saveAndFlush(accountNo5);
        underTest.saveAndFlush(accountNo6);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            transactionManager = TransactionManagersIds.ACCOUNT_MODULE_TX_MANAGER)
    void derivedQueryFindByEmailTest() {
        var accountWithExistingEmail = underTest.findByEmail(EMAIL_NO_2);

        assertTrue(accountWithExistingEmail.isPresent());
        assertEquals(EMAIL_NO_2, accountWithExistingEmail.get().getEmail());
        assertEquals(USERNAME_NO_2, accountWithExistingEmail.get().getUsername());
        assertEquals(FIRST_NAME_NO_2, accountWithExistingEmail.get().getFirstName());
        assertEquals(LAST_NAME_NO_2, accountWithExistingEmail.get().getLastName());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            transactionManager = TransactionManagersIds.ACCOUNT_MODULE_TX_MANAGER)
    void derivedQueryFindByEmailReturnEmptyOptionalTest() {
        var accountWithNotExistingEmail = underTest.findByEmail("test");

        assertFalse(accountWithNotExistingEmail.isPresent());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            transactionManager = TransactionManagersIds.ACCOUNT_MODULE_TX_MANAGER)
    void specificationQueryFindAllMatchingUsernameOrFirstNameOrLastNameReturnAllSortByUsernameAsc() {
        List<String> phrases = new ArrayList<>();
        phrases.add("");

        Pageable pageable = PageRequest.of(0, 4, Sort.Direction.ASC, USERNAME_FIELD);
        Page<Account> resultPage = underTest.findAll(usernameOrFirstNameOrLastNameMatches(phrases), pageable);
        List<Account> accountsOnPage = resultPage.getContent();

        assertEquals(4, accountsOnPage.size());
        assertEquals(6, resultPage.getTotalElements());
        assertEquals(2, resultPage.getTotalPages());

        assertEquals(USERNAME_NO_1, accountsOnPage.getFirst().getUsername());
        assertEquals(USERNAME_NO_2, accountsOnPage.get(1).getUsername());
        assertEquals(USERNAME_NO_3, accountsOnPage.get(2).getUsername());
        assertEquals(USERNAME_NO_4, accountsOnPage.get(3).getUsername());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            transactionManager = TransactionManagersIds.ACCOUNT_MODULE_TX_MANAGER)
    void specificationQueryFindAllMatchingUsernameOrFirstNameOrLastNameReturnAllSortByUsernameDesc() {
        List<String> phrases = new ArrayList<>();
        phrases.add("");

        Pageable pageable = PageRequest.of(0, 4, Sort.Direction.DESC, USERNAME_FIELD);
        Page<Account> resultPage = underTest.findAll(usernameOrFirstNameOrLastNameMatches(phrases), pageable);
        List<Account> accountsOnPage = resultPage.getContent();

        assertEquals(4, accountsOnPage.size());
        assertEquals(6, resultPage.getTotalElements());
        assertEquals(2, resultPage.getTotalPages());

        assertEquals(USERNAME_NO_6, accountsOnPage.getFirst().getUsername());
        assertEquals(USERNAME_NO_5, accountsOnPage.get(1).getUsername());
        assertEquals(USERNAME_NO_4, accountsOnPage.get(2).getUsername());
        assertEquals(USERNAME_NO_3, accountsOnPage.get(3).getUsername());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            transactionManager = TransactionManagersIds.ACCOUNT_MODULE_TX_MANAGER)
    void specificationQueryFindAllMatchingUsernameOrFirstNameOrLastNameReturnAllSortByFirstNameAsc() {
        List<String> phrases = new ArrayList<>();
        phrases.add("");

        Pageable pageable = PageRequest.of(0, 4, Sort.Direction.ASC, FIRST_NAME_FIELD);
        Page<Account> resultPage = underTest.findAll(usernameOrFirstNameOrLastNameMatches(phrases), pageable);
        List<Account> accountsOnPage = resultPage.getContent();

        assertEquals(4, accountsOnPage.size());
        assertEquals(6, resultPage.getTotalElements());
        assertEquals(2, resultPage.getTotalPages());

        assertEquals(FIRST_NAME_NO_1, accountsOnPage.getFirst().getFirstName());
        assertEquals(FIRST_NAME_NO_2, accountsOnPage.get(1).getFirstName());
        assertEquals(FIRST_NAME_NO_3, accountsOnPage.get(2).getFirstName());
        assertEquals(FIRST_NAME_NO_4, accountsOnPage.get(3).getFirstName());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            transactionManager = TransactionManagersIds.ACCOUNT_MODULE_TX_MANAGER)
    void specificationQueryFindAllMatchingUsernameOrFirstNameOrLastNameReturnAllSortByFirstNameDesc() {
        List<String> phrases = new ArrayList<>();
        phrases.add("");

        Pageable pageable = PageRequest.of(0, 4, Sort.Direction.DESC, LAST_NAME_FIELD);
        Page<Account> resultPage = underTest.findAll(usernameOrFirstNameOrLastNameMatches(phrases), pageable);
        List<Account> accountsOnPage = resultPage.getContent();

        assertEquals(4, accountsOnPage.size());
        assertEquals(6, resultPage.getTotalElements());
        assertEquals(2, resultPage.getTotalPages());

        assertEquals(FIRST_NAME_NO_6, accountsOnPage.getFirst().getFirstName());
        assertEquals(FIRST_NAME_NO_5, accountsOnPage.get(1).getFirstName());
        assertEquals(FIRST_NAME_NO_4, accountsOnPage.get(2).getFirstName());
        assertEquals(FIRST_NAME_NO_3, accountsOnPage.get(3).getFirstName());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            transactionManager = TransactionManagersIds.ACCOUNT_MODULE_TX_MANAGER)
    void specificationQueryFindAllMatchingUsernameOrFirstNameOrLastNameReturnAllSortByLastNameAsc() {
        List<String> phrases = new ArrayList<>();
        phrases.add("");

        Pageable pageable = PageRequest.of(0, 4, Sort.Direction.ASC, LAST_NAME_FIELD);
        Page<Account> resultPage = underTest.findAll(usernameOrFirstNameOrLastNameMatches(phrases), pageable);
        List<Account> accountsOnPage = resultPage.getContent();

        assertEquals(4, accountsOnPage.size());
        assertEquals(6, resultPage.getTotalElements());
        assertEquals(2, resultPage.getTotalPages());

        assertEquals(LAST_NAME_NO_1, accountsOnPage.getFirst().getLastName());
        assertEquals(LAST_NAME_NO_2, accountsOnPage.get(1).getLastName());
        assertEquals(LAST_NAME_NO_3, accountsOnPage.get(2).getLastName());
        assertEquals(LAST_NAME_NO_4, accountsOnPage.get(3).getLastName());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            transactionManager = TransactionManagersIds.ACCOUNT_MODULE_TX_MANAGER)
    void specificationQueryFindAllMatchingUsernameOrFirstNameOrLastNameReturnAllSortByLastNameDesc() {
        List<String> phrases = new ArrayList<>();
        phrases.add("");

        Pageable pageable = PageRequest.of(0, 4, Sort.Direction.DESC, LAST_NAME_FIELD);
        Page<Account> resultPage = underTest.findAll(usernameOrFirstNameOrLastNameMatches(phrases), pageable);
        List<Account> accountsOnPage = resultPage.getContent();

        assertEquals(4, accountsOnPage.size());
        assertEquals(6, resultPage.getTotalElements());
        assertEquals(2, resultPage.getTotalPages());

        assertEquals(LAST_NAME_NO_6, accountsOnPage.getFirst().getLastName());
        assertEquals(LAST_NAME_NO_5, accountsOnPage.get(1).getLastName());
        assertEquals(LAST_NAME_NO_4, accountsOnPage.get(2).getLastName());
        assertEquals(LAST_NAME_NO_3, accountsOnPage.get(3).getLastName());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            transactionManager = TransactionManagersIds.ACCOUNT_MODULE_TX_MANAGER)
    void specificationQueryFindAllMatchingUsernameOrFirstNameOrLastNameReturnAllMatchingFirstname() {
        List<String> phrases1 = new ArrayList<>();
        List<String> phrases2 = new ArrayList<>();
        phrases1.add(FIRST_NAME_NO_3);
        phrases2.add(FIRST_NAME_NO_3);
        phrases2.add(FIRST_NAME_NO_4);

        Pageable pageable = PageRequest.of(0, 3, Sort.Direction.ASC, USERNAME_FIELD);
        Page<Account> resultPage1 = underTest.findAll(usernameOrFirstNameOrLastNameMatches(phrases1), pageable);
        Page<Account> resultPage2 = underTest.findAll(usernameOrFirstNameOrLastNameMatches(phrases2), pageable);

        List<Account> accountsOnPage1 = resultPage1.getContent();
        List<Account> accountsOnPage2 = resultPage2.getContent();

        assertEquals(1, accountsOnPage1.size());
        assertEquals(1, resultPage1.getTotalElements());
        assertEquals(1, resultPage1.getTotalPages());
        assertEquals(FIRST_NAME_NO_3, accountsOnPage1.getFirst().getFirstName());

        assertEquals(2, accountsOnPage2.size());
        assertEquals(2, resultPage2.getTotalElements());
        assertEquals(1, resultPage2.getTotalPages());
        assertEquals(FIRST_NAME_NO_3, accountsOnPage2.getFirst().getFirstName());
        assertEquals(FIRST_NAME_NO_4, accountsOnPage2.getLast().getFirstName());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            transactionManager = TransactionManagersIds.ACCOUNT_MODULE_TX_MANAGER)
    void specificationQueryFindAllMatchingUsernameOrFirstNameOrLastNameReturnAllMatchingLastname() {
        List<String> phrases1 = new ArrayList<>();
        List<String> phrases2 = new ArrayList<>();
        phrases1.add(LAST_NAME_NO_5);
        phrases2.add(LAST_NAME_NO_5);
        phrases2.add(LAST_NAME_NO_6);

        Pageable pageable = PageRequest.of(0, 2, Sort.Direction.ASC, USERNAME_FIELD);
        Page<Account> resultPage1 = underTest.findAll(usernameOrFirstNameOrLastNameMatches(phrases1), pageable);
        Page<Account> resultPage2 = underTest.findAll(usernameOrFirstNameOrLastNameMatches(phrases2), pageable);

        List<Account> accountsOnPage1 = resultPage1.getContent();
        List<Account> accountsOnPage2 = resultPage2.getContent();

        assertEquals(1, accountsOnPage1.size());
        assertEquals(1, resultPage1.getTotalElements());
        assertEquals(1, resultPage1.getTotalPages());
        assertEquals(USERNAME_NO_5, accountsOnPage1.getFirst().getUsername());

        assertEquals(2, accountsOnPage2.size());
        assertEquals(2, resultPage2.getTotalElements());
        assertEquals(1, resultPage2.getTotalPages());
        assertEquals(USERNAME_NO_5, accountsOnPage2.getFirst().getUsername());
        assertEquals(USERNAME_NO_6, accountsOnPage2.getLast().getUsername());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            transactionManager = TransactionManagersIds.ACCOUNT_MODULE_TX_MANAGER)
    void specificationQueryFindAllMatchingUsernameOrFirstNameOrLastNameReturnAllMatchingUsername() {
        List<String> phrases1 = new ArrayList<>();
        List<String> phrases2 = new ArrayList<>();
        phrases1.add(USERNAME_NO_5);
        phrases2.add(USERNAME_NO_5);
        phrases2.add(USERNAME_NO_6);

        Pageable pageable = PageRequest.of(0, 3, Sort.Direction.ASC, USERNAME_FIELD);
        Page<Account> resultPage1 = underTest.findAll(usernameOrFirstNameOrLastNameMatches(phrases1), pageable);
        Page<Account> resultPage2 = underTest.findAll(usernameOrFirstNameOrLastNameMatches(phrases2), pageable);

        List<Account> accountsOnPage1 = resultPage1.getContent();
        List<Account> accountsOnPage2 = resultPage2.getContent();

        assertEquals(1, accountsOnPage1.size());
        assertEquals(1, resultPage1.getTotalElements());
        assertEquals(1, resultPage1.getTotalPages());
        assertEquals(USERNAME_NO_5, accountsOnPage1.getFirst().getUsername());

        assertEquals(2, accountsOnPage2.size());
        assertEquals(2, resultPage2.getTotalElements());
        assertEquals(1, resultPage2.getTotalPages());
        assertEquals(USERNAME_NO_5, accountsOnPage2.getFirst().getUsername());
        assertEquals(USERNAME_NO_6, accountsOnPage2.getLast().getUsername());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            transactionManager = TransactionManagersIds.ACCOUNT_MODULE_TX_MANAGER)
    void specificationQueryFindAllMatchingUsernameOrFirstNameOrLastnameReturnEmptyList() {
        List<String> phrases = new ArrayList<>();
        phrases.add("username_which_does_not_exist");

        Pageable pageable = PageRequest.of(0, 3, Sort.Direction.ASC, USERNAME_FIELD);
        Page<Account> resultPage = underTest.findAll(usernameOrFirstNameOrLastNameMatches(phrases), pageable);

        List<Account> accountsOnPage = resultPage.getContent();

        assertEquals(0, accountsOnPage.size());
        assertEquals(0, resultPage.getTotalElements());
        assertEquals(0, resultPage.getTotalPages());
        assertTrue(resultPage.isEmpty());
    }
}