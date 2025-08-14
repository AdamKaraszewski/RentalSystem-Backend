package com.rental_manager.roomie.account_module.controllers.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rental_manager.roomie.account_module.services.implementations.AccountService;
import com.rental_manager.roomie.exceptions.ExceptionMessages;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
    private static final Random random = new Random();

    private static final String FIRST_NAME_FIELD = "firstName";
    private static final String LAST_NAME_FIELD = "lastName";
    private static final String USERNAME_FIELD = "username";
    private static final String EMAIL_FIELD = "email";
    private static final String PASSWORD_FIELD = "password";

    private static final String VALID_FIRST_NAME = "simpleFirstName";
    private static final String TO_SHORT_FIRST_NAME = "";
    private static final String TO_LONG_FIRST_NAME = RandomStringUtils.random(33, 'a', 'z' + 1,
            true, false, null, random);

    private static final String VALID_LAST_NAME = "simpleLastName";
    private static final String TO_SHORT_LAST_NAME = "";
    private static final String TO_LONG_LAST_NAME = RandomStringUtils.random(33, 'a', 'z' + 1,
            true, false, null, random);

    private static final String VALID_USERNAME = "simple_username";
    private static final String TO_SHORT_USERNAME = "xy";
    private static final String TO_LONG_USERNAME = RandomStringUtils.random(33, 'a', 'z' + 1,
            true, false, null, random);

    private static final String VALID_EMAIL = "simple.email@example.com";
    private static final String INVALID_EMAIL = "simple.mail";

    private static final String VALID_PASSWORD = "simple_password";
    private static final String TO_SHORT_PASSWORD = "pass";
    private static final String TO_LONG_PASSWORD = RandomStringUtils.random(65, '0', 'z' + 1,
            true, true, null, random);


    @Test
    void registerClientReturnCreatedStausCodeTest() throws Exception {
        Map<String , String> data = new HashMap<>();
        data.put(FIRST_NAME_FIELD, VALID_FIRST_NAME);
        data.put(LAST_NAME_FIELD, VALID_LAST_NAME);
        data.put(USERNAME_FIELD, VALID_USERNAME);
        data.put(EMAIL_FIELD, VALID_EMAIL);
        data.put(PASSWORD_FIELD, VALID_PASSWORD);
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
        data.put(LAST_NAME_FIELD, VALID_LAST_NAME);
        data.put(USERNAME_FIELD, VALID_USERNAME);
        data.put(EMAIL_FIELD, VALID_EMAIL);
        data.put(PASSWORD_FIELD, VALID_PASSWORD);
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
        data.put(LAST_NAME_FIELD, VALID_LAST_NAME);
        data.put(USERNAME_FIELD, VALID_USERNAME);
        data.put(EMAIL_FIELD, VALID_EMAIL);
        data.put(PASSWORD_FIELD, VALID_PASSWORD);
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
        data.put(LAST_NAME_FIELD, VALID_LAST_NAME);
        data.put(USERNAME_FIELD, VALID_USERNAME);
        data.put(EMAIL_FIELD, VALID_EMAIL);
        data.put(PASSWORD_FIELD, VALID_PASSWORD);
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
        data.put(FIRST_NAME_FIELD, VALID_FIRST_NAME);
        data.put(LAST_NAME_FIELD, null);
        data.put(USERNAME_FIELD, VALID_USERNAME);
        data.put(EMAIL_FIELD, VALID_EMAIL);
        data.put(PASSWORD_FIELD, VALID_PASSWORD);
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
        data.put(FIRST_NAME_FIELD, VALID_FIRST_NAME);
        data.put(LAST_NAME_FIELD, TO_SHORT_LAST_NAME);
        data.put(USERNAME_FIELD, VALID_USERNAME);
        data.put(EMAIL_FIELD, VALID_EMAIL);
        data.put(PASSWORD_FIELD, VALID_PASSWORD);
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
        data.put(FIRST_NAME_FIELD, VALID_FIRST_NAME);
        data.put(LAST_NAME_FIELD, TO_LONG_LAST_NAME);
        data.put(USERNAME_FIELD, VALID_USERNAME);
        data.put(EMAIL_FIELD, VALID_EMAIL);
        data.put(PASSWORD_FIELD, VALID_PASSWORD);
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
        data.put(FIRST_NAME_FIELD, VALID_FIRST_NAME);
        data.put(LAST_NAME_FIELD, VALID_LAST_NAME);
        data.put(USERNAME_FIELD, null);
        data.put(EMAIL_FIELD, VALID_EMAIL);
        data.put(PASSWORD_FIELD, VALID_PASSWORD);
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
        data.put(FIRST_NAME_FIELD, VALID_FIRST_NAME);
        data.put(LAST_NAME_FIELD, VALID_LAST_NAME);
        data.put(USERNAME_FIELD, TO_SHORT_USERNAME);
        data.put(EMAIL_FIELD, VALID_EMAIL);
        data.put(PASSWORD_FIELD, VALID_PASSWORD);
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
        data.put(FIRST_NAME_FIELD, VALID_FIRST_NAME);
        data.put(LAST_NAME_FIELD, VALID_LAST_NAME);
        data.put(USERNAME_FIELD, TO_LONG_USERNAME);
        data.put(EMAIL_FIELD, VALID_EMAIL);
        data.put(PASSWORD_FIELD, VALID_PASSWORD);
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
        data.put(FIRST_NAME_FIELD, VALID_FIRST_NAME);
        data.put(LAST_NAME_FIELD, VALID_LAST_NAME);
        data.put(USERNAME_FIELD, VALID_USERNAME);
        data.put(EMAIL_FIELD, null);
        data.put(PASSWORD_FIELD, VALID_PASSWORD);
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
        data.put(FIRST_NAME_FIELD, VALID_FIRST_NAME);
        data.put(LAST_NAME_FIELD, VALID_LAST_NAME);
        data.put(USERNAME_FIELD, VALID_USERNAME);
        data.put(EMAIL_FIELD, INVALID_EMAIL);
        data.put(PASSWORD_FIELD, VALID_PASSWORD);
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
        data.put(FIRST_NAME_FIELD, VALID_FIRST_NAME);
        data.put(LAST_NAME_FIELD, VALID_LAST_NAME);
        data.put(USERNAME_FIELD, VALID_USERNAME);
        data.put(EMAIL_FIELD, VALID_EMAIL);
        data.put(PASSWORD_FIELD, null);
        String requestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        doNothing().when(accountService).registerClient(any());

        mockMvc.perform(
                post(BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorCode").value(422))
                .andExpect(jsonPath("$.validationErrors[0].fieldName").value(PASSWORD_FIELD))
                .andExpect(jsonPath("$.validationErrors[0].message").value(ExceptionMessages.FIELD_NULL_VALUE));
    }

    @Test
    void registerClientPasswordToShortThrowsValidationException() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(FIRST_NAME_FIELD, VALID_FIRST_NAME);
        data.put(LAST_NAME_FIELD, VALID_LAST_NAME);
        data.put(USERNAME_FIELD, VALID_USERNAME);
        data.put(EMAIL_FIELD, VALID_EMAIL);
        data.put(PASSWORD_FIELD, TO_SHORT_PASSWORD);
        String requestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        doNothing().when(accountService).registerClient(any());

        mockMvc.perform(
                post(BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorCode").value(422))
                .andExpect(jsonPath("$.validationErrors[0].fieldName").value(PASSWORD_FIELD))
                .andExpect(jsonPath("$.validationErrors[0].message").value(ExceptionMessages.PASSWORD_LENGTH_NOT_VALID));
    }

    @Test
    void registerClientPasswordToLongThrowsValidationException() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(FIRST_NAME_FIELD, VALID_FIRST_NAME);
        data.put(LAST_NAME_FIELD, VALID_LAST_NAME);
        data.put(USERNAME_FIELD, VALID_USERNAME);
        data.put(EMAIL_FIELD, VALID_EMAIL);
        data.put(PASSWORD_FIELD, TO_LONG_PASSWORD);
        String requestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        doNothing().when(accountService).registerClient(any());

        mockMvc.perform(
                post(BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorCode").value(422))
                .andExpect(jsonPath("$.validationErrors[0].fieldName").value(PASSWORD_FIELD))
                .andExpect(jsonPath("$.validationErrors[0].message").value(ExceptionMessages.PASSWORD_LENGTH_NOT_VALID));
    }

}