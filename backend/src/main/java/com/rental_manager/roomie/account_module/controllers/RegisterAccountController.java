package com.rental_manager.roomie.account_module.controllers;

import com.rental_manager.roomie.account_module.dtos.RegisterClientDTO;
import com.rental_manager.roomie.account_module.services.AccountService;
import com.rental_manager.roomie.account_module.utils.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterAccountController implements IRegisterAccountController {

    private final AccountService accountService;

    public RegisterAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    @Override
    public ResponseEntity<Void> registerClientAccount(@RequestBody RegisterClientDTO registerClientDTO) {
        accountService.registerClient(Converter.convertRegisterClientDTOToAccount(registerClientDTO));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
