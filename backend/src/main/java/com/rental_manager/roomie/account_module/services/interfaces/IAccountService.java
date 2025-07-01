package com.rental_manager.roomie.account_module.services.interfaces;

import com.rental_manager.roomie.entities.Account;
import com.rental_manager.roomie.entities.roles.RolesEnum;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.AccountDoesNotOweAnyRoleException;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.RoleAlreadyOwnedException;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.RoleDoesNotExistException;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.RoleIsNotOwnedException;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.AccountNotFoundException;

import java.util.UUID;

public interface IAccountService {

    void registerClient(Account account);

    void addRole(UUID accountId, RolesEnum roleName) throws AccountNotFoundException, RoleDoesNotExistException,
            RoleAlreadyOwnedException;

    void archiveRole(UUID accountId, RolesEnum roleName) throws AccountNotFoundException, RoleIsNotOwnedException,
            AccountDoesNotOweAnyRoleException;
}
