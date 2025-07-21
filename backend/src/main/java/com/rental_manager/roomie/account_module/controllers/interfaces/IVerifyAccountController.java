package com.rental_manager.roomie.account_module.controllers.interfaces;

import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.VerificationTokenDoesNotMatchException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface IVerifyAccountController {

    @Operation(summary = "Verify newly registered account using verification token",
            description = "Endpoint provides account verificatin.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account successfully verified."),
            @ApiResponse(responseCode = "404", description = "Token doesn't match to any account.")
    })
    ResponseEntity<Void> verifyAccountUsingVerificationToken(@PathVariable String verificationTokenValue)
            throws VerificationTokenDoesNotMatchException;
}
