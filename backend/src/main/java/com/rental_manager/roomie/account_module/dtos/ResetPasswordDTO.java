package com.rental_manager.roomie.account_module.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ResetPasswordDTO {

    @Schema(description = "User new password.", example = "newPassword")
    private String newPassword;

    @Schema(description = "Repeated new password.", example = "newPassword")
    private String repeatNewPassword;
}
