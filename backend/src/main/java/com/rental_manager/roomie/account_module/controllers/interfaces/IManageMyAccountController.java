package com.rental_manager.roomie.account_module.controllers.interfaces;

import com.rental_manager.roomie.account_module.dtos.AccountDTO;
import com.rental_manager.roomie.account_module.dtos.EditMyAccountDTO;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.AccountNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface IManageMyAccountController {

//    todo: Get id form security context
//    @Operation(summary = "Change my password")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Password successfully changed"),
//            @ApiResponse(responseCode = "404", description = "Account with specified Id does not exist")
//    })
//    ResponseEntity<Void> changeMyPassword(ChangeMyPasswordDTO newPassword) throws AccountNotFoundException;

//    todo: Get id from security context
//    @Operation(summary = "Modify account data")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Account data succesfully modified"),
//            @ApiResponse(responseCode = "404", description = "Account does not exist")
//    })
//    ResponseEntity<AccountDTO> editMyAccount(EditMyAccountDTO editMyAccountDTO) throws AccountNotFoundException;

}
