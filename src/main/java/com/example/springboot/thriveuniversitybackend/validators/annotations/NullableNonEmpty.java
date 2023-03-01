package com.example.springboot.thriveuniversitybackend.validators.annotations;

import com.example.springboot.thriveuniversitybackend.validators.NullableNonEmptyConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NullableNonEmptyConstraintValidator.class)
public @interface NullableNonEmpty {
    String message() default "must not be empty";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
