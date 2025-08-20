package com.rental_manager.roomie.account_module.dtos;

import com.rental_manager.roomie.config.Constraints;
import com.rental_manager.roomie.exceptions.ExceptionMessages;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ResetPasswordDTO {

    @Schema(description = "User new password.", example = "newPassword")
    @NotNull(message = ExceptionMessages.FIELD_NULL_VALUE)
    @Size(
            min = Constraints.PASSWORD_MIN_LENGTH,
            max = Constraints.PASSWORD_MAX_LENGTH,
            message = ExceptionMessages.PASSWORD_LENGTH_NOT_VALID
    )
    private String password;
}
