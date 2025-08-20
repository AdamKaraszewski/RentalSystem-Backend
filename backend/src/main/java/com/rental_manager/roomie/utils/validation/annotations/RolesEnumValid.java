package com.rental_manager.roomie.utils.validation.annotations;

import com.rental_manager.roomie.exceptions.ExceptionMessages;
import com.rental_manager.roomie.utils.validation.validators.RolesEnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = RolesEnumValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RolesEnumValid {
    String message() default ExceptionMessages.ROLE_DOES_NOT_EXIST;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
