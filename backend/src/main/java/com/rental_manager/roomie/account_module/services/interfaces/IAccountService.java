package com.rental_manager.roomie.account_module.services.interfaces;

import com.rental_manager.roomie.account_module.dtos.AccountDTO;
import com.rental_manager.roomie.account_module.dtos.AccountOnPageDTO;
import com.rental_manager.roomie.entities.Account;
import com.rental_manager.roomie.entities.roles.RolesEnum;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.*;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.AccountNotFoundException;
import com.rental_manager.roomie.utils.searching_with_pagination.PagingResult;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

public interface IAccountService {

    void registerClient(Account account);

    void addRole(UUID accountId, RolesEnum roleName) throws AccountNotFoundException, RoleAlreadyOwnedException;

    void archiveRole(UUID accountId, RolesEnum roleName) throws AccountNotFoundException, RoleIsNotOwnedException,
            AccountDoesNotOweAnyRoleException;

    void blockAccount(UUID accountId) throws AccountNotFoundException, AccountAlreadyBlockedException;

    void activateAccount(UUID accountId) throws AccountNotFoundException, AccountAlreadyActiveException;

    PagingResult<AccountOnPageDTO> getAllAccountsMatchingPhrasesWithPagination(int pageNumber, int pageSize, Sort.Direction direction, String sortField, List<String> phrases);

    AccountDTO getAccountById(UUID id) throws AccountNotFoundException;

    void changeMyPassword(UUID accountId, String newPassword) throws AccountNotFoundException;

    AccountDTO editMyAccount(UUID accountId, String newFirstName, String newLastName) throws AccountNotFoundException;
}
