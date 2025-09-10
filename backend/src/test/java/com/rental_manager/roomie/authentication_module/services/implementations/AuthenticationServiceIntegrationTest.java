package com.rental_manager.roomie.authentication_module.services.implementations;

import com.rental_manager.roomie.authentication_module.repositories.AccountAuthenticationRepository;
import com.rental_manager.roomie.entities.Account;
import com.rental_manager.roomie.entities.roles.RolesEnum;
import com.rental_manager.roomie.exceptions.ExceptionMessages;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static com.rental_manager.roomie.AccountModuleTestUtility.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceIntegrationTest {

    @Mock
    private AccountAuthenticationRepository accountAuthenticationRepository;

    @InjectMocks
    private AuthenticationService underTest;

    private final Account account = createAccount(
            FIRST_NAME_NO_1,
            LAST_NAME_NO_1,
            USERNAME_NO_1,
            EMAIL_NO_1,
            true,
            true,
            List.of(RolesEnum.CLIENT)
    );

    @Test
    void findByUsernameReturnUserWithSpecifiedEmail() {
        when(accountAuthenticationRepository.findByEmail(eq(EMAIL_NO_1))).thenReturn(Optional.of(account));

        UserDetails accountFound = underTest.loadUserByUsername(EMAIL_NO_1);

        assertNotNull(accountFound);
        assertEquals(account, accountFound);
    }

    @Test
    void findByUsernameThrowsUsernameNotFoundExceptionWhenUserWithSpecifiedEmail() {
        when(accountAuthenticationRepository.findByEmail(eq(EMAIL_NO_1))).thenReturn(Optional.empty());

        var exceptionThrown = assertThrows(UsernameNotFoundException.class, () -> {
            underTest.loadUserByUsername(EMAIL_NO_1);
        });

        assertEquals(ExceptionMessages.ACCOUNT_NOT_FOUND, exceptionThrown.getMessage());
    }
}
