package com.rental_manager.roomie.exceptions.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorOnValidationErrorDTO {

    private String fieldName;
    private String message;
}
