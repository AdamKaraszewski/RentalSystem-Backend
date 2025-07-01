package com.rental_manager.roomie.account_module.controllers.interfaces;

import com.rental_manager.roomie.account_module.dtos.ChangeRoleDTO;
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
    ResponseEntity<Void> addRole(UUID accountId, ChangeRoleDTO changeRoleDTO);

    @Operation(summary = "Archive role", description = "Archive role user owe. The HTTP request must contain ChangeAccountRoleDTO in its body.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role successfully archived"),
            @ApiResponse(responseCode = "404", description = "Account does not exist"),
            @ApiResponse(responseCode = "409", description = "User does not have specified role")
    })
    ResponseEntity<Void> archiveRole(UUID accountId, ChangeRoleDTO changeRoleDTO);
}
