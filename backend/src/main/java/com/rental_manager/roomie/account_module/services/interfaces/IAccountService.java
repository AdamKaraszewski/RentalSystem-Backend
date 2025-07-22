package com.rental_manager.roomie.account_module.services.interfaces;

import com.rental_manager.roomie.entities.Account;
import com.rental_manager.roomie.entities.roles.RolesEnum;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.*;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.AccountNotFoundException;

import java.util.UUID;

public interface IAccountService {

    void registerClient(Account account);

    void addRole(UUID accountId, RolesEnum roleName) throws AccountNotFoundException, RoleAlreadyOwnedException;

    void archiveRole(UUID accountId, RolesEnum roleName) throws AccountNotFoundException, RoleIsNotOwnedException,
            AccountDoesNotOweAnyRoleException;

    void blockAccount(UUID accountId) throws AccountNotFoundException, AccountAlreadyBlockedException;

    void activateAccount(UUID accountId) throws AccountNotFoundException, AccountAlreadyActiveException;
}
