package com.rental_manager.roomie.account_module.services.implementations;

import com.rental_manager.roomie.account_module.repositories.AccountRepository;
import com.rental_manager.roomie.account_module.repositories.VerificationTokenRepository;
import com.rental_manager.roomie.entities.roles.RolesEnum;
import com.rental_manager.roomie.exceptions.ExceptionMessages;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.VerificationTokenDoesNotMatchException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.rental_manager.roomie.AccountModuleTestUtility.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountVerificationServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @InjectMocks
    private AccountVerificationService underTest;

    @Test
    void verifyAccountUsingVerificationTokenTest() {
        var accountToBeVerified = createAccount(
                FIRST_NAME_NO_1,
                LAST_NAME_NO_1,
                USERNAME_NO_1,
                EMAIL_NO_1,
                false,
                true,
                List.of(RolesEnum.CLIENT)
        );
        var verificationToken = createVerificationToken(accountToBeVerified, VERIFICATION_TOKEN_VALUE);
        when(verificationTokenRepository.findByTokenValueAndExpirationDateAfter(eq(VERIFICATION_TOKEN_VALUE), any(LocalDateTime.class)))
                .thenReturn(Optional.of(verificationToken));
        when(accountRepository.saveAndFlush(accountToBeVerified)).thenReturn(accountToBeVerified);
        doNothing().when(verificationTokenRepository).delete(verificationToken);

        underTest.verifyAccountUsingVerificationToken(VERIFICATION_TOKEN_VALUE);

        assertTrue(accountToBeVerified.isVerified());
    }

    @Test
    void verifyAccountUsingVerificationThrowsVerificationTokenDoesNotMatchException() {
        when(verificationTokenRepository.findByTokenValueAndExpirationDateAfter(eq(VERIFICATION_TOKEN_VALUE), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        var exceptionThrown = assertThrows(VerificationTokenDoesNotMatchException.class, () -> {
            underTest.verifyAccountUsingVerificationToken(VERIFICATION_TOKEN_VALUE);
        });

        assertEquals(ExceptionMessages.VERIFICATION_TOKEN_DOES_NOT_MATCH, exceptionThrown.getMessage());
    }

}