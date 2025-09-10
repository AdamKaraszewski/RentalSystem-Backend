package com.rental_manager.roomie.account_module.services.implementations;

import com.rental_manager.roomie.IntegrationTestsBase;
import com.rental_manager.roomie.account_module.repositories.AccountRepository;
import com.rental_manager.roomie.account_module.repositories.VerificationTokenRepository;
import com.rental_manager.roomie.config.database.TransactionManagersIds;
import com.rental_manager.roomie.entities.Account;
import com.rental_manager.roomie.entities.roles.RolesEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import static com.rental_manager.roomie.config.database.InitDataSourceConfig.INIT_DS_NAME;
import static org.junit.jupiter.api.Assertions.*;
import static com.rental_manager.roomie.AccountModuleTestUtility.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

class AccountServiceIntegrationTest extends IntegrationTestsBase {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private AccountService underTest;

    private static final String CLEAR_SQL = "/account_service_it_clear.sql";

    @Test
    @Transactional(
            transactionManager = TransactionManagersIds.ACCOUNT_MODULE_TX_MANAGER)
    @Sql(scripts = CLEAR_SQL, executionPhase = AFTER_TEST_METHOD,
            config = @SqlConfig(
                    dataSource = INIT_DS_NAME,
                    transactionManager = TransactionManagersIds.INIT_TX_MANAGER,
                    transactionMode = ISOLATED
            ))
    void registerClientTest() {
        Account accountToBeRegistered = new Account(
                FIRST_NAME_NO_1,
                LAST_NAME_NO_1,
                USERNAME_NO_1,
                EMAIL_NO_1
        );

        underTest.registerClient(accountToBeRegistered, PASSWORD);
        var registeredAccount = accountRepository.findByEmail(EMAIL_NO_1);
        var verificationToken = verificationTokenRepository.findAll().getFirst();

        assertTrue(registeredAccount.isPresent());
        assertEquals(FIRST_NAME_NO_1, registeredAccount.get().getFirstName());
        assertEquals(LAST_NAME_NO_1, registeredAccount.get().getLastName());
        assertEquals(USERNAME_NO_1, registeredAccount.get().getUsername());
        assertEquals(EMAIL_NO_1, registeredAccount.get().getEmail());
        assertNotNull(registeredAccount.get().getPassword());

        assertEquals(1, registeredAccount.get().getRoles().size());

        var clientRole = registeredAccount.get().getRoles().stream().findFirst();
        assertTrue(clientRole.isPresent());
        assertEquals(RolesEnum.CLIENT, clientRole.get().getRole());
        assertTrue(clientRole.get().isActive());

        assertEquals(registeredAccount.get(), verificationToken.getAccount());
    }
}