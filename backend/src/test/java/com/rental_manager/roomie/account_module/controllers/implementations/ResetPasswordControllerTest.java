package com.rental_manager.roomie.account_module.controllers.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rental_manager.roomie.account_module.services.implementations.ResetPasswordService;
import com.rental_manager.roomie.exceptions.ExceptionMessages;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.AccountNotFoundException;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.ResetPasswordTokenDoesNotMatchException;
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
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ResetPasswordController.class)
@ActiveProfiles("test")
class ResetPasswordControllerTest {

    private static final String GENERATE_RESET_PASSWORD_TOKEN_ENDPOINT = "/reset-password";
    private static final String RESET_PASSWORD_ENDPOINT = "/reset-password/" + RESET_PASSWORD_TOKEN_VALUE;

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ResetPasswordService resetPasswordService;

    @Test
    void generateResetPasswordTokenReturnCreatedStatusCodeTest() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(EMAIL_FIELD, EMAIL_NO_1);
        String requestBody = mapper.writeValueAsString(data);
        doNothing().when(resetPasswordService).generateResetPasswordToken(any());

        mockMvc.perform(
                post(GENERATE_RESET_PASSWORD_TOKEN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isCreated());
    }

    @Test
    void generateResetPasswordTokenReturnNotFoundStatusAndResourceNotFoundExceptionDtoWhenAccountDoesNotExistTest() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(EMAIL_FIELD, EMAIL_NO_1);
        String requestBody = mapper.writeValueAsString(data);
        doThrow(new AccountNotFoundException()).when(resetPasswordService).generateResetPasswordToken(any());

        mockMvc.perform(
                post(GENERATE_RESET_PASSWORD_TOKEN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.message").value(ExceptionMessages.ACCOUNT_NOT_FOUND));
    }

    @Test
    void resetPasswordReturnOkStatusCodeTest() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(NEW_PASSWORD_FIELD, PASSWORD);
        data.put(REPEAT_NEW_PASSWORD_FIELD, PASSWORD);
        String requestBody = mapper.writeValueAsString(data);
        doNothing().when(resetPasswordService).resetPassword(any(), any(), any());

        mockMvc.perform(
                post(RESET_PASSWORD_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isOk());
    }

    @Test
    void resetPasswordReturnNotFoundStatusCodeAndResourceNotFoundDtoWhenSpecifiedResetTokenValueDoesNotExist() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(NEW_PASSWORD_FIELD, PASSWORD);
        data.put(REPEAT_NEW_PASSWORD_FIELD, PASSWORD);
        String requestBody = mapper.writeValueAsString(data);
        doThrow(new ResetPasswordTokenDoesNotMatchException()).when(resetPasswordService).resetPassword(any(), any(), any());

        mockMvc.perform(
                post(RESET_PASSWORD_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.message").value(ExceptionMessages.RESET_PASSWORD_TOKEN_DOES_NOT_MATCH));
    }
}