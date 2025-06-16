package com.rental_manager.roomie.account_module.controllers.interfaces;

import com.rental_manager.roomie.account_module.dtos.RegisterClientDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface IRegisterAccountController {

    @Operation(summary = "Register account with Client role",
            description = "Endpoint provides account registration. The HTTP request must contain RegisterClientDTO in its body.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account successfully created. Verification email will be send.")
    })
    ResponseEntity<Void> registerClientAccount(@RequestBody RegisterClientDTO registerClientDTO);
}
