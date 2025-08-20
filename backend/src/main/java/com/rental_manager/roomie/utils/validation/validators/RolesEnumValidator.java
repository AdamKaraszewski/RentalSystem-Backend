package com.rental_manager.roomie.utils.validation.validators;

import com.rental_manager.roomie.entities.roles.RolesEnum;
import com.rental_manager.roomie.utils.validation.annotations.RolesEnumValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RolesEnumValidator implements ConstraintValidator<RolesEnumValid, String> {

    @Override
    public boolean isValid(String role, ConstraintValidatorContext context) {
        if (role == null) {
            return true;
        }

        try {
            RolesEnum.valueOf(role);
            return true;
        } catch (IllegalArgumentException iae) {
            return false;
        }
    }
}
