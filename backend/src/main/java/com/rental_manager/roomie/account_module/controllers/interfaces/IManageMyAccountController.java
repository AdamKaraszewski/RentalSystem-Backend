package com.rental_manager.roomie.account_module.controllers.interfaces;

import com.rental_manager.roomie.account_module.dtos.ChangeMyPasswordDTO;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.AccountNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IManageMyAccountController {

//    todo: remove id argument.
//    todo: Get id form security context
//    @Operation(summary = "Change my password")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Password successfully changed"),
//            @ApiResponse(responseCode = "404", description = "Account with specified Id does not exist")
//    })
//    ResponseEntity<Void> changeMyPassword(UUID id, ChangeMyPasswordDTO newPassword) throws AccountNotFoundException;

}
