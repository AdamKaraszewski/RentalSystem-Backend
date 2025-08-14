package com.rental_manager.roomie.exceptions.dtos;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationErrorsDTO {

    private final int errorCode = 422;
    private final List<ErrorOnValidationErrorDTO> validationErrors;

    public ValidationErrorsDTO(List<ErrorOnValidationErrorDTO> validationErrors) {
        this.validationErrors = validationErrors;
    }
}
