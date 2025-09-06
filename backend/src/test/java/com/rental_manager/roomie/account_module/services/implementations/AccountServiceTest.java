package com.rental_manager.roomie.account_module.services.implementations;

import com.rental_manager.roomie.account_module.dtos.AccountDTO;
import com.rental_manager.roomie.account_module.dtos.AccountOnPageDTO;
import com.rental_manager.roomie.account_module.repositories.AccountRepository;
import com.rental_manager.roomie.account_module.repositories.VerificationTokenRepository;
import com.rental_manager.roomie.entities.Account;
import com.rental_manager.roomie.entities.Role;
import com.rental_manager.roomie.entities.roles.RolesEnum;
import com.rental_manager.roomie.exceptions.ExceptionMessages;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.*;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.AccountNotFoundException;
import com.rental_manager.roomie.utils.searching_with_pagination.PagingResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.rental_manager.roomie.AccountModuleTestUtility.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    void addClientRoleToAccountWithoutClientRoleWorksProperly() {
        var accountWithoutClientRole = createAccount(
                FIRST_NAME_NO_1,
                LAST_NAME_NO_1,
                USERNAME_NO_1,
                EMAIL_NO_1,
                true,
                true,
                new ArrayList<>(List.of(RolesEnum.LANDLORD))
        );
        when(accountRepository.findById(SIMPLE_UUID)).thenReturn(Optional.of(accountWithoutClientRole));
        when(accountRepository.saveAndFlush(accountWithoutClientRole)).thenReturn(accountWithoutClientRole);

        underTest.addRole(SIMPLE_UUID, RolesEnum.CLIENT);

        assertEquals(2, accountWithoutClientRole.getRoles().size());
        Optional<Role> addedRole = accountWithoutClientRole.getRoles().stream()
                .filter(r -> r.getRole() == RolesEnum.CLIENT && r.isActive())
                .findFirst();
        assertTrue(addedRole.isPresent());
    }

    @Test
    void addLandlordRoleToAccountWithoutLandlordRoleWorksProperly() {
        var accountWithoutLandlordRole = createAccount(
                FIRST_NAME_NO_1,
                LAST_NAME_NO_1,
                USERNAME_NO_1,
                EMAIL_NO_1,
                true,
                true,
                new ArrayList<>(List.of(RolesEnum.CLIENT))
        );
        when(accountRepository.findById(SIMPLE_UUID)).thenReturn(Optional.of(accountWithoutLandlordRole));
        when(accountRepository.saveAndFlush(accountWithoutLandlordRole)).thenReturn(accountWithoutLandlordRole);

        underTest.addRole(SIMPLE_UUID, RolesEnum.LANDLORD);

        assertEquals(2, accountWithoutLandlordRole.getRoles().size());
        Optional<Role> addedRole = accountWithoutLandlordRole.getRoles().stream()
                .filter(r -> r.getRole() == RolesEnum.LANDLORD && r.isActive())
                .findFirst();
        assertTrue(addedRole.isPresent());
    }

    @Test
    void addAdminRoleToAccountWithoutAdminRole() {
        var accountWithoutAdminRole = createAccount(
                FIRST_NAME_NO_1,
                LAST_NAME_NO_1,
                USERNAME_NO_1,
                EMAIL_NO_1,
                true,
                true,
                new ArrayList<>(List.of(RolesEnum.CLIENT))
        );
        when(accountRepository.findById(SIMPLE_UUID)).thenReturn(Optional.of(accountWithoutAdminRole));
        when(accountRepository.saveAndFlush(accountWithoutAdminRole)).thenReturn(accountWithoutAdminRole);

        underTest.addRole(SIMPLE_UUID, RolesEnum.ADMIN);

        assertEquals(2, accountWithoutAdminRole.getRoles().size());
        Optional<Role> addedRole = accountWithoutAdminRole.getRoles().stream()
                .filter(r -> r.getRole() == RolesEnum.ADMIN).findFirst();
        assertTrue(addedRole.isPresent());
    }

    @Test
    void activeArchivedRole() {
        var accountWithClientRoleToArchive = createAccount(
                FIRST_NAME_NO_1,
                LAST_NAME_NO_1,
                USERNAME_NO_1,
                EMAIL_NO_1,
                true,
                true,
                new ArrayList<>(List.of(RolesEnum.CLIENT, RolesEnum.ADMIN))
        );
        when(accountRepository.findById(SIMPLE_UUID)).thenReturn(Optional.of(accountWithClientRoleToArchive));
        when(accountRepository.saveAndFlush(accountWithClientRoleToArchive)).thenReturn(accountWithClientRoleToArchive);
        accountWithClientRoleToArchive.getRoles().stream()
                .filter(r -> r.getRole() == RolesEnum.ADMIN)
                .findFirst()
                .ifPresent(role -> role.setActive(false));

        underTest.addRole(SIMPLE_UUID, RolesEnum.ADMIN);

        assertEquals(2, accountWithClientRoleToArchive.getRoles().size());
        Optional<Role> addedRole = accountWithClientRoleToArchive.getRoles().stream()
                .filter(r -> r.getRole() == RolesEnum.ADMIN && r.isActive())
                .findFirst();
        assertTrue(addedRole.isPresent());
    }

    @Test
    void addRoleToAccountThrowsAccountNotFoundException() {
        when(accountRepository.findById(SIMPLE_UUID)).thenReturn(Optional.empty());

        var thrownException = assertThrows(AccountNotFoundException.class, () ->
            underTest.addRole(SIMPLE_UUID, RolesEnum.CLIENT)
        );

        assertEquals(ExceptionMessages.ACCOUNT_NOT_FOUND, thrownException.getMessage());
    }

    @Test
    void addRoleToAccountThrowsAccountRoleAlreadyOwnedException() {
        var accountWithActiveClientRole = createAccount(
                FIRST_NAME_NO_1,
                LAST_NAME_NO_1,
                USERNAME_NO_1,
                EMAIL_NO_1,
                true,
                true,
                new ArrayList<>(List.of(RolesEnum.CLIENT))
        );

        when(accountRepository.findById(SIMPLE_UUID)).thenReturn(Optional.of(accountWithActiveClientRole));

        var thrownException = assertThrows(RoleAlreadyOwnedException.class, () ->
            underTest.addRole(SIMPLE_UUID, RolesEnum.CLIENT)
        );

        assertEquals(ExceptionMessages.ROLE_ALREADY_OWNED, thrownException.getMessage());
    }

    @Test
    void archiveRoleTestWorksProperly() {
        var accountWithClientRoleToBeArchived = createAccount(
                FIRST_NAME_NO_1,
                LAST_NAME_NO_1,
                USERNAME_NO_1,
                EMAIL_NO_1,
                true,
                true,
                new ArrayList<>(List.of(RolesEnum.CLIENT, RolesEnum.LANDLORD))
        );

        when(accountRepository.findById(SIMPLE_UUID)).thenReturn(Optional.of(accountWithClientRoleToBeArchived));
        when(accountRepository.saveAndFlush(accountWithClientRoleToBeArchived)).thenReturn(accountWithClientRoleToBeArchived);

        underTest.archiveRole(SIMPLE_UUID, RolesEnum.CLIENT);

        Optional<Role> archivedRole = accountWithClientRoleToBeArchived.getRoles().stream()
                .filter(r -> !r.isActive() && r.getRole() == RolesEnum.CLIENT)
                .findFirst();
        assertTrue(archivedRole.isPresent());
    }

    @Test
    void archiveRoleThrowsAccountNotFoundException() {
        when(accountRepository.findById(SIMPLE_UUID)).thenReturn(Optional.empty());

        var thrownException = assertThrows(AccountNotFoundException.class, () ->
            underTest.archiveRole(SIMPLE_UUID, RolesEnum.CLIENT)
        );

        assertEquals(ExceptionMessages.ACCOUNT_NOT_FOUND, thrownException.getMessage());
    }

    @Test
    void archiveRoleThrowsRoleIsNotOwnedExceptionWhenAccountHasNotSpecifiedRole() {
        var accountWithClientRoleOnly = createAccount(
                FIRST_NAME_NO_1,
                LAST_NAME_NO_1,
                USERNAME_NO_1,
                EMAIL_NO_1,
                true,
                true,
                List.of(RolesEnum.CLIENT)
        );
        when(accountRepository.findById(SIMPLE_UUID)).thenReturn(Optional.of(accountWithClientRoleOnly));

        var thrownException = assertThrows(RoleIsNotOwnedException.class, () ->
            underTest.archiveRole(SIMPLE_UUID, RolesEnum.ADMIN)
        );

        assertEquals(ExceptionMessages.ROlE_IS_NOT_OWNED, thrownException.getMessage());
    }

    @Test
    void archiveRoleThrowsRoleIsNotOwnedExceptionWhenSpecifiedRoleIsAlreadyArchived() {
        var accountWithAdminRoleArchived = createAccount(
                FIRST_NAME_NO_1,
                LAST_NAME_NO_1,
                USERNAME_NO_1,
                EMAIL_NO_1,
                true,
                true,
                List.of(RolesEnum.CLIENT, RolesEnum.ADMIN)
        );
        when(accountRepository.findById(SIMPLE_UUID)).thenReturn(Optional.of(accountWithAdminRoleArchived));
        underTest.archiveRole(SIMPLE_UUID, RolesEnum.ADMIN);

        var exceptionThrown = assertThrows(RoleIsNotOwnedException.class, () ->
                underTest.archiveRole(SIMPLE_UUID, RolesEnum.ADMIN)
        );

        assertEquals(ExceptionMessages.ROlE_IS_NOT_OWNED, exceptionThrown.getMessage());
    }

    @Test
    void archiveRoleThrowsAccountDoesNotOweAnyRoleException() {
        var accountWithClientRole = createAccount(
                FIRST_NAME_NO_1,
                LAST_NAME_NO_1,
                USERNAME_NO_1,
                EMAIL_NO_1,
                true,
                true,
                List.of(RolesEnum.CLIENT)
        );
        when(accountRepository.findById(SIMPLE_UUID)).thenReturn(Optional.of(accountWithClientRole));

        var thrownException = assertThrows(AccountDoesNotOweAnyRoleException.class, () ->
            underTest.archiveRole(SIMPLE_UUID, RolesEnum.CLIENT)
        );

        assertEquals(ExceptionMessages.ACCOUNT_DOES_NOT_OWE_ANY_ROLE, thrownException.getMessage());
    }

    @Test
    void blockAccountWorksProperlyTest() {
        var accountToBeBlocked = createAccount(
                FIRST_NAME_NO_1,
                LAST_NAME_NO_1,
                USERNAME_NO_1,
                EMAIL_NO_1,
                true,
                true,List.of()
        );
        when(accountRepository.findById(SIMPLE_UUID)).thenReturn(Optional.of(accountToBeBlocked));
        when(accountRepository.saveAndFlush(accountToBeBlocked)).thenReturn(accountToBeBlocked);

        underTest.blockAccount(SIMPLE_UUID);

        assertFalse(accountToBeBlocked.isActive());
    }

    @Test
    void blockAccountThrowsAccountNotFoundExceptionWhenAccountDoesNotExistTest() {
        when(accountRepository.findById(SIMPLE_UUID)).thenReturn(Optional.empty());

        var thrownException = assertThrows(AccountNotFoundException.class, () ->
            underTest.blockAccount(SIMPLE_UUID)
        );

        assertEquals(ExceptionMessages.ACCOUNT_NOT_FOUND, thrownException.getMessage());
    }

    @Test
    void blockAccountThrowsAccountIsAlreadyBlockedException() {
        var blockedAccount = createAccount(
                FIRST_NAME_NO_1,
                LAST_NAME_NO_1,
                USERNAME_NO_1,
                EMAIL_NO_1,
                true,
                false,
                List.of(RolesEnum.CLIENT)
        );
        when(accountRepository.findById(SIMPLE_UUID)).thenReturn(Optional.of(blockedAccount));

        var exceptionThrown = assertThrows(AccountAlreadyBlockedException.class, () ->
                underTest.blockAccount(SIMPLE_UUID)
        );

        assertEquals(ExceptionMessages.ACCOUNT_ALREADY_BLOCKED, exceptionThrown.getMessage());
    }

    @Test
    void activateAccountWorksProperlyTest() {
        var accountToBeActivated = createAccount(
                FIRST_NAME_NO_1,
                LAST_NAME_NO_1,
                USERNAME_NO_1,
                EMAIL_NO_1,
                true,
                false,
                List.of(RolesEnum.CLIENT)
        );
        when(accountRepository.findById(SIMPLE_UUID)).thenReturn(Optional.of(accountToBeActivated));
        when(accountRepository.saveAndFlush(accountToBeActivated)).thenReturn(accountToBeActivated);

        underTest.activateAccount(SIMPLE_UUID);

        assertTrue(accountToBeActivated.isActive());
    }

    @Test
    void activateAccountThrowsAccountNotFoundExceptionWhenAccountDoesNotExistTest() {
        when(accountRepository.findById(SIMPLE_UUID)).thenReturn(Optional.empty());

        var exceptionThrown = assertThrows(AccountNotFoundException.class, () -> underTest.activateAccount(SIMPLE_UUID));

        assertEquals(ExceptionMessages.ACCOUNT_NOT_FOUND, exceptionThrown.getMessage());
    }

    @Test
    void activateAccountThrowsAccountAlreadyActiveWhenAccountIsAlreadyActive() {
        var activeAccount = createAccount(
                FIRST_NAME_NO_1,
                LAST_NAME_NO_1,
                USERNAME_NO_1,
                EMAIL_NO_1,
                true,
                true,
                List.of(RolesEnum.CLIENT)
                );
        when(accountRepository.findById(SIMPLE_UUID)).thenReturn(Optional.of(activeAccount));

        var exceptionThrown = assertThrows(AccountAlreadyActiveException.class, () -> underTest.activateAccount(SIMPLE_UUID));

        assertEquals(ExceptionMessages.ACCOUNT_ALREADY_ACTIVE, exceptionThrown.getMessage());
    }

    @Test
    void getAllAccountsMatchingPhrasesWithPaginationReturnNotEmptyPage() {
        List<Account> accountsOnPage = new ArrayList<>();
        var accountClient1 = createAccount(
                FIRST_NAME_NO_1,
                LAST_NAME_NO_1,
                USERNAME_NO_1,
                EMAIL_NO_1,
                true,
                true,
                List.of(RolesEnum.CLIENT)
        );
        var accountClient2 = createAccount(
                FIRST_NAME_NO_2,
                LAST_NAME_NO_2,
                USERNAME_NO_2,
                EMAIL_NO_2,
                true,
                true,
                List.of(RolesEnum.CLIENT)
        );
        accountsOnPage.add(accountClient1);
        accountsOnPage.add(accountClient2);

        Pageable pageable = PageRequest.of(0, 2, Sort.Direction.ASC, "username");
        Page<Account> accounts = new PageImpl<>(accountsOnPage, pageable, 2);
        when(accountRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(accounts);
        List<String> phrases = new ArrayList<>();
        phrases.add(LAST_NAME_NO_1);
        phrases.add(LAST_NAME_NO_2);

        PagingResult<AccountOnPageDTO> result = underTest.getAllAccountsMatchingPhrasesWithPagination(0,
                2, Sort.Direction.ASC, USERNAME_FIELD, phrases);

        assertNotNull(result);
        assertNotNull(result.getContent());
        assertEquals(2, result.getContent().size());
        assertEquals(USERNAME_NO_1, result.getContent().getFirst().getUsername());
        assertEquals(FIRST_NAME_NO_1, result.getContent().getFirst().getFirstName());
        assertEquals(LAST_NAME_NO_1, result.getContent().getFirst().getLastName());

        assertEquals(USERNAME_NO_2, result.getContent().getLast().getUsername());
        assertEquals(FIRST_NAME_NO_2, result.getContent().getLast().getFirstName());
        assertEquals(LAST_NAME_NO_2, result.getContent().getLast().getLastName());
    }

    @Test
    void getAllAccountsMatchingPhrasesWithPaginationReturnEmptyPage() {
        List<Account> accountsOnPage = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 2, Sort.Direction.ASC, USERNAME_FIELD);
        Page<Account> accounts = new PageImpl<>(accountsOnPage, pageable, 0);
        when(accountRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(accounts);
        List<String> phrases = new ArrayList<>();

        PagingResult<AccountOnPageDTO> result = underTest.getAllAccountsMatchingPhrasesWithPagination(0,
                2, Sort.Direction.ASC, USERNAME_FIELD, phrases);

        assertNotNull(result);
        assertNotNull(result.getContent());
        assertEquals(0, result.getContent().size());
    }

    @Test
    void getAccountByIdReturnAccountDTO() {
        Account account = createAccount(
                FIRST_NAME_NO_1,
                LAST_NAME_NO_1,
                USERNAME_NO_1,
                EMAIL_NO_1,
                true,
                true,
                List.of(RolesEnum.CLIENT)
        );
        when(accountRepository.findById(SIMPLE_UUID)).thenReturn(Optional.of(account));

        AccountDTO accountDTO = underTest.getAccountById(SIMPLE_UUID);

        assertNotNull(accountDTO);
        assertEquals(FIRST_NAME_NO_1, accountDTO.getFirstName());
        assertEquals(LAST_NAME_NO_1, accountDTO.getLastName());
        assertEquals(USERNAME_NO_1, accountDTO.getUsername());
        assertEquals(EMAIL_NO_1, accountDTO.getEmail());
        assertTrue(accountDTO.isVerified());
        assertTrue(accountDTO.isActive());
        assertEquals(1, accountDTO.getRoles().size());
        assertEquals(RolesEnum.CLIENT.name(), accountDTO.getRoles().getFirst());
    }

    @Test
    void getAccountByIdThrowsAccountNotFoundException() {
        when(accountRepository.findById(SIMPLE_UUID)).thenReturn(Optional.empty());

        var exceptionThrown = assertThrows(AccountNotFoundException.class, () -> underTest.getAccountById(SIMPLE_UUID));

        assertEquals(ExceptionMessages.ACCOUNT_NOT_FOUND, exceptionThrown.getMessage());
    }

    @Test
    void changeMyPasswordSuccessfully() {
        var accountToBeModified = createAccount(
                FIRST_NAME_NO_1,
                LAST_NAME_NO_1,
                USERNAME_NO_1,
                EMAIL_NO_1,
                true,
                true,
                List.of(RolesEnum.CLIENT)
        );
        when(accountRepository.findById(SIMPLE_UUID)).thenReturn(Optional.of(accountToBeModified));

        underTest.changeMyPassword(SIMPLE_UUID, NEW_PASSWORD);

        assertEquals(NEW_PASSWORD, accountToBeModified.getPassword());
    }

    @Test
    void changeMyPasswordThrowsAccountNotFoundException() {
        when(accountRepository.findById(SIMPLE_UUID)).thenThrow(new AccountNotFoundException());

        var exceptionThrown = assertThrows(AccountNotFoundException.class, () ->
                underTest.changeMyPassword(SIMPLE_UUID, NEW_PASSWORD));

        assertEquals(ExceptionMessages.ACCOUNT_NOT_FOUND, exceptionThrown.getMessage());
    }

    @Test
    void editMyAccountChangeFirstNameAndLastNameSuccessfully() {
        var accountToBeModified = createAccount(
                FIRST_NAME_NO_1,
                LAST_NAME_NO_1,
                USERNAME_NO_1,
                EMAIL_NO_1,
                true,
                true,
                List.of(RolesEnum.CLIENT)
        );
        when(accountRepository.findById(SIMPLE_UUID)).thenReturn(Optional.of(accountToBeModified));
        when(accountRepository.saveAndFlush(accountToBeModified)).thenReturn(accountToBeModified);

        var modifiedAccountDto = underTest.editMyAccount(
                SIMPLE_UUID,
                FIRST_NAME_NO_5,
                LAST_NAME_NO_5
        );

        assertNotNull(modifiedAccountDto);
        assertEquals(FIRST_NAME_NO_5, accountToBeModified.getFirstName());
        assertEquals(LAST_NAME_NO_5, accountToBeModified.getLastName());
    }

    @Test
    void editMyAccountThrowsAccountNotFoundException() {
        when(accountRepository.findById(SIMPLE_UUID)).thenReturn(Optional.empty());

        var exceptionThrown = assertThrows(AccountNotFoundException.class, () ->
            underTest.editMyAccount(
                    SIMPLE_UUID,
                    FIRST_NAME_NO_5,
                    LAST_NAME_NO_5)
        );

        assertEquals(ExceptionMessages.ACCOUNT_NOT_FOUND, exceptionThrown.getMessage());
    }
}