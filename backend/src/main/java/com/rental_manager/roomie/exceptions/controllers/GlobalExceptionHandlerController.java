package com.rental_manager.roomie.exceptions.controllers;

import com.rental_manager.roomie.exceptions.business_logic_exceptions.BusinessLogicConflictException;
import com.rental_manager.roomie.exceptions.dtos.BusinessLogicExceptionDTO;
import com.rental_manager.roomie.exceptions.dtos.ErrorOnValidationErrorDTO;
import com.rental_manager.roomie.exceptions.dtos.ValidationErrorsDTO;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.ResourceNotFoundException;
import com.rental_manager.roomie.exceptions.dtos.ResourceNotFoundExceptionDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<ResourceNotFoundExceptionDTO> handleResourceNotFoundException(
            ResourceNotFoundException rnfe) {
        ResourceNotFoundExceptionDTO responseBody =
                new ResourceNotFoundExceptionDTO(rnfe.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {BusinessLogicConflictException.class})
    public ResponseEntity<BusinessLogicExceptionDTO> handleBusinessLogicConflictException(BusinessLogicConflictException ble) {
        BusinessLogicExceptionDTO responseBody =
                new BusinessLogicExceptionDTO(ble.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {

        var validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> new ErrorOnValidationErrorDTO(fe.getField(), fe.getDefaultMessage()))
                .toList();

        ValidationErrorsDTO responseBody = new ValidationErrorsDTO(validationErrors);

        return new ResponseEntity<>(responseBody, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
