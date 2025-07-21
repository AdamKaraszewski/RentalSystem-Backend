package com.rental_manager.roomie.account_module.controllers.implementations;

import com.rental_manager.roomie.AccountModuleTestUtility;
import com.rental_manager.roomie.account_module.services.implementations.ResetPasswordService;
import com.rental_manager.roomie.exceptions.ExceptionMessages;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.AccountNotFoundException;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.ResetPasswordTokenDoesNotMatchException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ResetPasswordController.class)
class ResetPasswordControllerTest {

    private static final String BASE_ENDPOINT = "/reset-password";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ResetPasswordService resetPasswordService;

    @Test
    void generateResetPasswordTokenReturnCreatedStatusCodeTest() throws Exception {
        doNothing().when(resetPasswordService).generateResetPasswordToken(any());

        mockMvc.perform(
                post(BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "email": "example.email@example.com"
                                }
                                """)
        )
                .andExpect(status().isCreated());
    }

    @Test
    void generateResetPasswordTokenReturnNotFoundStatusAndResourceNotFoundExceptionDtoWhenAccountDoesNotExistTest() throws Exception {
        doThrow(new AccountNotFoundException()).when(resetPasswordService).generateResetPasswordToken(any());

        mockMvc.perform(
                post(BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "email": "example.email@example.com"
                                }
                                """)
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.message").value(ExceptionMessages.ACCOUNT_NOT_FOUND));
    }

    @Test
    void resetPasswordReturnOkStatusCodeTest() throws Exception {
        doNothing().when(resetPasswordService).resetPassword(any(), any(), any());

        mockMvc.perform(
                post(BASE_ENDPOINT + "/" + AccountModuleTestUtility.RESET_PASSWORD_TOKEN_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "newPassword": "password",
                                "repeatNewPassword": "password"
                                }
                                """)
        )
                .andExpect(status().isOk());
    }

    @Test
    void resetPasswordReturnNotFoundStatusCodeAndResourceNotFoundDtoWhenSpecifiedResetTokenValueDoesNotExist() throws Exception {
        doThrow(new ResetPasswordTokenDoesNotMatchException()).when(resetPasswordService).resetPassword(any(), any(), any());

        mockMvc.perform(
                post(BASE_ENDPOINT + "/" + AccountModuleTestUtility.RESET_PASSWORD_TOKEN_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "newPassword": "password",
                                "repeatNewPassword": "password"
                                }
                                """)
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.message").value(ExceptionMessages.RESET_PASSWORD_TOKEN_DOES_NOT_MATCH));
    }
}