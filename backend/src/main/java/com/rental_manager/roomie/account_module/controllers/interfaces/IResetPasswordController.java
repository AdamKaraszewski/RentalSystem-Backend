package com.rental_manager.roomie.account_module.controllers.interfaces;

import com.rental_manager.roomie.account_module.dtos.GenerateResetPasswordTokenDTO;
import com.rental_manager.roomie.account_module.dtos.ResetPasswordDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface IResetPasswordController {

    @Operation(summary = "Send email which allows user to reset their password",
            description = "Endpoint provides generating reset password token - 'Forget my password'. The HTTP request must contain ResetPasswordDTO in its body.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reset password token was created, email should be send."),
            @ApiResponse(responseCode = "404", description = "Account with matching email doesn't exist.")
    })
    ResponseEntity<Void> generateResetPasswordToken(
            @RequestBody GenerateResetPasswordTokenDTO generateResetPasswordTokenDTO);

    @Operation(summary = "Reset password.",
            description = "Endpoint provides resetting user password. The HTTP request must contain ResetPasswordDTO in its body.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password was reset"),
            @ApiResponse(responseCode = "404", description = "Account with matching reset password token doesn't exist.")
    })
    ResponseEntity<Void> resetPassword(@PathVariable String token, @RequestBody ResetPasswordDTO resetPasswordDTO);
}
