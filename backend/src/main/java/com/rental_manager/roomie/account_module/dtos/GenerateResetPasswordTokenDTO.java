package com.rental_manager.roomie.account_module.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class GenerateResetPasswordTokenDTO {

    @Schema(description = "User email. The reset password token will be send on entered email", example = "j.kowalski@example.com")
    private String email;
}
