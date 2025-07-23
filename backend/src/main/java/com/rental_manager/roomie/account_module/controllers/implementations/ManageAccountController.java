package com.rental_manager.roomie.account_module.controllers.implementations;

import com.rental_manager.roomie.account_module.controllers.interfaces.IManageAccountController;
import com.rental_manager.roomie.account_module.dtos.AccountOnPageDTO;
import com.rental_manager.roomie.account_module.dtos.ChangeRoleDTO;
import com.rental_manager.roomie.account_module.services.implementations.AccountService;
import com.rental_manager.roomie.account_module.services.interfaces.IAccountService;
import com.rental_manager.roomie.entities.roles.RolesEnum;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.AccountAlreadyBlockedException;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.AccountDoesNotOweAnyRoleException;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.RoleAlreadyOwnedException;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.RoleIsNotOwnedException;
import com.rental_manager.roomie.exceptions.validation_exceptions.RoleDoesNotExistException;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.AccountNotFoundException;
import com.rental_manager.roomie.utils.PagingResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class ManageAccountController implements IManageAccountController {

    private final IAccountService accountService;

    public ManageAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{accountId}/roles")
    @Override
    public ResponseEntity<Void> addRole(@PathVariable UUID accountId, @RequestBody ChangeRoleDTO changeRoleDTO) throws
            AccountNotFoundException, RoleAlreadyOwnedException {
        RolesEnum roleToBeAdded = changeRoleDTO.getRole();
        accountService.addRole(accountId, roleToBeAdded);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @DeleteMapping("/{accountId}/roles")
    @Override
    public ResponseEntity<Void> archiveRole(@PathVariable UUID accountId, @RequestBody ChangeRoleDTO changeRoleDTO)
            throws AccountNotFoundException, RoleIsNotOwnedException, AccountDoesNotOweAnyRoleException {
        RolesEnum roleToBeArchived = changeRoleDTO.getRole();
        accountService.archiveRole(accountId, roleToBeArchived);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping("/{accountId}/block")
    @Override
    public ResponseEntity<Void> blockAccount(@PathVariable UUID accountId) throws AccountNotFoundException,
            AccountAlreadyBlockedException {
        accountService.blockAccount(accountId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping("/{accountId}/activate")
    @Override
    public ResponseEntity<Void> activateAccount(@PathVariable UUID accountId) throws AccountNotFoundException,
            AccountAlreadyBlockedException {
        accountService.activateAccount(accountId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Override
    @GetMapping
    public ResponseEntity<PagingResult<AccountOnPageDTO>> getAllAccountsWithPagination(
            @RequestParam int pageNumber, @RequestParam int pageSize) {
        PagingResult<AccountOnPageDTO> responseBody = accountService.getAllAccountsWithPagination(pageNumber, pageSize);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
