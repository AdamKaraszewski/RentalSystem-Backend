package com.rental_manager.roomie.exceptions.resource_not_found_exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
