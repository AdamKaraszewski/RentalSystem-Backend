package com.rental_manager.roomie.utils.account_module_utils;

import com.rental_manager.roomie.entities.Account;
import com.rental_manager.roomie.entities.roles.RolesEnum;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.rental_manager.roomie.AccountModuleTestUtility.*;
import static org.junit.jupiter.api.Assertions.*;

class AccountConverterTest {


    private static final Account account_no_1 = createAccount(
            FIRST_NAME_NO_1,
            LAST_NAME_NO_1,
            USERNAME_NO_1,
            EMAIL_NO_1,
            true,
            true,
            new ArrayList<>(List.of(RolesEnum.CLIENT))
    );

    private static final Account account_no_2 = createAccount(
            FIRST_NAME_NO_2,
            LAST_NAME_NO_2,
            USERNAME_NO_2,
            EMAIL_NO_2,
            true,
            true,
            new ArrayList<>(List.of(RolesEnum.CLIENT, RolesEnum.LANDLORD))
    );

    @Test
    void convertRegisterClientDTOToAccountTest() {
        var actual = AccountConverter.convertRegisterClientDTOToAccount(TEST_REGISTER_CLIENT_DTO);
        assertNotNull(actual);
        assertNull(actual.getId());
        assertEquals(FIRST_NAME_NO_1, actual.getFirstName());
        assertEquals(LAST_NAME_NO_1, actual.getLastName());
        assertEquals(USERNAME_NO_1, actual.getUsername());
        assertEquals(EMAIL_NO_1, actual.getEmail());
        assertTrue(actual.isActive());
        assertFalse(actual.isVerified());
        assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void convertAccountToAccountOnPageDTO() {
        var actual = AccountConverter.convertAccountToAccountOnPageDto(account_no_1);
        assertNotNull(actual);
        assertEquals(USERNAME_NO_1, actual.getUsername());
        assertEquals(FIRST_NAME_NO_1, actual.getFirstName());
        assertEquals(LAST_NAME_NO_1, actual.getLastName());
    }

    @Test
    void convertAccountToAccountDTO() {

        var actual = AccountConverter.convertAccountToAccountDto(account_no_2);
        assertNotNull(actual);
        assertEquals(FIRST_NAME_NO_2, actual.getFirstName());
        assertEquals(LAST_NAME_NO_2, actual.getLastName());
        assertEquals(USERNAME_NO_2, actual.getUsername());
        assertTrue(actual.isVerified());
        assertTrue(actual.isActive());
        assertEquals(2, actual.getRoles().size());
        assertTrue(actual.getRoles().contains(RolesEnum.CLIENT.name()));
        assertTrue(actual.getRoles().contains(RolesEnum.LANDLORD.name()));
    }
}
