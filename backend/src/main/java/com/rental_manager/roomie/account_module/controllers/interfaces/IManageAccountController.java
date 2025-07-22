package com.rental_manager.roomie.account_module.controllers.interfaces;

import com.rental_manager.roomie.account_module.dtos.ChangeRoleDTO;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.AccountAlreadyBlockedException;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.AccountDoesNotOweAnyRoleException;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.RoleAlreadyOwnedException;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.RoleIsNotOwnedException;
import com.rental_manager.roomie.exceptions.validation_exceptions.RoleDoesNotExistException;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.AccountNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IManageAccountController {

    @Operation(summary = "Add role to certain user",
            description = "Add role to account. If role is archived activate it. The HTTP request must contain ChangeAccountRoleDTO in its body.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role successfully added"),
            @ApiResponse(responseCode = "404", description = "Account does not exist"),
            @ApiResponse(responseCode = "409", description = "User already has this role")
    })
    ResponseEntity<Void> addRole(UUID accountId, ChangeRoleDTO changeRoleDTO) throws AccountNotFoundException,
            RoleAlreadyOwnedException;

    @Operation(summary = "Archive role", description = "Archive role user owe. The HTTP request must contain ChangeAccountRoleDTO in its body.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role successfully archived"),
            @ApiResponse(responseCode = "404", description = "Account does not exist"),
            @ApiResponse(responseCode = "409", description = "User does not have specified role")
    })
    ResponseEntity<Void> archiveRole(UUID accountId, ChangeRoleDTO changeRoleDTO) throws AccountNotFoundException,
            RoleIsNotOwnedException, AccountDoesNotOweAnyRoleException;

    @Operation(summary = "Block specified account",
            description = "Set isActive flag value false")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account successfully blocked"),
            @ApiResponse(responseCode = "404", description = "Account with specified Id does not exist"),
            @ApiResponse(responseCode = "409", description = "Account is already blocked")
    })
    ResponseEntity<Void> blockAccount(UUID accountId) throws AccountNotFoundException, AccountAlreadyBlockedException;
}
