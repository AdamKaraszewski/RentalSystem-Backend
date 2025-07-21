package com.rental_manager.roomie.account_module.controllers.implementations;

import com.rental_manager.roomie.account_module.services.implementations.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegisterAccountController.class)
class RegisterAccountControllerTest {

    private static final String BASE_ENDPOINT = "/register";

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AccountService accountService;

    @Test
    void registerClientReturnCreatedStausCodeTest() throws Exception {
        doNothing().when(accountService).registerClient(any());

        mvc.perform(
                post(BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "firstName": "simple_first_name",
                                "lastName": "simple_last_name",
                                "username": "simple_username",
                                "email": "simple.emali@example.com",
                                "password": "simple_password"
                                }
                                """)
        )
                .andExpect(status().isCreated());
    }
}