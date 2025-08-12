package com.rental_manager.roomie.account_module.controllers.implementations;

import com.rental_manager.roomie.AccountModuleTestUtility;
import com.rental_manager.roomie.account_module.services.implementations.AccountVerificationService;
import com.rental_manager.roomie.exceptions.ExceptionMessages;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.VerificationTokenDoesNotMatchException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VerifyAccountController.class)
@ActiveProfiles("test")
class VerifyAccountControllerTest {

    private static final String BASE_ENDPOINT = "/verify-account";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountVerificationService accountVerificationService;

    @Test
    void verifyAccountUsingVerificationTokenReturnOkStatusCode() throws Exception {
        doNothing().when(accountVerificationService).verifyAccountUsingVerificationToken(any());

        mockMvc.perform(
                post(BASE_ENDPOINT + "/" + AccountModuleTestUtility.VERIFICATION_TOKEN_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());
    }

    @Test
    void verifyAccountUsingVerificationTokenReturnNotFoundStatusCodeAndResourceNotFoundDtoWhenSpecifiedTokenValueDoesNotExistTest()
        throws Exception {
        doThrow(new VerificationTokenDoesNotMatchException()).when(accountVerificationService).verifyAccountUsingVerificationToken(any());

        mockMvc.perform(
                        post(BASE_ENDPOINT + "/" + AccountModuleTestUtility.VERIFICATION_TOKEN_VALUE)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.message").value(ExceptionMessages.VERIFICATION_TOKEN_DOES_NOT_MATCH));
    }

}