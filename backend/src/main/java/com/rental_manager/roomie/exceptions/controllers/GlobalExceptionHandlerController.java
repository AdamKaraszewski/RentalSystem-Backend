package com.rental_manager.roomie.exceptions.controllers;

import com.rental_manager.roomie.exceptions.business_logic_exceptions.BusinessLogicConflictException;
import com.rental_manager.roomie.exceptions.dtos.BusinessLogicExceptionDTO;
import com.rental_manager.roomie.exceptions.dtos.ValidationExceptionDTO;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.ResourceNotFoundException;
import com.rental_manager.roomie.exceptions.dtos.ResourceNotFoundExceptionDTO;
import com.rental_manager.roomie.exceptions.validation_exceptions.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<ResourceNotFoundExceptionDTO> handleResourceNotFoundException(
            ResourceNotFoundException rnfe) {
        ResourceNotFoundExceptionDTO responseBody =
                new ResourceNotFoundExceptionDTO(rnfe.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ValidationException.class})
    public ResponseEntity<ValidationExceptionDTO> handleValidationException(ValidationException ve) {
        ValidationExceptionDTO responseBody =
                new ValidationExceptionDTO(ve.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {BusinessLogicConflictException.class})
    public ResponseEntity<BusinessLogicExceptionDTO> handleBusinessLogicConflictException(BusinessLogicConflictException ble) {
        BusinessLogicExceptionDTO responseBody =
                new BusinessLogicExceptionDTO(ble.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.CONFLICT);
    }
}
