package com.rental_manager.roomie.account_module.dtos;

import com.rental_manager.roomie.exceptions.ExceptionMessages;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class GenerateResetPasswordTokenDTO {

    @Schema(description = "User email. The reset password token will be send on entered email", example = "j.kowalski@example.com")
    @Email(message = ExceptionMessages.EMAIL_NOT_VALID)
    @NotNull(message = ExceptionMessages.FIELD_NULL_VALUE)
    private String email;
}
