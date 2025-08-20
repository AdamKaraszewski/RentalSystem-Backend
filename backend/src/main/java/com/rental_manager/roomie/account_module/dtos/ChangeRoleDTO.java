package com.rental_manager.roomie.account_module.dtos;

import com.rental_manager.roomie.exceptions.ExceptionMessages;
import com.rental_manager.roomie.utils.validation.annotations.RolesEnumValid;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ChangeRoleDTO {

    @Schema(description = "Role to be added/deleted", example = "LANDLORD")
    @NotNull(message = ExceptionMessages.FIELD_NULL_VALUE)
    @RolesEnumValid
    private String role;
}
