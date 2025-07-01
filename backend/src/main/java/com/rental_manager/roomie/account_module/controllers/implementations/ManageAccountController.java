package com.rental_manager.roomie.account_module.controllers.implementations;

import com.rental_manager.roomie.account_module.controllers.interfaces.IManageAccountController;
import com.rental_manager.roomie.account_module.dtos.ChangeRoleDTO;
import com.rental_manager.roomie.account_module.services.implementations.AccountService;
import com.rental_manager.roomie.account_module.services.interfaces.IAccountService;
import com.rental_manager.roomie.entities.roles.RolesEnum;
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
    public ResponseEntity<Void> addRole(@PathVariable UUID accountId, @RequestBody ChangeRoleDTO changeRoleDTO) {
        RolesEnum roleToBeAdded = changeRoleDTO.getRole();
        accountService.addRole(accountId, roleToBeAdded);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @DeleteMapping("/{accountId}/roles")
    @Override
    public ResponseEntity<Void> archiveRole(@PathVariable UUID accountId, @RequestBody ChangeRoleDTO changeRoleDTO) {
        RolesEnum roleToBeArchived = changeRoleDTO.getRole();
        accountService.archiveRole(accountId, roleToBeArchived);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
