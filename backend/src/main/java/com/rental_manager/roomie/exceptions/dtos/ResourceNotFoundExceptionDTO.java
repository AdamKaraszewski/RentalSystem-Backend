package com.rental_manager.roomie.exceptions.dtos;

import lombok.Getter;

@Getter
public class ResourceNotFoundExceptionDTO {
    private final int errorCode = 404;
    private final String message;

    public ResourceNotFoundExceptionDTO(String message) {
        this.message = message;
    }
}
