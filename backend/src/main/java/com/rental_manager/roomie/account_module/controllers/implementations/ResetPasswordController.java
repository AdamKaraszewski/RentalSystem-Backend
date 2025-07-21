package com.rental_manager.roomie.account_module.controllers.implementations;

import com.rental_manager.roomie.account_module.controllers.interfaces.IResetPasswordController;
import com.rental_manager.roomie.account_module.dtos.GenerateResetPasswordTokenDTO;
import com.rental_manager.roomie.account_module.dtos.ResetPasswordDTO;
import com.rental_manager.roomie.account_module.services.implementations.ResetPasswordService;
import com.rental_manager.roomie.account_module.services.interfaces.IResetPasswordService;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.AccountNotFoundException;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.ResetPasswordTokenDoesNotMatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reset-password")
public class ResetPasswordController implements IResetPasswordController {

    private final IResetPasswordService resetPasswordService;

    public ResetPasswordController(ResetPasswordService resetPasswordService) {
        this.resetPasswordService = resetPasswordService;
    }

    @Override
    @PostMapping
    public ResponseEntity<Void> generateResetPasswordToken(
            @RequestBody GenerateResetPasswordTokenDTO generateResetPasswordTokenDTO) throws AccountNotFoundException {
        resetPasswordService.generateResetPasswordToken(generateResetPasswordTokenDTO.getEmail());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    @PostMapping("/{token}")
    public ResponseEntity<Void> resetPassword(@PathVariable String token, @RequestBody ResetPasswordDTO resetPasswordDTO)
            throws ResetPasswordTokenDoesNotMatchException {
        resetPasswordService.resetPassword(token, resetPasswordDTO.getNewPassword(),
                resetPasswordDTO.getRepeatNewPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
