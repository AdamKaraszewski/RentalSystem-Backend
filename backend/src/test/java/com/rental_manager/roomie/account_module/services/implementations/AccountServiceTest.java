package com.rental_manager.roomie.account_module.services.implementations;

import com.rental_manager.roomie.AccountModuleTestUtility;
import com.rental_manager.roomie.account_module.repositories.AccountRepository;
import com.rental_manager.roomie.account_module.repositories.VerificationTokenRepository;
import com.rental_manager.roomie.entities.Role;
import com.rental_manager.roomie.entities.roles.Admin;
import com.rental_manager.roomie.entities.roles.Landlord;
import com.rental_manager.roomie.entities.roles.RolesEnum;
import com.rental_manager.roomie.exceptions.ExceptionMessages;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.AccountAlreadyActiveException;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.AccountDoesNotOweAnyRoleException;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.RoleAlreadyOwnedException;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.RoleIsNotOwnedException;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.AccountNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.rental_manager.roomie.AccountModuleTestUtility.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    private AccountService underTest;

    @BeforeEach
    void setUp() {
        underTest = new AccountService(VERIFICATION_TOKEN_LIFE_TIME, accountRepository, verificationTokenRepository);
    }

    @Test
    void addClientRoleToAccountWithoutClientRole() {
        var account = createNotVerifiedAccountWithAdminRole();

        when(accountRepository.findById(eq(ID))).thenReturn(Optional.of(account));
        when(accountRepository.saveAndFlush(account)).thenReturn(account);

        underTest.addRole(ID, RolesEnum.CLIENT);

        assertEquals(2, account.getRoles().size());
        Optional<Role> addedRole = account.getRoles().stream()
                .filter(r -> r.getRole() == RolesEnum.CLIENT)
                .findFirst();
        assertTrue(addedRole.isPresent());
    }

    @Test
    void addLandlordRoleToAccountWithoutLandlordRole() {
        var account = AccountModuleTestUtility.createNotVerifiedAccountWithClientRole();

        when(accountRepository.findById(eq(ID))).thenReturn(Optional.of(account));
        when(accountRepository.saveAndFlush(account)).thenReturn(account);

        underTest.addRole(ID, RolesEnum.LANDLORD);

        assertEquals(2, account.getRoles().size());
        Optional<Role> addedRole = account.getRoles().stream()
                .filter(r -> r.getRole() == RolesEnum.LANDLORD).findFirst();
        assertTrue(addedRole.isPresent());
    }

    @Test
    void addAdminRoleToAccountWithoutAdminRole() {
        var account = AccountModuleTestUtility.createNotVerifiedAccountWithClientRole();

        when(accountRepository.findById(eq(ID))).thenReturn(Optional.of(account));
        when(accountRepository.saveAndFlush(account)).thenReturn(account);

        underTest.addRole(ID, RolesEnum.ADMIN);

        assertEquals(2, account.getRoles().size());
        Optional<Role> addedRole = account.getRoles().stream().filter(r -> r.getRole() == RolesEnum.ADMIN).findFirst();
        assertTrue(addedRole.isPresent());
    }

    @Test
    void activeArchivedRole() {
        var account = AccountModuleTestUtility.createNotVerifiedAccountWithClientRole();
        var adminRole = new Admin(account);
        adminRole.setActive(false);
        account.addRole(adminRole);

        when(accountRepository.findById(eq(ID))).thenReturn(Optional.of(account));
        when(accountRepository.saveAndFlush(account)).thenReturn(account);


        underTest.addRole(ID, RolesEnum.ADMIN);

        assertEquals(2, account.getRoles().size());
        Optional<Role> addedRole = account.getRoles().stream()
                .filter(r -> r.getRole() == RolesEnum.ADMIN && !r.isActive())
                .findFirst();
        assertTrue(addedRole.isPresent());
    }

    @Test
    void addRoleToAccountThrowsAccountNotFoundException() {
        when(accountRepository.findById(eq(ID))).thenReturn(Optional.empty());

        var thrownException = assertThrows(AccountNotFoundException.class, () -> {
            underTest.addRole(ID, RolesEnum.CLIENT);
        });

        assertEquals(ExceptionMessages.ACCOUNT_NOT_FOUND, thrownException.getMessage());
    }

    @Test
    void addRoleToAccountThrowsAccountRoleAlreadyOwnedException() {
        var account = AccountModuleTestUtility.createNotVerifiedAccountWithClientRole();

        when(accountRepository.findById(eq(ID))).thenReturn(Optional.of(account));

        var thrownException = assertThrows(RoleAlreadyOwnedException.class, () -> {
            underTest.addRole(ID, RolesEnum.CLIENT);
        });

        assertEquals(ExceptionMessages.ROLE_ALREADY_OWNED, thrownException.getMessage());
    }

    @Test
    void archiveRoleTest() {
        var account = AccountModuleTestUtility.createNotVerifiedAccountWithClientRole();
        var landlordRole = new Landlord(account);
        account.addRole(landlordRole);

        when(accountRepository.findById(eq(ID))).thenReturn(Optional.of(account));
        when(accountRepository.saveAndFlush(account)).thenReturn(account);

        underTest.archiveRole(ID, RolesEnum.CLIENT);

        Optional<Role> archivedRole = account.getRoles().stream()
                .filter(r -> !r.isActive() && r.getRole() == RolesEnum.CLIENT)
                .findFirst();
        assertTrue(archivedRole.isPresent());
    }

    @Test
    void archiveRoleThrowsAccountNotFoundException() {
        when(accountRepository.findById(eq(ID))).thenReturn(Optional.empty());

        var thrownException = assertThrows(AccountNotFoundException.class, () -> {
            underTest.archiveRole(ID, RolesEnum.CLIENT);
        });

        assertEquals(ExceptionMessages.ACCOUNT_NOT_FOUND, thrownException.getMessage());
    }

    @Test
    void archiveRoleThrowsRoleIsNotOwnedException() {
        var account = AccountModuleTestUtility.createNotVerifiedAccount();
        when(accountRepository.findById(eq(ID))).thenReturn(Optional.of(account));

        var thrownException = assertThrows(RoleIsNotOwnedException.class, () -> {
            underTest.archiveRole(ID, RolesEnum.ADMIN);
        });

        assertEquals(ExceptionMessages.ROlE_IS_NOT_OWNED, thrownException.getMessage());
    }

    @Test
    void archiveRoleThrowsAccountDoesNotOweAnyRoleException() {
        var account = AccountModuleTestUtility.createNotVerifiedAccountWithClientRole();
        when(accountRepository.findById(eq(ID))).thenReturn(Optional.of(account));

        var thrownException = assertThrows(AccountDoesNotOweAnyRoleException.class, () -> {
            underTest.archiveRole(ID, RolesEnum.CLIENT);
        });

        assertEquals(ExceptionMessages.ACCOUNT_DOES_NOT_OWE_ANY_ROLE, thrownException.getMessage());
    }

    @Test
    void blockAccountWorksProperlyTest() {
        var account = AccountModuleTestUtility.createNotVerifiedAccountWithClientRole();
        when(accountRepository.findById(eq(ID))).thenReturn(Optional.of(account));
        when(accountRepository.saveAndFlush(account)).thenReturn(account);

        underTest.blockAccount(ID);

        assertFalse(account.isActive());
    }

    @Test
    void blockAccountThrowsAccountNotFoundExceptionWhenAccountDoesNotExistTest() {
        when(accountRepository.findById(ID)).thenReturn(Optional.empty());

        var thrownException = assertThrows(AccountNotFoundException.class, () -> {
            underTest.blockAccount(ID);
        });

        assertEquals(ExceptionMessages.ACCOUNT_NOT_FOUND, thrownException.getMessage());
    }

    @Test
    void activateAccountWorksProperlyTest() {
        var account = createNotVerifiedAccountWithClientRole();
        account.setActive(false);
        when(accountRepository.findById(ID)).thenReturn(Optional.of(account));
        when(accountRepository.saveAndFlush(account)).thenReturn(account);

        underTest.activateAccount(ID);

        assertTrue(account.isActive());
    }

    @Test
    void activateAccountThrowsAccountNotFoundExceptionWhenAccountDoesNotExistTest() {
        when(accountRepository.findById(ID)).thenReturn(Optional.empty());

        var exceptionThrown = assertThrows(AccountNotFoundException.class, () -> underTest.activateAccount(ID));

        assertEquals(ExceptionMessages.ACCOUNT_NOT_FOUND, exceptionThrown.getMessage());
    }

    @Test
    void activateAccountThrowsAccountAlreadyActiveWhenAccountIsAlreadyActive() {
        var account = createNotVerifiedAccountWithClientRole();
        when(accountRepository.findById(ID)).thenReturn(Optional.of(account));

        var exceptionThrown = assertThrows(AccountAlreadyActiveException.class, () -> underTest.activateAccount(ID));

        assertEquals(ExceptionMessages.ACCOUNT_ALREADY_ACTIVE, exceptionThrown.getMessage());
    }
}