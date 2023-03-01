package com.example.springboot.thriveuniversitybackend.validators.annotations;

import com.example.springboot.thriveuniversitybackend.validators.EnumConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EnumConstraintValidator.class)
public @interface EnumValue {
    Class<? extends Enum<?>> enumC();
    String message() default "must be any of enum {enumC}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
