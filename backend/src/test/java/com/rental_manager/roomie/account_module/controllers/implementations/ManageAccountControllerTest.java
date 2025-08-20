package com.rental_manager.roomie.account_module.controllers.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rental_manager.roomie.account_module.dtos.AccountDTO;
import com.rental_manager.roomie.account_module.dtos.AccountOnPageDTO;
import com.rental_manager.roomie.account_module.services.implementations.AccountService;
import com.rental_manager.roomie.entities.roles.RolesEnum;
import com.rental_manager.roomie.exceptions.ExceptionMessages;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.*;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.AccountNotFoundException;
import com.rental_manager.roomie.utils.searching_with_pagination.PagingResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static com.rental_manager.roomie.AccountModuleTestUtility.*;
import static com.rental_manager.roomie.utils.account_module_utils.PaginationTestUtility.*;
import static org.hamcrest.Matchers.hasSize;
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

    private static final String CHANGE_ROLE_ENDPOINT = "/accounts/" + UUID.randomUUID() + "/roles";
    private static final String BLOCK_ACCOUNT_ENDPOINT = "/accounts/" + UUID.randomUUID() + "/block";
    private static final String ACTIVATE_ACCOUNT_ENDPOINT = "/accounts/" + UUID.randomUUID() + "/activate";
    private static final String GET_ACCOUNTS_ENDPOINT = "/accounts";
    private static final String GET_ACCOUNT_BY_ID_ENDPOINT = "/accounts/" + UUID.randomUUID();

    private static final String CLIENT_ROLE = "CLIENT";
    private static final String LANDLORD_ROLE = "LANDLORD";
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String INVALID_ROLE = "NOT_EXISTING_ROLE";

    private static final AccountOnPageDTO ACCOUNT_ON_PAGE_DTO_NO_1 = new AccountOnPageDTO(
            USERNAME_NO_1,
            FIRST_NAME_NO_1,
            LAST_NAME_NO_1
    );

    private static final AccountOnPageDTO ACCOUNT_ON_PAGE_DTO_NO_2 = new AccountOnPageDTO(
            USERNAME_NO_2,
            FIRST_NAME_NO_2,
            LAST_NAME_NO_2
    );

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    @Test
    void addRoleReturnOkStatusCodeTest() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(ROLE_FIELD, CLIENT_ROLE);
        String requestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        doNothing().when(accountService).addRole(any(), any());

        mockMvc.perform(
                post(CHANGE_ROLE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isOk());
    }

    @Test
    void addRoleReturnNotFoundStatusAndAccountNotFoundDtoWhenAccountIsNotFoundTest() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(ROLE_FIELD, CLIENT_ROLE);
        String requestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        doThrow(new AccountNotFoundException()).when(accountService).addRole(any(), any());

        mockMvc.perform(
                post(CHANGE_ROLE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.message").value(ExceptionMessages.ACCOUNT_NOT_FOUND));
    }

    @Test
    void addRoleReturnUnprocessableEntityStatusCodeAndValidationErrorsDtoWhenSpecifiedRoleIsNull() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(ROLE_FIELD, null);
        String requestBody = mapper.writeValueAsString(data);
        doNothing().when(accountService).addRole(any(), any());

        mockMvc.perform(
                post(CHANGE_ROLE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorCode").value(422))
                .andExpect(jsonPath("$.validationErrors[0].fieldName").value(ROLE_FIELD))
                .andExpect(jsonPath("$.validationErrors[0].message").value(ExceptionMessages.FIELD_NULL_VALUE));
    }

    @Test
    void addRoleReturnUnprocessableEntityStatusCodeAndValidationErrorsDtoWhenSpecifiedRoleDoesNotExist() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(ROLE_FIELD, INVALID_ROLE);
        String requestBody = mapper.writeValueAsString(data);
        doNothing().when(accountService).addRole(any(), any());

        mockMvc.perform(
                post(CHANGE_ROLE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorCode").value(422))
                .andExpect(jsonPath("$.validationErrors[0].fieldName").value(ROLE_FIELD))
                .andExpect(jsonPath("$.validationErrors[0].message").value(ExceptionMessages.ROLE_DOES_NOT_EXIST));
    }

    @Test
    void addRoleReturnConflictStatusAndBusinessLogicExceptionDtoWhenUserAlreadyOwnsRoleTest() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(ROLE_FIELD, CLIENT_ROLE);
        String requestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        doThrow(new RoleAlreadyOwnedException()).when(accountService).addRole(any(), any());

        mockMvc.perform(
                post(CHANGE_ROLE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value(409))
                .andExpect(jsonPath("$.message").value(ExceptionMessages.ROLE_ALREADY_OWNED));
    }

    @Test
    void archiveRoleReturnOkStatusCodeTest() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(ROLE_FIELD, LANDLORD_ROLE);
        String requestBody = mapper.writeValueAsString(data);
        doNothing().when(accountService).archiveRole(any(), any());

        mockMvc.perform(
                delete(CHANGE_ROLE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isOk());
    }

    @Test
    void archiveRoleReturnNotFoundStatusCodeAndResourceNotExceptionDtoWhenSpecifiedAccountDoesNotExistTest() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(ROLE_FIELD, LANDLORD_ROLE);
        String requestBody = mapper.writeValueAsString(data);
        doThrow(new AccountNotFoundException()).when(accountService).archiveRole(any(), any());

        mockMvc.perform(
                delete(CHANGE_ROLE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.message").value(ExceptionMessages.ACCOUNT_NOT_FOUND));
    }

    @Test
    void archiveRoleReturnUnprocessableEntityStatusAndValidationErrorsDtoWhenSpecifiedRoleIsNull() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(ROLE_FIELD, null);
        String requestBody = mapper.writeValueAsString(data);
        doNothing().when(accountService).archiveRole(any(), any());

        mockMvc.perform(
                delete(CHANGE_ROLE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorCode").value(422))
                .andExpect(jsonPath("$.validationErrors[0].fieldName").value(ROLE_FIELD))
                .andExpect(jsonPath("$.validationErrors[0].message").value(ExceptionMessages.FIELD_NULL_VALUE));
    }

    @Test
    void archiveRoleReturnUnprocessableEntityCodeAndValidationErrorsDtoWhenSpecifiedRoleDoesNotExist() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(ROLE_FIELD, INVALID_ROLE);
        String requestBody = mapper.writeValueAsString(data);
        doNothing().when(accountService).archiveRole(any(), any());

        mockMvc.perform(
                delete(CHANGE_ROLE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorCode").value(422))
                .andExpect(jsonPath("$.validationErrors[0].fieldName").value(ROLE_FIELD))
                .andExpect(jsonPath("$.validationErrors[0].message").value(ExceptionMessages.ROLE_DOES_NOT_EXIST));
    }

    @Test
    void archiveRoleReturnConflictStatusCodeAndBusinessLogicExceptionDtoWhenAccountDoesNotOweSpecifiedRoleTest() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(ROLE_FIELD, CLIENT_ROLE);
        String requestBody = mapper.writeValueAsString(data);
        doThrow(new RoleIsNotOwnedException()).when(accountService).archiveRole(any(), any());

        mockMvc.perform(
                delete(CHANGE_ROLE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value(409))
                .andExpect(jsonPath("$.message").value(ExceptionMessages.ROlE_IS_NOT_OWNED));
    }

    @Test
    void archiveRoleReturnConflictStatusCodeAndBusinessLogicExceptionDtoWhenArchiveRoleArchivesTheLastActiveRoleTest() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(ROLE_FIELD, ADMIN_ROLE);
        String requestBody = mapper.writeValueAsString(data);
        doThrow(new AccountDoesNotOweAnyRoleException()).when(accountService).archiveRole(any(), any());

        mockMvc.perform(
                delete(CHANGE_ROLE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value(409))
                .andExpect(jsonPath("$.message").value(ExceptionMessages.ACCOUNT_DOES_NOT_OWE_ANY_ROLE));

    }

    @Test
    void blockAccountReturnOkStatusCodeWhenAccountIsBlockedSuccessfullyTest() throws Exception {
        doNothing().when(accountService).blockAccount(any());

        mockMvc.perform(
                post(BLOCK_ACCOUNT_ENDPOINT)
        )
                .andExpect(status().isOk());
    }

    @Test
    void blockAccountReturnNotFoundStatusCodeAndResourceNotFountDtoWhenAccountDoesNotExistTest() throws Exception {
        doThrow(new AccountNotFoundException()).when(accountService).blockAccount(any());

        mockMvc.perform(
                post(BLOCK_ACCOUNT_ENDPOINT)
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.message").value(ExceptionMessages.ACCOUNT_NOT_FOUND));
    }

    @Test
    void blockAccountReturnConflictStatusAndBusinessLogicExceptionDtoWhenAccountIsAlreadyBlocked() throws Exception {
        doThrow(new AccountAlreadyBlockedException()).when(accountService).blockAccount(any());

        mockMvc.perform(
                post(BLOCK_ACCOUNT_ENDPOINT)
        )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value(409))
                .andExpect(jsonPath("$.message").value(ExceptionMessages.ACCOUNT_ALREADY_BLOCKED));
    }

    @Test
    void activateAccountReturnOkStatusCodeWhenAccountIsSuccessfullyActivated() throws Exception {
        doNothing().when(accountService).activateAccount(any());

        mockMvc.perform(
                post(ACTIVATE_ACCOUNT_ENDPOINT)
        )
                .andExpect(status().isOk());
    }

    @Test
    void activateAccountReturnNotFoundStatusCodeAndResourceNotFoundDtoWhenSpecifiedAccountDoesNotExist() throws Exception {
        doThrow(new AccountNotFoundException()).when(accountService).activateAccount(any());

        mockMvc.perform(
                post(ACTIVATE_ACCOUNT_ENDPOINT)
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
                post(ACTIVATE_ACCOUNT_ENDPOINT)
        )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value(409))
                .andExpect(jsonPath("$.message").value(ExceptionMessages.ACCOUNT_ALREADY_ACTIVE));
    }

    @Test
    void getAllAccountsMatchingPhraseWithPaginationReturnsNotEmptyPage() throws Exception {
        List<AccountOnPageDTO> pageContent = new ArrayList<>();
        pageContent.add(ACCOUNT_ON_PAGE_DTO_NO_1);
        pageContent.add(ACCOUNT_ON_PAGE_DTO_NO_2);

        Integer totalPages = 2;
        long totalElements = 4;
        int pageSize = 2;
        int pageNumber = 0;

        PagingResult<AccountOnPageDTO> accountPageDTO = new PagingResult<>(
                pageContent,
                totalPages,
                totalElements,
                pageSize,
                pageNumber,
                false
        );

        Map<String, Object> data = new HashMap<>();
        data.put(PAGE_NUMBER_FIELD, pageNumber);
        data.put(PAGE_SIZE_FIELD, pageSize);
        data.put(DIRECTION_FIELD, DIRECTION_ASC);
        data.put(SORT_FIELD, USERNAME_FIELD);
        data.put(PHRASES_FIELD, new ArrayList<>(List.of("user")));
        String requestBody = mapper.writeValueAsString(data);
        when(accountService.getAllAccountsMatchingPhrasesWithPagination(eq(pageNumber), eq(pageSize), eq(Sort.Direction.ASC),
                eq(USERNAME_FIELD), anyList())).thenReturn(accountPageDTO);

        mockMvc.perform(
                get(GET_ACCOUNTS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].username").value(USERNAME_NO_1))
                .andExpect(jsonPath("$.content[0].firstName").value(FIRST_NAME_NO_1))
                .andExpect(jsonPath("$.content[0].lastName").value(LAST_NAME_NO_1))
                .andExpect(jsonPath("$.content[1].username").value(USERNAME_NO_2))
                .andExpect(jsonPath("$.content[1].firstName").value(FIRST_NAME_NO_2))
                .andExpect(jsonPath("$.content[1].lastName").value(LAST_NAME_NO_2))
                .andExpect(jsonPath("$.totalPages").value(totalPages))
                .andExpect(jsonPath("$.totalElements").value(totalElements))
                .andExpect(jsonPath("$.size").value(pageSize))
                .andExpect(jsonPath("$.page").value(pageNumber))
                .andExpect(jsonPath("$.empty").value(false));
    }

    @Test
    void getAllAccountsMatchingPhraseWithPaginationReturnsEmptyPage() throws Exception {
        List<AccountOnPageDTO> pageContent = new ArrayList<>();

        Integer totalPages = 0;
        long totalElements = 0;
        int pageSize = 2;
        int pageNumber = 0;

        PagingResult<AccountOnPageDTO> accountPageDTO = new PagingResult<>(
                pageContent,
                totalPages,
                totalElements,
                pageSize,
                pageNumber,
                true
        );

        Map<String, Object> data = new HashMap<>();
        data.put(PAGE_NUMBER_FIELD, pageNumber);
        data.put(PAGE_SIZE_FIELD, pageSize);
        data.put(DIRECTION_FIELD, DIRECTION_ASC);
        data.put(SORT_FIELD, USERNAME_FIELD);
        data.put(PHRASES_FIELD, new ArrayList<>(List.of("not_existing_user")));
        String requestBody = mapper.writeValueAsString(data);
        when(accountService.getAllAccountsMatchingPhrasesWithPagination(eq(pageNumber), eq(pageSize), eq(Sort.Direction.ASC),
                eq(USERNAME_FIELD), anyList())).thenReturn(accountPageDTO);

        mockMvc.perform(
                        get(GET_ACCOUNTS_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalPages").value(totalPages))
                .andExpect(jsonPath("$.totalElements").value(totalElements))
                .andExpect(jsonPath("$.size").value(pageSize))
                .andExpect(jsonPath("$.page").value(pageNumber))
                .andExpect(jsonPath("$.empty").value(true));
    }

    @Test
    void getAccountByIdReturnsAccountDtoAndStatusCodeOk() throws Exception {
        List<String> roles = new ArrayList<>();
        roles.add(RolesEnum.LANDLORD.name());
        roles.add(RolesEnum.CLIENT.name());
        AccountDTO accountDTO = new AccountDTO(
                FIRST_NAME_NO_1,
                LAST_NAME_NO_1,
                USERNAME_NO_1,
                EMAIL_NO_1,
                true,
                true,
                roles);
        when(accountService.getAccountById(any())).thenReturn(accountDTO);

        mockMvc.perform(
                get(GET_ACCOUNT_BY_ID_ENDPOINT)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(FIRST_NAME_NO_1))
                .andExpect(jsonPath("$.lastName").value(LAST_NAME_NO_1))
                .andExpect(jsonPath("$.username").value(USERNAME_NO_1))
                .andExpect(jsonPath("$.email").value(EMAIL_NO_1))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.verified").value(true))
                .andExpect(jsonPath("$.roles").isArray())
                .andExpect(jsonPath("$.roles", hasSize(2)))
                .andExpect(jsonPath("$.roles[0]").value(RolesEnum.LANDLORD.name()))
                .andExpect(jsonPath("$.roles[1]").value(RolesEnum.CLIENT.name()));
    }

    @Test
    void getAccountByIdReturnResourceNotFoundDTOAndNotFoundStatusCode() throws Exception {
        when(accountService.getAccountById(any())).thenThrow(new AccountNotFoundException());

        mockMvc.perform(
                get(GET_ACCOUNT_BY_ID_ENDPOINT))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.message").value(ExceptionMessages.ACCOUNT_NOT_FOUND));
    }
}