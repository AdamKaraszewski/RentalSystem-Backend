package com.rental_manager.roomie.account_module.controllers.implementations;

import com.rental_manager.roomie.account_module.controllers.interfaces.IVerifyAccountController;
import com.rental_manager.roomie.account_module.services.implementations.AccountVerificationService;
import com.rental_manager.roomie.account_module.services.interfaces.IAccountVerificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verify-account")
public class VerifyAccountController implements IVerifyAccountController {

    private final IAccountVerificationService accountVerificationService;

    public VerifyAccountController(AccountVerificationService accountVerificationService) {
        this.accountVerificationService = accountVerificationService;
    }

    @Override
    @PostMapping("/{token}")
    public ResponseEntity<Void> verifyAccountUsingVerificationToken(@PathVariable String token) {
        accountVerificationService.verifyAccountUsingVerificationToken(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
