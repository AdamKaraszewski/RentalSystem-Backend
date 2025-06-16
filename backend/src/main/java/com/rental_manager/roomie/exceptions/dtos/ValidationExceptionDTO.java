package com.rental_manager.roomie.exceptions.dtos;

import lombok.Getter;

@Getter
public class ValidationExceptionDTO {
    private final int errorCode = 400;
    private final String message;

    public ValidationExceptionDTO(String message) {
        this.message = message;
    }
}
