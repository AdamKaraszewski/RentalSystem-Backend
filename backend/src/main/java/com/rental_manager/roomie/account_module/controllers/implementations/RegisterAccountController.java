package com.rental_manager.roomie.account_module.controllers.implementations;

import com.rental_manager.roomie.account_module.controllers.interfaces.IRegisterAccountController;
import com.rental_manager.roomie.account_module.dtos.RegisterClientDTO;
import com.rental_manager.roomie.account_module.services.implementations.AccountService;
import com.rental_manager.roomie.account_module.services.interfaces.IAccountService;
import com.rental_manager.roomie.utils.account_module_utils.AccountConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegisterAccountController implements IRegisterAccountController {

    private final IAccountService accountService;

    public RegisterAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    @PostMapping
    public ResponseEntity<Void> registerClientAccount(@RequestBody RegisterClientDTO registerClientDTO) {
        accountService.registerClient(AccountConverter.convertRegisterClientDTOToAccount(registerClientDTO));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
