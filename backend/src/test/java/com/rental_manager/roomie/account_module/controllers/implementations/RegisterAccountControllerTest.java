package com.rental_manager.roomie.account_module.controllers.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rental_manager.roomie.account_module.services.implementations.AccountService;
import com.rental_manager.roomie.exceptions.ExceptionMessages;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static com.rental_manager.roomie.AccountModuleTestUtility.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegisterAccountController.class)
@ActiveProfiles("test")
class RegisterAccountControllerTest {

    private static final String BASE_ENDPOINT = "/register";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void registerClientReturnCreatedStausCodeTest() throws Exception {
        Map<String , String> data = new HashMap<>();
        data.put(FIRST_NAME_FIELD, FIRST_NAME_NO_1);
        data.put(LAST_NAME_FIELD, LAST_NAME_NO_1);
        data.put(USERNAME_FIELD, USERNAME_NO_1);
        data.put(EMAIL_FIELD, EMAIL_NO_1);
        data.put(PASSWORD_FILED, PASSWORD);
        String requestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        doNothing().when(accountService).registerClient(any());

        mockMvc.perform(
                post(BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isCreated());
    }

    @Test
    void registerClientFirstNameNullThrowsValidationException() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(FIRST_NAME_FIELD, null);
        data.put(LAST_NAME_FIELD, LAST_NAME_NO_1);
        data.put(USERNAME_FIELD, USERNAME_NO_1);
        data.put(EMAIL_FIELD, EMAIL_NO_1);
        data.put(PASSWORD_FILED, PASSWORD);
        String requestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        doNothing().when(accountService).registerClient(any());

        mockMvc.perform(
                post(BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorCode").value(422))
                .andExpect(jsonPath("$.validationErrors[0].fieldName").value(FIRST_NAME_FIELD))
                .andExpect(jsonPath("$.validationErrors[0].message").value(ExceptionMessages.FIELD_NULL_VALUE));
    }

    @Test
    void registerClientFirstNameToShortThrowsValidationException() throws Exception {
        Map<String , String> data = new HashMap<>();
        data.put(FIRST_NAME_FIELD, TO_SHORT_FIRST_NAME);
        data.put(LAST_NAME_FIELD, LAST_NAME_NO_1);
        data.put(USERNAME_FIELD, USERNAME_NO_1);
        data.put(EMAIL_FIELD, EMAIL_NO_1);
        data.put(PASSWORD_FILED, PASSWORD);
        String requestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        doNothing().when(accountService).registerClient(any());

        mockMvc.perform(
                post(BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorCode").value(422))
                .andExpect(jsonPath("$.validationErrors[0].fieldName").value(FIRST_NAME_FIELD))
                .andExpect(jsonPath("$.validationErrors[0].message").value(ExceptionMessages.FIRST_NAME_LENGTH_NOT_VALID));
        ;
    }

    @Test
    void registerClientFirstNameToLongThrowsValidationException() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(FIRST_NAME_FIELD, TO_LONG_FIRST_NAME);
        data.put(LAST_NAME_FIELD, LAST_NAME_NO_1);
        data.put(USERNAME_FIELD, USERNAME_NO_1);
        data.put(EMAIL_FIELD, EMAIL_NO_1);
        data.put(PASSWORD_FILED, PASSWORD);
        String requestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        doNothing().when(accountService).registerClient(any());

        mockMvc.perform(
                post(BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorCode").value(422))
                .andExpect(jsonPath("$.validationErrors[0].fieldName").value(FIRST_NAME_FIELD))
                .andExpect(jsonPath("$.validationErrors[0].message").value(ExceptionMessages.FIRST_NAME_LENGTH_NOT_VALID));
    }

    @Test
    void registerClientLastNameNullThrowsValidationException() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(FIRST_NAME_FIELD, FIRST_NAME_NO_1);
        data.put(LAST_NAME_FIELD, null);
        data.put(USERNAME_FIELD, USERNAME_NO_1);
        data.put(EMAIL_FIELD, EMAIL_NO_1);
        data.put(PASSWORD_FILED, PASSWORD);
        String requestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        doNothing().when(accountService).registerClient(any());

        mockMvc.perform(
                post(BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorCode").value(422))
                .andExpect(jsonPath("$.validationErrors[0].fieldName").value(LAST_NAME_FIELD))
                .andExpect(jsonPath("$.validationErrors[0].message").value(ExceptionMessages.FIELD_NULL_VALUE));
    }

    @Test
    void registerClientLastNameToShortThrowsValidationException() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(FIRST_NAME_FIELD, FIRST_NAME_NO_1);
        data.put(LAST_NAME_FIELD, TO_SHORT_LAST_NAME);
        data.put(USERNAME_FIELD, USERNAME_NO_1);
        data.put(EMAIL_FIELD, EMAIL_NO_1);
        data.put(PASSWORD_FILED, PASSWORD);
        String requestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        doNothing().when(accountService).registerClient(any());

        mockMvc.perform(
                post(BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorCode").value(422))
                .andExpect(jsonPath("$.validationErrors[0].fieldName").value(LAST_NAME_FIELD))
                .andExpect(jsonPath("$.validationErrors[0].message").value(ExceptionMessages.LAST_NAME_LENGTH_NOT_VALID));
    }

    @Test
    void registerClientLastNameToLongThrowsValidationException() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(FIRST_NAME_FIELD, FIRST_NAME_NO_1);
        data.put(LAST_NAME_FIELD, TO_LONG_LAST_NAME);
        data.put(USERNAME_FIELD, USERNAME_NO_1);
        data.put(EMAIL_FIELD, EMAIL_NO_1);
        data.put(PASSWORD_FILED, PASSWORD);
        String requestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        doNothing().when(accountService).registerClient(any());

        mockMvc.perform(
                post(BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorCode").value(422))
                .andExpect(jsonPath("$.validationErrors[0].fieldName").value(LAST_NAME_FIELD))
                .andExpect(jsonPath("$.validationErrors[0].message").value(ExceptionMessages.LAST_NAME_LENGTH_NOT_VALID));
    }

    @Test
    void registerClientUsernameNullThrowsValidationException() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(FIRST_NAME_FIELD, FIRST_NAME_NO_1);
        data.put(LAST_NAME_FIELD, LAST_NAME_NO_1);
        data.put(USERNAME_FIELD, null);
        data.put(EMAIL_FIELD, EMAIL_NO_1);
        data.put(PASSWORD_FILED, PASSWORD);
        String requestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        doNothing().when(accountService).registerClient(any());

        mockMvc.perform(
                post(BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorCode").value(422))
                .andExpect(jsonPath("$.validationErrors[0].fieldName").value(USERNAME_FIELD))
                .andExpect(jsonPath("$.validationErrors[0].message").value(ExceptionMessages.FIELD_NULL_VALUE));
    }

    @Test
    void registerClientUsernameToShortThrowsValidationException() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(FIRST_NAME_FIELD, FIRST_NAME_NO_1);
        data.put(LAST_NAME_FIELD, LAST_NAME_NO_1);
        data.put(USERNAME_FIELD, TO_SHORT_USERNAME);
        data.put(EMAIL_FIELD, EMAIL_NO_1);
        data.put(PASSWORD_FILED, PASSWORD);
        String requestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        doNothing().when(accountService).registerClient(any());

        mockMvc.perform(
                post(BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorCode").value(422))
                .andExpect(jsonPath("$.validationErrors[0].fieldName").value(USERNAME_FIELD))
                .andExpect(jsonPath("$.validationErrors[0].message").value(ExceptionMessages.USERNAME_LENGTH_NOT_VALID));
    }

    @Test
    void registerClientUsernameToLongThrowsValidationException() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(FIRST_NAME_FIELD, FIRST_NAME_NO_1);
        data.put(LAST_NAME_FIELD, LAST_NAME_NO_1);
        data.put(USERNAME_FIELD, TO_LONG_USERNAME);
        data.put(EMAIL_FIELD, EMAIL_NO_1);
        data.put(PASSWORD_FILED, PASSWORD);
        String requestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        doNothing().when(accountService).registerClient(any());

        mockMvc.perform(
                post(BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorCode").value(422))
                .andExpect(jsonPath("$.validationErrors[0].fieldName").value(USERNAME_FIELD))
                .andExpect(jsonPath("$.validationErrors[0].message").value(ExceptionMessages.USERNAME_LENGTH_NOT_VALID));
    }

    @Test
    void registerClientEmailNullThrowsValidationException() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(FIRST_NAME_FIELD, FIRST_NAME_NO_1);
        data.put(LAST_NAME_FIELD, LAST_NAME_NO_1);
        data.put(USERNAME_FIELD, USERNAME_NO_1);
        data.put(EMAIL_FIELD, null);
        data.put(PASSWORD_FILED, PASSWORD);
        String requestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        doNothing().when(accountService).registerClient(any());

        mockMvc.perform(
                post(BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorCode").value(422))
                .andExpect(jsonPath("$.validationErrors[0].fieldName").value(EMAIL_FIELD))
                .andExpect(jsonPath("$.validationErrors[0].message").value(ExceptionMessages.FIELD_NULL_VALUE));
    }

    @Test
    void registerClientEmailRegexNotFollowedThrowsValidationException() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(FIRST_NAME_FIELD, FIRST_NAME_NO_1);
        data.put(LAST_NAME_FIELD, LAST_NAME_NO_1);
        data.put(USERNAME_FIELD, USERNAME_NO_1);
        data.put(EMAIL_FIELD, INVALID_EMAIL);
        data.put(PASSWORD_FILED, PASSWORD);
        String requestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        doNothing().when(accountService).registerClient(any());

        mockMvc.perform(
                post(BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorCode").value(422))
                .andExpect(jsonPath("$.validationErrors[0].fieldName").value(EMAIL_FIELD))
                .andExpect(jsonPath("$.validationErrors[0].message").value(ExceptionMessages.EMAIL_NOT_VALID));
    }

    @Test
    void registerClientPasswordNullThrowsValidationException() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(FIRST_NAME_FIELD, FIRST_NAME_NO_1);
        data.put(LAST_NAME_FIELD, LAST_NAME_NO_1);
        data.put(USERNAME_FIELD, USERNAME_NO_1);
        data.put(EMAIL_FIELD, EMAIL_NO_1);
        data.put(PASSWORD_FILED, null);
        String requestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        doNothing().when(accountService).registerClient(any());

        mockMvc.perform(
                post(BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorCode").value(422))
                .andExpect(jsonPath("$.validationErrors[0].fieldName").value(PASSWORD_FILED))
                .andExpect(jsonPath("$.validationErrors[0].message").value(ExceptionMessages.FIELD_NULL_VALUE));
    }

    @Test
    void registerClientPasswordToShortThrowsValidationException() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(FIRST_NAME_FIELD, FIRST_NAME_NO_1);
        data.put(LAST_NAME_FIELD, LAST_NAME_NO_1);
        data.put(USERNAME_FIELD, USERNAME_NO_1);
        data.put(EMAIL_FIELD, EMAIL_NO_1);
        data.put(PASSWORD_FILED, TO_SHORT_PASSWORD);
        String requestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        doNothing().when(accountService).registerClient(any());

        mockMvc.perform(
                post(BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorCode").value(422))
                .andExpect(jsonPath("$.validationErrors[0].fieldName").value(PASSWORD_FILED))
                .andExpect(jsonPath("$.validationErrors[0].message").value(ExceptionMessages.PASSWORD_LENGTH_NOT_VALID));
    }

    @Test
    void registerClientPasswordToLongThrowsValidationException() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(FIRST_NAME_FIELD, FIRST_NAME_NO_1);
        data.put(LAST_NAME_FIELD, LAST_NAME_NO_1);
        data.put(USERNAME_FIELD, USERNAME_NO_1);
        data.put(EMAIL_FIELD, EMAIL_NO_1);
        data.put(PASSWORD_FILED, TO_LONG_PASSWORD);
        String requestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        doNothing().when(accountService).registerClient(any());

        mockMvc.perform(
                post(BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorCode").value(422))
                .andExpect(jsonPath("$.validationErrors[0].fieldName").value(PASSWORD_FILED))
                .andExpect(jsonPath("$.validationErrors[0].message").value(ExceptionMessages.PASSWORD_LENGTH_NOT_VALID));
    }

}