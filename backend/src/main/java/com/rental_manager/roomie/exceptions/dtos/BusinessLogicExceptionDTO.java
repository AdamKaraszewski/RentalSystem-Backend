package com.rental_manager.roomie.exceptions.dtos;

import lombok.Getter;

@Getter
public class BusinessLogicExceptionDTO {
    private final int errorCode = 409;
    private final String message;

    public BusinessLogicExceptionDTO(String message) {
        this.message = message;
    }
}
