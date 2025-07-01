package com.rental_manager.roomie.account_module.dtos;

import com.rental_manager.roomie.entities.roles.RolesEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ChangeRoleDTO {

    @Schema(description = "Role to be added/deleted", example = "LANDLORD")
    private RolesEnum role;
}
