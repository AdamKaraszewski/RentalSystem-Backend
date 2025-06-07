package com.rental_manager.roomie.account_module.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class RegisterClientDTO {

    @Schema(description = "User first name.", example = "Jan")
    private String firstName;

    @Schema(description = "User last name.", example = "Kowalski")
    private String lastName;

    @Schema(description = "User unique login.", example = "JKowalski")
    private String username;

    @Schema(description = "User unique email. The account verification token will be send on entered email",
            example = "j.kowalski@example.com")
    private String email;

    @Schema(description = "User accounts password", example = "password123")
    private String password;
}