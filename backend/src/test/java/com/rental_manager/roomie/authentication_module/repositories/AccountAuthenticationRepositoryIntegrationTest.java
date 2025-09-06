package com.rental_manager.roomie.authentication_module.repositories;

import com.rental_manager.roomie.IntegrationTestsBase;
import com.rental_manager.roomie.config.database.TransactionManagersIds;
import com.rental_manager.roomie.entities.Account;
import com.rental_manager.roomie.entities.roles.RolesEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.rental_manager.roomie.AccountModuleTestUtility.*;
import static com.rental_manager.roomie.config.database.InitDataSourceConfig.INIT_DS_NAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.*;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

class AccountAuthenticationRepositoryIntegrationTest extends IntegrationTestsBase {

    private static final String SETUP_SQL = "/account_authentication_repository_it_init.sql";
    private static final String CLEAR_SQL = "/account_authentication_repository_it_clear.sql";

    @Autowired
    private AccountAuthenticationRepository underTest;

    private final Account accountNo1 = createAccount(FIRST_NAME_NO_1, LAST_NAME_NO_1, USERNAME_NO_1, EMAIL_NO_1,
            true, true, new ArrayList<>(List.of(RolesEnum.CLIENT)));

    @Test
    @Transactional(transactionManager = TransactionManagersIds.AUTHENTICATION_MODULE_TX_MANAGER)
    @Sql(scripts = SETUP_SQL, executionPhase = BEFORE_TEST_METHOD,
            config = @SqlConfig(
                    dataSource = INIT_DS_NAME,
                    transactionManager = TransactionManagersIds.INIT_TX_MANAGER,
                    transactionMode = ISOLATED
            ))
    @Sql(scripts = CLEAR_SQL, executionPhase = AFTER_TEST_METHOD,
            config = @SqlConfig(
                    dataSource = INIT_DS_NAME,
                    transactionManager = TransactionManagersIds.INIT_TX_MANAGER,
                    transactionMode = ISOLATED
            ))
    void derivedQueryFindByEmailReturnOptionalContainsAccountWithSpecifiedEmail() {
        Account accountFound = underTest.findByEmail(EMAIL_NO_1).get();

        assertNotNull(accountFound);
        assertEquals(FIRST_NAME_NO_1, accountFound.getFirstName());
        assertEquals(LAST_NAME_NO_1, accountFound.getLastName());
        assertEquals(USERNAME_NO_1, accountFound.getUsername());
    }

    @Test
    @Transactional(transactionManager = TransactionManagersIds.AUTHENTICATION_MODULE_TX_MANAGER)
    @Sql(scripts = SETUP_SQL, executionPhase = BEFORE_TEST_METHOD,
            config = @SqlConfig(
                    dataSource = INIT_DS_NAME,
                    transactionManager = TransactionManagersIds.INIT_TX_MANAGER,
                    transactionMode = ISOLATED
            ))
    @Sql(scripts = CLEAR_SQL, executionPhase = AFTER_TEST_METHOD,
            config = @SqlConfig(
                    dataSource = INIT_DS_NAME,
                    transactionManager = TransactionManagersIds.INIT_TX_MANAGER,
                    transactionMode = ISOLATED
            ))
    void derivedQueryFindByEmailReturnEmptyOptionalWhenAccountWithSpecifiedEmailDoesNotExist() {
        Optional<Account> notExistingAccount = underTest.findByEmail(EMAIL_NO_2);

        assertTrue(notExistingAccount.isEmpty());
    }
}
