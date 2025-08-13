package com.rental_manager.roomie.account_module.controllers.implementations;

import com.rental_manager.roomie.account_module.controllers.interfaces.IManageMyAccountController;
import com.rental_manager.roomie.account_module.dtos.ChangeMyPasswordDTO;
import com.rental_manager.roomie.account_module.services.implementations.AccountService;
import com.rental_manager.roomie.account_module.services.interfaces.IAccountService;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.AccountNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/accounts/my")
public class ManageMyAccountController implements IManageMyAccountController {

    private final IAccountService accountService;

    public ManageMyAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

//    todo: remove id argument.
//    todo: Get id form security context
//    @Override
//    @PostMapping("/{id}/password")
//    public ResponseEntity<Void> changeMyPassword(@PathVariable UUID id, @RequestBody ChangeMyPasswordDTO changeMyPasswordDTO)
//            throws AccountNotFoundException {
//        accountService.changeMyPassword(id, changeMyPasswordDTO.getNewPassword());
//        return new ResponseEntity<Void>(HttpStatus.OK);
//    }
}
