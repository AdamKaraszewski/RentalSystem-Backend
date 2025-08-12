package com.rental_manager.roomie.account_module.controllers.implementations;

import com.rental_manager.roomie.account_module.dtos.AccountOnPageDTO;
import com.rental_manager.roomie.account_module.services.implementations.AccountService;
import com.rental_manager.roomie.entities.Account;
import com.rental_manager.roomie.exceptions.ExceptionMessages;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.*;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.AccountNotFoundException;
import com.rental_manager.roomie.utils.searching_with_pagination.PagingResult;
import com.rental_manager.roomie.utils.account_module_utils.AccountConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.rental_manager.roomie.AccountModuleTestUtility.createNotVerifiedAccount;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ManageAccountController.class)
@ActiveProfiles("test")
class ManageAccountControllerTest {

    private static final String BASE_ENDPOINT = "/accounts";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    @Test
    void addRoleReturnOkStatusCodeTest() throws Exception {
        doNothing().when(accountService).addRole(any(), any());

        mockMvc.perform(
                post(BASE_ENDPOINT + "/" + UUID.randomUUID() + "/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "role": "CLIENT"
                                }
                                """)
        )
                .andExpect(status().isOk());
    }

    @Test
    void addRoleReturnNotFoundStatusAndAccountNotFoundDtoWhenAccountIsNotFoundTest() throws Exception {
        doThrow(new AccountNotFoundException()).when(accountService).addRole(any(), any());

        mockMvc.perform(
                post(BASE_ENDPOINT + "/" + UUID.randomUUID() + "/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "role": "CLIENT"
                                }
                                """)
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.message").value(ExceptionMessages.ACCOUNT_NOT_FOUND));;
    }

