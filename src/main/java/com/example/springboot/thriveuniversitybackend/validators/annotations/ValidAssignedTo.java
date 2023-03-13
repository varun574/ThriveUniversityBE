package com.example.springboot.thriveuniversitybackend.validators.annotations;

import com.example.springboot.thriveuniversitybackend.validators.ValidAssignedToConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Constraint(validatedBy = ValidAssignedToConstraintValidator.class)
@Documented
public @interface ValidAssignedTo {
    String message() default "AssignedTo is not valid!";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
