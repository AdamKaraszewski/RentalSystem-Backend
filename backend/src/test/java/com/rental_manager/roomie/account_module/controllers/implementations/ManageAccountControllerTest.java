package com.rental_manager.roomie.account_module.controllers.implementations;

import com.rental_manager.roomie.account_module.services.implementations.AccountService;
import com.rental_manager.roomie.exceptions.ExceptionMessages;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.AccountAlreadyBlockedException;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.AccountDoesNotOweAnyRoleException;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.RoleAlreadyOwnedException;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.RoleIsNotOwnedException;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.AccountNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                .andExpect(jsonPath("$.message").value(ExceptionMessages.ACCOUNT_ALREADY_BLOCKED_EXCEPTION));
    }
}