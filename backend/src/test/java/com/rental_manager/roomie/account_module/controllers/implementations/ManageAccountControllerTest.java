package com.rental_manager.roomie.account_module.controllers.implementations;

import com.rental_manager.roomie.account_module.dtos.AccountOnPageDTO;
import com.rental_manager.roomie.account_module.services.implementations.AccountService;
import com.rental_manager.roomie.entities.Account;
import com.rental_manager.roomie.exceptions.ExceptionMessages;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.*;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.AccountNotFoundException;
import com.rental_manager.roomie.utils.PagingResult;
import com.rental_manager.roomie.utils.account_module_utils.AccountConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
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
    void getAllAccountsWithPaginationReturnOkStatusCodeAndPageWithContentSize3WhenAccountsAreSuccessfullyLoaded()
            throws Exception {
        var account1 = createNotVerifiedAccount("b_username_no_1", "email.no1@example.com",
                "first_name_no_1","last_name_no_1");
        var account2 = createNotVerifiedAccount("a_username_no_2", "email.no2@example.com",
                "first_name_no_2", "last_name_no_2");
        var account3 = createNotVerifiedAccount("x_username_no_3", "email.np3@example.com",
                "first_name_no_3", "last_name_no_3");

        List<Account> pageContent1 = new ArrayList<>();
        pageContent1.add(account2);
        pageContent1.add(account1);
        pageContent1.add(account3);

        Pageable pageable = PageRequest.of(0, 3);

        Page<Account> page1 = new PageImpl<>(pageContent1, pageable, 3);

        List<AccountOnPageDTO> accountOnPageDTOs1 = page1.stream()
                .map(AccountConverter::convertAccountToAccountOnPageDto).toList();

        PagingResult<AccountOnPageDTO> resultPage1 = new PagingResult<>(
                accountOnPageDTOs1,
                page1.getTotalPages(),
                page1.getTotalElements(),
                page1.getSize(),
                page1.getNumber(),
                page1.isEmpty()
        );

        when(accountService.getAllAccountsWithPagination(0, 3)).thenReturn(resultPage1);

        mockMvc.perform(
                get(BASE_ENDPOINT)
                        .param("pageNumber", "0")
                        .param("pageSize", "3")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username").value("a_username_no_2"))
                .andExpect(jsonPath("$.content[0].firstName").value("first_name_no_2"))
                .andExpect(jsonPath("$.content[0].lastName").value("last_name_no_2"))
                .andExpect(jsonPath("$.content[2].username").value("x_username_no_3"))
                .andExpect(jsonPath("$.content[2].firstName").value("first_name_no_3"))
                .andExpect(jsonPath("$.content[2].lastName").value("last_name_no_3"))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.size").value(3))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.empty").value(false));
    }

    @Test
    void getAllAccountsWithPaginationReturnOkStatusAndPageWithEmptyContentPageSizeIsGraterAllAccountsNumberAndPageNumberIsGraterThan0()
        throws Exception {
        List<Account> pageContent1 = new ArrayList<>();

        Pageable pageable = PageRequest.of(10, 10);

        Page<Account> page1 = new PageImpl<>(pageContent1, pageable, 3);

        List<AccountOnPageDTO> accountOnPageDTOs1 = page1.stream()
                .map(AccountConverter::convertAccountToAccountOnPageDto).toList();

        PagingResult<AccountOnPageDTO> resultPage1 = new PagingResult<>(
                accountOnPageDTOs1,
                page1.getTotalPages(),
                page1.getTotalElements(),
                page1.getSize(),
                page1.getNumber(),
                page1.isEmpty()
        );

        when(accountService.getAllAccountsWithPagination(10, 10)).thenReturn(resultPage1);

        mockMvc.perform(
                get(BASE_ENDPOINT)
                        .param("pageNumber", "10")
                        .param("pageSize", "10")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.page").value(10))
                .andExpect(jsonPath("$.empty").value(true));
    }
}