package com.rental_manager.roomie.account_module.dtos;

import com.rental_manager.roomie.config.Constraints;
import com.rental_manager.roomie.exceptions.ExceptionMessages;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RegisterClientDTO {

    @Schema(description = "User first name.", example = "Jan")
    @NotNull(message = ExceptionMessages.FIELD_NULL_VALUE)
    @Size(
            min = Constraints.FIRST_NAME_MIN_LENGTH,
            max = Constraints.FIRST_NAME_MAX_LENGTH,
            message = ExceptionMessages.FIRST_NAME_LENGTH_NOT_VALID
    )
    @Pattern(
            regexp = Constraints.FIRST_NAME_REGEX,
            message = ExceptionMessages.FIRST_NAME_REGEX_NOT_FOLLOWED
    )
    private String firstName;

    @Schema(description = "User last name.", example = "Kowalski")
    @NotNull(message = ExceptionMessages.FIELD_NULL_VALUE)
    @Size(
            min = Constraints.LAST_NAME_MIN_LENGTH,
            max = Constraints.LAST_NAME_MAX_LENGTH,
            message = ExceptionMessages.LAST_NAME_LENGTH_NOT_VALID
    )
    @Pattern(
            regexp = Constraints.LAST_NAME_REGEX,
            message = ExceptionMessages.LAST_NAME_REGEX_NOT_FOLLOWED
    )
    private String lastName;

    @Schema(description = "User unique login.", example = "JKowalski")
    @NotNull(message = ExceptionMessages.FIELD_NULL_VALUE)
    @Size(
            min = Constraints.USERNAME_MIN_LENGTH,
            max = Constraints.USERNAME_MAX_LENGTH,
            message = ExceptionMessages.USERNAME_LENGTH_NOT_VALID
    )
    @Pattern(
            regexp = Constraints.USERNAME_REGEX,
            message = ExceptionMessages.USERNAME_REGEX_NOT_FOLLOWED
    )
    private String username;

    @Schema(description = "User unique email. The account verification token will be send on entered email",
            example = "j.kowalski@example.com")
    @NotNull(message = ExceptionMessages.FIELD_NULL_VALUE)
    @Email(message = ExceptionMessages.EMAIL_NOT_VALID)
    private String email;

    @Schema(description = "User accounts password", example = "password123")
    @NotNull(message = ExceptionMessages.FIELD_NULL_VALUE)
    @Size(
            min = Constraints.PASSWORD_MIN_LENGTH,
            max = Constraints.PASSWORD_MAX_LENGTH,
            message = ExceptionMessages.PASSWORD_LENGTH_NOT_VALID
    )
    private String password;
}