    @Test
    void addRoleReturnConflictStatusAndBusinessLogicExceptionDtoWhenUserAlreadyOwnsRoleTest() throws Exception {
        doThrow(new RoleAlreadyOwnedException()).when(accountService).addRole(any(), any());

        mockMvc.perform(
                post(BASE_ENDPOINT + "/" + UUID.randomUUID() + "/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "role": "CLIENT"
                                }
                                """)
        )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value(409))
                .andExpect(jsonPath("$.message").value(ExceptionMessages.ROLE_ALREADY_OWNED));
    }

    @Test
    void archiveRoleReturnOkStatusCodeTest() throws Exception {
        doNothing().when(accountService).archiveRole(any(), any());

        mockMvc.perform(
                delete(BASE_ENDPOINT + "/" + UUID.randomUUID() + "/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "role": "LANDLORD"
                                }
                                """)
        )
                .andExpect(status().isOk());
    }

    @Test
    void archiveRoleReturnNotFoundStatusCodeAndResourceNotExceptionDtoWhenSpecifiedAccountDoesNotExistTest() throws Exception {
        doThrow(new AccountNotFoundException()).when(accountService).archiveRole(any(), any());

        mockMvc.perform(
                delete(BASE_ENDPOINT + "/" + UUID.randomUUID() + "/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "role": "LANDLORD"
                                }
                                """)
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.message").value(ExceptionMessages.ACCOUNT_NOT_FOUND));
    }

    @Test
    void archiveRoleReturnConflictStatusCodeAndBusinessLogicExceptionDtoWhenAccountDoesNotOweSpecifiedRoleTest() throws Exception {
        doThrow(new RoleIsNotOwnedException()).when(accountService).archiveRole(any(), any());

        mockMvc.perform(
                delete(BASE_ENDPOINT + "/" + UUID.randomUUID() + "/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "role": "CLIENT"
                                }
                                """)
        )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value(409))
                .andExpect(jsonPath("$.message").value(ExceptionMessages.ROlE_IS_NOT_OWNED));
    }

    @Test
    void archiveRoleReturnConflictStatusCodeAndBusinessLogicExceptionDtoWhenArchiveRoleArchivesTheLastActiveRoleTest() throws Exception {
        doThrow(new AccountDoesNotOweAnyRoleException()).when(accountService).archiveRole(any(), any());

        mockMvc.perform(
                delete(BASE_ENDPOINT + "/" + UUID.randomUUID() + "/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "role": "ADMIN"
                                }
                                """)
        )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value(409))
                .andExpect(jsonPath("$.message").value(ExceptionMessages.ACCOUNT_DOES_NOT_OWE_ANY_ROLE));

    }

    @Test
    void blockAccountReturnOkStatusCodeWhenAccountIsBlockedSuccessfullyTest() throws Exception {
        doNothing().when(accountService).blockAccount(any());

        mockMvc.perform(
                post(BASE_ENDPOINT + "/" + UUID.randomUUID() + "/block")
        )
                .andExpect(status().isOk());
    }

    @Test
    void blockAccountReturnNotFoundStatusCodeAndResourceNotFountDtoWhenAccountDoesNotExistTest() throws Exception {
        doThrow(new AccountNotFoundException()).when(accountService).blockAccount(any());

        mockMvc.perform(
                post(BASE_ENDPOINT + "/" + UUID.randomUUID() + "/block")
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.message").value(ExceptionMessages.ACCOUNT_NOT_FOUND));
    }

    @Test
    void blockAccountReturnConflictStatusAndBusinessLogicExceptionDtoWhenAccountIsAlreadyBlocked() throws Exception {
        doThrow(new AccountAlreadyBlockedException()).when(accountService).blockAccount(any());

        mockMvc.perform(
                post(BASE_ENDPOINT + "/" + UUID.randomUUID() + "/block")
        )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value(409))
                .andExpect(jsonPath("$.message").value(ExceptionMessages.ACCOUNT_ALREADY_BLOCKED));
    }

    @Test
    void activateAccountReturnOkStatusCodeWhenAccountIsSuccessfullyActivated() throws Exception {
        doNothing().when(accountService).activateAccount(any());

        mockMvc.perform(
                post(BASE_ENDPOINT + "/" + UUID.randomUUID() + "/activate")
        )
                .andExpect(status().isOk());
    }

    @Test
    void activateAccountReturnNotFoundStatusCodeAndResourceNotFoundDtoWhenSpecifiedAccountDoesNotExist() throws Exception {
        doThrow(new AccountNotFoundException()).when(accountService).activateAccount(any());

        mockMvc.perform(
                post(BASE_ENDPOINT + "/" + UUID.randomUUID() + "/activate")
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.message").value(ExceptionMessages.ACCOUNT_NOT_FOUND));
    }

    @Test
    void activateAccountReturnConflictStatusCodeAndBusinessLogicExceptionDtoWhenSpecifiedAccountIsAlreadyActive()
            throws Exception {
        doThrow(new AccountAlreadyActiveException()).when(accountService).activateAccount(any());

        mockMvc.perform(
                post(BASE_ENDPOINT + "/" + UUID.randomUUID() + "/activate")
        )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value(409))
                .andExpect(jsonPath("$.message").value(ExceptionMessages.ACCOUNT_ALREADY_ACTIVE));
    }

    @Test
    void getAllAccountsMatchingPhraseWithPaginationReturnsNotEmptyPage() throws Exception {
        List<Account> accounts = new ArrayList<>();
        var account1 = createNotVerifiedAccount("a_username_no_1", "email.no1@example.com",
                "first_name_no_1", "last_name_no_1");
        var account2 = createNotVerifiedAccount("b_username_no_2", "email.no2@example.com",
                "first_name_no_2", "last_name_no_2");
        accounts.add(account1);
        accounts.add(account2);

        Pageable pageable = PageRequest.of(0, 2, Sort.Direction.ASC, "username");
        Page<Account> accountsPage = new PageImpl<>(accounts, pageable, 2);
        List<AccountOnPageDTO> pageDTOContent = accounts.stream()
                .map(AccountConverter::convertAccountToAccountOnPageDto).toList();
        PagingResult<AccountOnPageDTO> accountPageDTO = new PagingResult<>(
                pageDTOContent,
                accountsPage.getTotalPages(),
                accountsPage.getTotalElements(),
                accountsPage.getSize(),
                accountsPage.getNumber(),
                accountsPage.isEmpty()
        );

        when(accountService.getAllAccountsMatchingPhrasesWithPagination(eq(0), eq(2), eq(Sort.Direction.ASC),
                eq("username"), anyList())).thenReturn(accountPageDTO);

        mockMvc.perform(
                get(BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "pageNumber": 0,
                                "pageSize": 2,
                                "direction": "ASC",
                                "sortField": "username",
                                "phrases": [
                                "user"
                                ]
                                }
                                """)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].username").value("a_username_no_1"))
                .andExpect(jsonPath("$.content[0].firstName").value("first_name_no_1"))
                .andExpect(jsonPath("$.content[0].lastName").value("last_name_no_1"))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.empty").value(false));
    }

    @Test
    void getAllAccountsMatchingPhraseWithPaginationReturnsEmptyPage() throws Exception {
        List<Account> accounts = new ArrayList<>();

        Pageable pageable = PageRequest.of(0, 2, Sort.Direction.ASC, "username");
        Page<Account> accountsPage = new PageImpl<>(accounts, pageable, 0);
        List<AccountOnPageDTO> pageDTOContent = accounts.stream()
                .map(AccountConverter::convertAccountToAccountOnPageDto).toList();
        PagingResult<AccountOnPageDTO> accountPageDTO = new PagingResult<>(
                pageDTOContent,
                accountsPage.getTotalPages(),
                accountsPage.getTotalElements(),
                accountsPage.getSize(),
                accountsPage.getNumber(),
                accountsPage.isEmpty()
        );

        when(accountService.getAllAccountsMatchingPhrasesWithPagination(eq(0), eq(2), eq(Sort.Direction.ASC),
                eq("username"), anyList())).thenReturn(accountPageDTO);

        mockMvc.perform(
                        get(BASE_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                "pageNumber": 0,
                                "pageSize": 2,
                                "direction": "ASC",
                                "sortField": "username",
                                "phrases": [
                                "not_existing_username"
                                ]
                                }
                                """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalPages").value(0))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.empty").value(true));
    }
}