package com.example.springboot.thriveuniversitybackend.validators.annotations;

import com.example.springboot.thriveuniversitybackend.validators.PasswordConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Password {
    String message() default "Password is invalid";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
