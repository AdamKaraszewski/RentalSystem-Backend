package com.rental_manager.roomie.entities;

import com.rental_manager.roomie.entities.roles.RolesEnum;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

import static com.rental_manager.roomie.AccountModuleTestUtility.*;
import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {

    @Test
    void getAuthoritiesReturnsOnlyActiveRoles() {
        var account = createAccount(
                FIRST_NAME_NO_1,
                LAST_NAME_NO_1,
                USERNAME_NO_1,
                EMAIL_NO_1,
                true,
                true,
                List.of(RolesEnum.CLIENT, RolesEnum.LANDLORD, RolesEnum.ADMIN)
                );

        account.getRoles().stream()
                .filter(r -> r.getRole() == RolesEnum.LANDLORD)
                .findFirst()
                .ifPresent(role -> role.setActive(false));

        Collection<? extends GrantedAuthority> authorities = account.getAuthorities();
        var clientRole = authorities.stream()
                .filter(a -> a.getAuthority().equals(RolesEnum.CLIENT.name()))
                .findFirst();

        var adminRole = authorities.stream()
                .filter(a -> a.getAuthority().equals(RolesEnum.ADMIN.name()))
                .findFirst();

        assertEquals(2, authorities.size());
        assertTrue(clientRole.isPresent());
        assertTrue(adminRole.isPresent());
    }
}
