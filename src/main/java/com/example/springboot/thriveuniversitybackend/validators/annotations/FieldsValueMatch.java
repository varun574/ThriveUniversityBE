package com.example.springboot.thriveuniversitybackend.validators.annotations;

import com.example.springboot.thriveuniversitybackend.validators.FieldsValueMatchConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = FieldsValueMatchConstraintValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldsValueMatch {
    String message() default "Fields values don't match!";

    String targetField();

    String compareField();
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        FieldsValueMatch[] value();
    }
}
